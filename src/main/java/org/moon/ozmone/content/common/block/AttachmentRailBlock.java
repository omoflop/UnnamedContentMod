package org.moon.ozmone.content.common.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.*;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.moon.ozmone.Util;
import org.moon.ozmone.content.common.ModEntities;
import org.moon.ozmone.content.common.entity.ContainerMinecartEntity;

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
            BlockPos abovePos = pos.up();
            BlockState aboveBlock = world.getBlockState(abovePos);
            boolean powered = state.get(PoweredRailBlock.POWERED);

            Class<? extends AbstractMinecartEntity> clazz = powered ? MinecartEntity.class : AbstractMinecartEntity.class;
            AbstractMinecartEntity cart = Util.getEntityAtBlockPos(clazz, pos, world);

            if (cart != null) {
                BlockState cartBlockState = Util.getMinecartBlock(cart);
                if (powered && cartBlockState.isAir() && !aboveBlock.isAir() && cart instanceof MinecartEntity minecart) {
                    if (!placeBlockInSpecialMinecart(minecart, aboveBlock, abovePos, world)) {
                        //&& !PistonBlock.isMovable(aboveBlock, world, abovePos, Direction.UP, false, Direction.UP))
                        placeBlockInMinecart(minecart, aboveBlock, abovePos, world);
                    }
                } else if (!powered && aboveBlock.isAir()) {
                    if (cart instanceof ContainerMinecartEntity) {
                        takeBlockFromMinecart(cart, abovePos, world);
                    } else if (!(cart instanceof MinecartEntity)) {
                        takeBlockFromMinecartVariant(cart, abovePos, world);
                    }
                }
            }
        }
    }

    public static void placeBlockInMinecart(MinecartEntity cart, BlockState state, BlockPos pos, World world) {
        if (state.isAir()) return;

        cart.setCustomBlock(state);
        world.setBlockState(pos, Blocks.AIR.getDefaultState());

        ContainerMinecartEntity containerMinecart = new ContainerMinecartEntity(ModEntities.CONTAINER_MINECART, world);
        Util.copyMinecartData(cart, containerMinecart);
        cart.remove(Entity.RemovalReason.DISCARDED);

        world.spawnEntity(containerMinecart);
    }
    public static boolean placeBlockInSpecialMinecart(MinecartEntity cart, BlockState state, BlockPos pos, World world) {
        if (state.isAir()) return false;

        AbstractMinecartEntity specialMinecart = null;

        if (state.getBlock() == Blocks.CHEST || state.getBlock() == Blocks.HOPPER) {
            specialMinecart = state.getBlock() == Blocks.CHEST ? new ChestMinecartEntity(EntityType.CHEST_MINECART, world) : new HopperMinecartEntity(EntityType.HOPPER_MINECART, world);
            Inventory inv = (Inventory) world.getBlockEntity(pos);
            for(int i = 0; i < inv.size(); i++) {
                ((StorageMinecartEntity) specialMinecart).setStack(i, inv.getStack(i));
                inv.setStack(i, ItemStack.EMPTY);
            }
        } else if (state.getBlock() == Blocks.TNT) {
            specialMinecart = new TntMinecartEntity(EntityType.TNT_MINECART, world);
        }
        else if (state.getBlock() == Blocks.FURNACE) {
            specialMinecart = new FurnaceMinecartEntity(EntityType.FURNACE_MINECART, world);
        }

        if (specialMinecart != null) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());

            Util.copyMinecartData(cart, specialMinecart);
            cart.remove(Entity.RemovalReason.DISCARDED);

            world.spawnEntity(specialMinecart);
            return true;
        }
        return false;
    }
    public static void takeBlockFromMinecart(AbstractMinecartEntity cart, BlockPos pos, World world) {
        world.setBlockState(pos, Util.getMinecartBlock(cart));
        cart.setCustomBlock(Blocks.AIR.getDefaultState());

        MinecartEntity minecart = new MinecartEntity(EntityType.MINECART, world);
        Util.copyMinecartData(cart, minecart);
        cart.remove(Entity.RemovalReason.DISCARDED);

        world.spawnEntity(minecart);
    }
    public static void takeBlockFromMinecartVariant(AbstractMinecartEntity cart, BlockPos pos, World world) {
        if (cart instanceof StorageMinecartEntity storageCart) {
            Inventory inv = null;
            if (cart.getMinecartType() == AbstractMinecartEntity.Type.CHEST) {
                BlockState state = Blocks.CHEST.getDefaultState();
                world.setBlockState(pos, state);

                ChestBlockEntity blockEntity = new ChestBlockEntity(pos, state);
                world.addBlockEntity(blockEntity);
                inv = blockEntity;
            } else if (cart.getMinecartType() == AbstractMinecartEntity.Type.HOPPER) {
                BlockState state = Blocks.HOPPER.getDefaultState();
                world.setBlockState(pos, state);

                HopperBlockEntity blockEntity = new HopperBlockEntity(pos, state);
                world.addBlockEntity(blockEntity);
                inv = blockEntity;
            }
            if (inv != null) {
                for (int i = 0; i < storageCart.size(); i++) {
                    if (inv.size() < storageCart.size())
                        inv.setStack(i, storageCart.getStack(i));
                    else
                        dropStack(world, pos, storageCart.getStack(i));
                }
            }
        } else if (cart instanceof TntMinecartEntity) {
            world.setBlockState(pos, Blocks.TNT.getDefaultState());
        } else if (cart instanceof FurnaceMinecartEntity) {
            world.setBlockState(pos, Blocks.FURNACE.getDefaultState());
        }

        MinecartEntity minecart = new MinecartEntity(EntityType.MINECART, world);
        Util.copyMinecartData(cart, minecart);
        cart.remove(Entity.RemovalReason.DISCARDED);

        world.spawnEntity(minecart);
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
        return (world.getBlockState(pos.up()).isAir()) ? 0 : 1;
    }

    static {
        VoxelShape a = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
        VoxelShape b = Block.createCuboidShape(0, 12, 0, 16, 16, 16);
        OUTLINE_SHAPE = VoxelShapes.union(a, b);
    }
}
