package org.moon.ozmone.content.common.entity;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.moon.ozmone.Ozmone;
import org.moon.ozmone.Util;
import org.moon.ozmone.mixin.AbstractMinecartEntityAccessorMixin;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class ContainerMinecartEntity extends AbstractMinecartEntity {
    public ContainerMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    boolean onRedstone = false;

    @Override
    public Type getMinecartType() {
        return Ozmone.CONTAINER;
    }

    @Override
    public void tick() {
        boolean wasOnRedstone = onRedstone;
        int pow = world.getReceivedRedstonePower(getBlockPos());
        onRedstone = pow > 0;
        if (wasOnRedstone != onRedstone) {
            simulateBlock(getBlockPos(), true, (state, pos) -> {
                state.neighborUpdate(world, pos, onRedstone ? Blocks.REDSTONE_BLOCK : Blocks.AIR, pos, true);
            });
        }

        super.tick();
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        BlockState state = Util.getMinecartBlock(this);

        if (BlockTags.STAIRS.contains(state.getBlock())) {
            if (player.shouldCancelInteraction()) {
                return ActionResult.PASS;
            } else if (this.hasPassengers()) {
                return ActionResult.PASS;
            } else if (!this.world.isClient) {
                return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
            } else {
                return ActionResult.SUCCESS;
            }
        }

        AtomicReference<ActionResult> result = new AtomicReference<>();
        simulateBlock(getBlockPos(), true, (blockState, pos) -> {
            result.set(state.onUse(world, player, hand, new BlockHitResult(this.getPos(), Direction.UP, pos, true)));
        });

        if (result.get().isAccepted()) return result.get();

        return super.interact(player, hand);
    }

    @Override
    public void dropItems(DamageSource damageSource) {
        int customBlock = getDataTracker().get(AbstractMinecartEntityAccessorMixin.getCustomBlockID());
        BlockState state = Block.getStateFromRawId(customBlock);

        if (world instanceof ServerWorld serverWorld) {
            List<ItemStack> drops = Block.getDroppedStacks(state, serverWorld, getBlockPos(), null);
            drops.forEach(stack -> {
                ItemScatterer.spawn(serverWorld, getX(), getY(), getZ(), stack);
            });
        }

        super.dropItems(damageSource);
    }

    public void simulateBlock(BlockPos simulationPos, boolean updateWorld, BiConsumer<BlockState, BlockPos> stateConsumer) {
        BlockState state = Util.getMinecartBlock(this);

        int flags = updateWorld ? 1 | 2 | 4 | 8 | 16 | 32 | 64 | 128 | 256 | 512 : 3;
        int depth = updateWorld ? 512 : 0;

        BlockState originalState = world.getBlockState(simulationPos);
        world.setBlockState(simulationPos, state, flags, depth);

        stateConsumer.accept(state, simulationPos);

        BlockState postUseState = world.getBlockState(simulationPos);
        setCustomBlock(postUseState);

        if (postUseState.emitsRedstonePower()) {
            int pow = postUseState.getWeakRedstonePower(world, simulationPos, Direction.UP);
            if (pow > 0) {
                Ozmone.redstoneRecievedOverrides.put(simulationPos, pow);
            } else Ozmone.redstoneRecievedOverrides.remove(simulationPos);
        }

        world.setBlockState(simulationPos, originalState, flags, depth);
    }
}
