package io.github.foundationgames.automobility.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.foundationgames.automobility.Automobility;
import io.github.foundationgames.automobility.item.AutomobileComponentItem;
import io.github.foundationgames.automobility.item.AutomobilityItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.StreamSupport;

public final class AutoMechanicTableRecipeSerializer {
    private static final Codec<LegacyIngredient> LEGACY_INGREDIENT_OBJECT = RecordCodecBuilder.create(inst -> inst.group(
            Item.CODEC.optionalFieldOf("item").forGetter(LegacyIngredient::item),
            TagKey.codec(Registries.ITEM).optionalFieldOf("tag").forGetter(LegacyIngredient::tag)
    ).apply(inst, LegacyIngredient::new));

    public static final Codec<Ingredient> LEGACY_INGREDIENT = Codec.either(Ingredient.CODEC, LEGACY_INGREDIENT_OBJECT)
            .flatXmap(
                    either -> either.map(DataResult::success, LegacyIngredient::toIngredient),
                    ingredient -> DataResult.success(Either.left(ingredient)));

    public static final Codec<AutoComponentResult> AUTO_COMPONENT_STACK = RecordCodecBuilder.create(inst -> inst.group(
            Item.CODEC.fieldOf("item").forGetter(AutoComponentResult::item),
            Codec.INT.optionalFieldOf("count", 1).forGetter(AutoComponentResult::count),
            Identifier.CODEC.fieldOf("component").forGetter(AutoComponentResult::component)
    ).apply(inst, AutoComponentResult::new));

    public static final MapCodec<AutoMechanicTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Identifier.CODEC.fieldOf("category").forGetter(AutoMechanicTableRecipe::getCategory),
            Codec.list(LEGACY_INGREDIENT).fieldOf("ingredients").forGetter(r -> r.ingredients),
            AUTO_COMPONENT_STACK.fieldOf("result").forGetter(AutoMechanicTableRecipe::getResultDescriptor),
            Codec.INT.fieldOf("sortnum").forGetter(r -> r.sortNum)
    ).apply(inst, AutoMechanicTableRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AutoMechanicTableRecipe> STREAM_CODEC =
            StreamCodec.of(AutoMechanicTableRecipeSerializer::toNetwork, AutoMechanicTableRecipeSerializer::fromNetwork);

    public static final RecipeSerializer<AutoMechanicTableRecipe> INSTANCE = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    public static AutoMechanicTableRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
        var category = Identifier.tryParse(buf.readUtf());

        int size = buf.readByte();
        var ingredients = new ArrayList<Ingredient>();
        for (int i = 0; i < size; i++) {
            ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
        }

        var result = AutoComponentResult.fromNetwork(buf);
        int sortNum = buf.readInt();

        return new AutoMechanicTableRecipe(category, ingredients, result, sortNum);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buf, AutoMechanicTableRecipe recipe) {
        buf.writeUtf(recipe.category.toString());
        buf.writeByte(recipe.ingredients.size());
        recipe.ingredients.forEach(ing -> Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing));
        recipe.result.toNetwork(buf);
        buf.writeInt(recipe.sortNum);
    }

    private record LegacyIngredient(Optional<Holder<Item>> item, Optional<TagKey<Item>> tag) {
        private DataResult<Ingredient> toIngredient() {
            if (item.isPresent() == tag.isPresent()) {
                return DataResult.error(() -> "Legacy ingredient must define exactly one of item or tag");
            }

            if (item.isPresent()) {
                return DataResult.success(Ingredient.of(HolderSet.direct(item.get())));
            }

            var holders = StreamSupport.stream(BuiltInRegistries.ITEM.getTagOrEmpty(tag.get()).spliterator(), false).toList();
            if (holders.isEmpty()) {
                return DataResult.error(() -> "Unknown or empty item tag '" + tag.get().location() + "'");
            }

            return DataResult.success(Ingredient.of(HolderSet.direct(holders)));
        }
    }

    public record AutoComponentResult(Holder<Item> item, int count, Identifier component) {
        public static AutoComponentResult fromStack(ItemStack stack) {
            var item = stack.getItem();
            Identifier component = Automobility.rl("empty");

            if (item instanceof AutomobileComponentItem.Dynamic<?> cItem) {
                component = cItem.getComponentId(stack, null);
            } else if (item instanceof AutomobileComponentItem.Builtin<?> cItem) {
                component = cItem.getComponent(stack, null).getId();
            }

            return new AutoComponentResult(stack.typeHolder(), stack.getCount(), component);
        }

        public static AutoComponentResult fromNetwork(RegistryFriendlyByteBuf buf) {
            return new AutoComponentResult(Item.STREAM_CODEC.decode(buf), buf.readVarInt(), buf.readIdentifier());
        }

        public void toNetwork(RegistryFriendlyByteBuf buf) {
            Item.STREAM_CODEC.encode(buf, this.item);
            buf.writeVarInt(this.count);
            buf.writeIdentifier(this.component);
        }

        public ItemStack createStack() {
            var stack = new ItemStack(this.item, this.count);
            var item = stack.getItem();
            if (item instanceof AutomobileComponentItem.Dynamic<?> cItem) {
                cItem.setComponent(stack, (ResourceKey) ResourceKey.create(cItem.registryKey, this.component));
            } else {
                stack.set(AutomobilityItems.COMPONENT_GENERIC_AUTO_PART.require(), this.component);
            }
            return stack;
        }
    }

    private AutoMechanicTableRecipeSerializer() {}
}
