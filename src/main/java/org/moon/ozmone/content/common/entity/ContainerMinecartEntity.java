package org.moon.ozmone.content.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;
import org.moon.ozmone.Ozmone;
import org.moon.ozmone.mixin.AbstractMinecartEntityAccessorMixin;

import java.util.List;

public class ContainerMinecartEntity extends AbstractMinecartEntity {
    public ContainerMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public Type getMinecartType() {
        return Ozmone.CONTAINER;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
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
}
