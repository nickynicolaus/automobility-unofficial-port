package io.github.foundationgames.automobility.item;

import io.github.foundationgames.automobility.automobile.attachment.front.AutopilotFrontAttachment;
import io.github.foundationgames.automobility.block.entity.AutopilotSignBlockEntity;
import io.github.foundationgames.automobility.entity.AutomobileEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

public class AutopilotSignBlockItem extends BlockItem {
    public AutopilotSignBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);

        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);

        if (remainingUseDuration % AutopilotSignBlockEntity.BROADCAST_INTERVAL == 0) {
            var lookDir = livingEntity.getLookAngle().multiply(1, 0.25, 1).normalize();

            var box = new AABB(-9, -1, -9, 9, 4, 9);
            box = box.move(livingEntity.position().add(lookDir.scale(3)));

            for (var auto : level.getEntitiesOfClass(AutomobileEntity.class, box)) {
                var fAtt = auto.getFrontAttachment();
                if (fAtt instanceof AutopilotFrontAttachment autopilot) {
                    autopilot.notifyPlayerStop();
                }
            }
        }
    }
}
