package org.moon.ozmone.content.common.block.iface;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPowerable<BaseType extends IPowerable<BaseType>> {
    BooleanProperty POWERED = BooleanProperty.of("powered");

    void onPowered(BlockState state, BaseType block, BlockPos pos, World world);

    void onUnpowered(BlockState state, BaseType block, BlockPos pos, World world);

    default boolean isPowered(BlockState state, BaseType block) {
        return block != null && state.get(POWERED);
    }

    default void setPowered(BlockState state, BaseType block, BlockPos pos, World world, boolean active) {
        boolean temp = state.get(POWERED);
        BlockState newState = state.with(POWERED, active);
        world.setBlockState(pos, newState);

        if (!temp && active) block.onPowered(newState, block, pos, world);
        if (temp && !active) block.onUnpowered(newState, block, pos, world);
    }
}
