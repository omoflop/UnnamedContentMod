package org.moon.ozmone.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.moon.ozmone.Ozmone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {

    @Inject(method = "isReceivingRedstonePower", at = @At("HEAD"), cancellable = true)
    public void isReceivingRedstonePower(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        Integer level = getEntityRedstoneLevel(blockPos);
        if (level != null) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Redirect(method = "getEmittedRedstonePower", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getWeakRedstonePower(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)I"))
    public int getEmittedRedstonePower(BlockState instance, BlockView blockView, BlockPos blockPos, Direction direction) {
        Integer level = getEntityRedstoneLevel(blockPos);
        if (level != null) {
            return getEntityRedstoneLevel(blockPos);
        }
        return instance.getWeakRedstonePower((World)((Object)this), blockPos, direction);
    }

    @Unique
    public Integer getEntityRedstoneLevel(BlockPos pos) {
        Integer level = Ozmone.redstoneRecievedOverrides.get(pos);
        if (level == 0) {
            Ozmone.redstoneRecievedOverrides.remove(pos);
            return null;
        }
        return level;
    }
}
