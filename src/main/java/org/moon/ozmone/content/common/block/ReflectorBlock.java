package org.moon.ozmone.content.common.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ReflectorBlock extends WallMountedBlock {

    public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 7);

    public ReflectorBlock() {
        super(FabricBlockSettings.copyOf(Blocks.REPEATER));
        setDefaultState(getStateManager().getDefaultState().with(ROTATION, 0).with(FACE, WallMountLocation.FLOOR).with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
        builder.add(FACE);
        builder.add(FACING);
        super.appendProperties(builder);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {

        return super.getPlacementState(ctx);
        //double val = (180.0F - ctx.getPlayerYaw()) * 16.0F / 360.0F;
        //return super.getPlacementState(ctx).with(ROTATION, MathHelper.floor( (val % 8) + 12.5D) % 8);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
