package org.moon.ozmone.content.common.block.iface;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.moon.ozmone.content.common.block.MachineBlock;

public interface IActivatable<T extends MachineBlock> {
    BooleanProperty ACTIVE = BooleanProperty.of("active");

    void onActivate(BlockState state, T block, BlockPos pos, World world);

    void onDeactivate(BlockState state, T block, BlockPos pos, World world);

    static <T extends IActivatable<MachineBlock>>boolean isActivated(BlockState state, T block) {
        return block != null && state.get(ACTIVE);
    }

    static <T extends IActivatable<MachineBlock>>void setActive(BlockState state, T block, BlockPos pos, World world, boolean active) {
        boolean temp = state.get(ACTIVE);
        BlockState newState = state.with(ACTIVE, active);
        world.setBlockState(pos, newState);

        if (!temp && active) block.onActivate(newState, (MachineBlock) block, pos, world);
        if (temp && !active) block.onDeactivate(newState, (MachineBlock) block, pos, world);
    }

    static <T extends IActivatable<MachineBlock>>void toggleActive(BlockState state, T block, BlockPos pos, World world) {
        setActive(state, block, pos, world, !isActivated(state, block));
    }
}
