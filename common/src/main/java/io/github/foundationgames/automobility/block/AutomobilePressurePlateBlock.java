package io.github.foundationgames.automobility.block;

import com.mojang.serialization.MapCodec;
import io.github.foundationgames.automobility.entity.AutomobileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class AutomobilePressurePlateBlock extends PressurePlateBlock {
    public static final MapCodec<AutomobilePressurePlateBlock> CODEC = simpleCodec(AutomobilePressurePlateBlock::new);

    public AutomobilePressurePlateBlock(Properties properties) {
        super(BlockSetType.IRON, properties);
    }

    @Override
    protected int getSignalStrength(Level level, BlockPos pos) {
        return getEntityCount(level, TOUCH_AABB.move(pos), AutomobileEntity.class) > 0 ? 15 : 0;
    }
}
