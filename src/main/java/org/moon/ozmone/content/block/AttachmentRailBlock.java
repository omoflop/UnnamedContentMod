package org.moon.ozmone.content.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.moon.ozmone.mixin.AbstractMinecartEntityAccessorMixin;

import java.util.List;

public class AttachmentRailBlock extends AbstractRailBlock {

    public static final VoxelShape OUTLINE_SHAPE;
    public static final EnumProperty<RailShape> STRAIGHT_FLAT_RAIL_SHAPE = EnumProperty.of("shape", RailShape.class, (shape) -> shape == RailShape.NORTH_SOUTH || shape == RailShape.EAST_WEST);

    public AttachmentRailBlock() {
        super(false, FabricBlockSettings.copyOf(Blocks.RAIL).sounds(BlockSoundGroup.WOOD).collidable(false));
        setDefaultState(super.getStateManager().getDefaultState().with(PoweredRailBlock.POWERED, false).with(WATERLOGGED, true).with(getShapeProperty(), RailShape.NORTH_SOUTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PoweredRailBlock.POWERED);
        builder.add(WATERLOGGED);
        builder.add(getShapeProperty());
        super.appendProperties(builder);
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return STRAIGHT_FLAT_RAIL_SHAPE;
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient) {
            BlockPos targetPos = pos.up();
            BlockState aboveBlock = world.getBlockState(targetPos);
            MinecartEntity cart = getClosestMinecart(pos, world);
            boolean powered = state.get(PoweredRailBlock.POWERED);

            if (cart != null) {
                int blockID = cart.getDataTracker().get(AbstractMinecartEntityAccessorMixin.getCustomBlockID());
                if (blockID == 0 && powered) {
                    if (!aboveBlock.isAir() && PistonBlock.isMovable(aboveBlock, world, pos, Direction.UP, false, Direction.UP)) {
                        cart.setCustomBlock(aboveBlock);
                        world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
                    }
                } else if (!powered && aboveBlock.isAir()) {
                    world.setBlockState(targetPos, Block.getStateFromRawId(blockID));
                    cart.setCustomBlock(Blocks.AIR.getDefaultState());
                }
            }
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.isClient) return;

        if (world.getBlockState(pos.down()).isAir()) {
            dropStacks(state, world, pos);
            world.removeBlock(pos, notify);
        } else if (world.getBlockState(pos).isOf(this)) {
            world.setBlockState(pos, state.with(PoweredRailBlock.POWERED, world.isReceivingRedstonePower(pos)), 1 | 2 | 4 | 8);
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return (world.getBlockState(pos.up()).isAir()) ? 0 : (int) Math.min(state.getHardness(world, pos), 15);
    }

    public static Box makeBoundingBox(BlockPos pos) {
        float x = pos.getX();
        float y = pos.getY();
        float z = pos.getZ();

        return new Box(x+0.1, y, z+0.1, x+0.9, y+1, z+0.9);
    }
    public static MinecartEntity getClosestMinecart(BlockPos pos, World world) {

        Box bbox = makeBoundingBox(pos);
        List<MinecartEntity> minecarts = world.getEntitiesByType(TypeFilter.instanceOf(MinecartEntity.class), bbox, p -> p.getBlockPos().equals(pos));
        if (minecarts.size() == 0) return null;
        return minecarts.get(minecarts.size()-1);
    }

    static {
        VoxelShape a = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
        VoxelShape b = Block.createCuboidShape(0, 12, 0, 16, 16, 16);
        OUTLINE_SHAPE = VoxelShapes.union(a, b);
    }
}
