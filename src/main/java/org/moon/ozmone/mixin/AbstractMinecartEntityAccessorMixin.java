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

    @Accessor("clientX") double getClientX();
    @Accessor("clientY") double getClientY();
    @Accessor("clientZ") double getClientZ();
    @Accessor("clientYaw") double getClientYaw();
    @Accessor("clientPitch") double getClientPitch();
    @Accessor("clientXVelocity") double getClientXVelocity();
    @Accessor("clientYVelocity") double getClientYVelocity();
    @Accessor("clientZVelocity") double getClientZVelocity();
    @Accessor("clientInterpolationSteps") int getClientInterpolationSteps();

    @Accessor("clientX") void setClientX(double value);
    @Accessor("clientY") void setClientY(double value);
    @Accessor("clientZ") void setClientZ(double value);
    @Accessor("clientYaw") void setClientYaw(double value);
    @Accessor("clientPitch") void setClientPitch(double value);
    @Accessor("clientXVelocity") void setClientXVelocity(double value);
    @Accessor("clientYVelocity") void setClientYVelocity(double value);
    @Accessor("clientZVelocity") void setClientZVelocity(double value);
    @Accessor("clientInterpolationSteps") void setClientInterpolationSteps(int value);


}
