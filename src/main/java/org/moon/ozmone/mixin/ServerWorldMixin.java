package org.moon.ozmone.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import org.moon.ozmone.Util;
import org.moon.ozmone.content.common.entity.ContainerMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "tickBlock", at = @At("HEAD"))
    public void processWorldEvent(ScheduledTick<Block> tick, CallbackInfo ci) {
        ServerWorld world = (ServerWorld)(Object)this;
        List<Entity> entities = world.getOtherEntities(null, Util.blockPosToBox(tick.pos), entity -> entity instanceof ContainerMinecartEntity);
        entities.forEach(e -> {
            ContainerMinecartEntity cart = (ContainerMinecartEntity)e;
            cart.simulateBlock(tick.pos, false, (state, pos) -> {
                state.scheduledTick(world, pos, world.random);
            });
        });
    }

}
