package io.github.foundationgames.automobility.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.foundationgames.automobility.item.AutomobilityItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;

public class AutoMechanicTableRecipeSerializer implements RecipeSerializer<AutoMechanicTableRecipe> {
    public static final AutoMechanicTableRecipeSerializer INSTANCE = new AutoMechanicTableRecipeSerializer();

    public static final Codec<ItemStack> AUTO_COMPONENT_STACK = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.ITEM_NON_AIR_CODEC.fieldOf("item").forGetter(ItemStack::getItemHolder),
            Codec.INT.fieldOf("count").forGetter(ItemStack::getCount),
            ResourceLocation.CODEC.fieldOf("component").forGetter(s -> s.get(AutomobilityItems.COMPONENT_GENERIC_AUTO_PART.require()))
    ).apply(inst, (i, c, p) -> {
        var stack = i.value().getDefaultInstance();
        stack.setCount(c);
        stack.set(AutomobilityItems.COMPONENT_GENERIC_AUTO_PART.require(), p);
        return stack;
    }));

    public static final MapCodec<AutoMechanicTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("category").forGetter(AutoMechanicTableRecipe::getCategory),
            Codec.list(Ingredient.CODEC).fieldOf("ingredients").forGetter(r -> r.ingredients),
            AUTO_COMPONENT_STACK.fieldOf("result").forGetter(AutoMechanicTableRecipe::getResultItem),
            Codec.INT.fieldOf("sortnum").forGetter(r -> r.sortNum)
    ).apply(inst, AutoMechanicTableRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AutoMechanicTableRecipe> STREAM_CODEC =
            StreamCodec.of(AutoMechanicTableRecipeSerializer::toNetwork, AutoMechanicTableRecipeSerializer::fromNetwork);

    public static AutoMechanicTableRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
        var category = ResourceLocation.tryParse(buf.readUtf());

        int size = buf.readByte();
        var ingredients = new ArrayList<Ingredient>();
        for (int i = 0; i < size; i++) {
            ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
        }

        var result = ItemStack.STREAM_CODEC.decode(buf);
        int sortNum = buf.readInt();

        return new AutoMechanicTableRecipe(category, ingredients, result, sortNum);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buf, AutoMechanicTableRecipe recipe) {
        buf.writeUtf(recipe.category.toString());
        buf.writeByte(recipe.ingredients.size());
        recipe.ingredients.forEach(ing -> Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing));
        ItemStack.STREAM_CODEC.encode(buf, recipe.result);
        buf.writeInt(recipe.sortNum);
    }

    @Override
    public MapCodec<AutoMechanicTableRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AutoMechanicTableRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
