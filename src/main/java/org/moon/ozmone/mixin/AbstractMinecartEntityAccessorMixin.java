package org.moon.ozmone.mixin;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractMinecartEntity.class)
public interface AbstractMinecartEntityAccessorMixin {

    @Accessor("CUSTOM_BLOCK_ID")
    static TrackedData<Integer> getCustomBlockID() {
        return null;
    }

}
