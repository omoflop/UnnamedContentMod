package org.moon.ozmone.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.RailPlacementHelper;
import net.minecraft.block.enums.RailShape;
import org.moon.ozmone.content.common.block.AttachmentRailBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RailPlacementHelper.class)
public class RailPlacementHelperMixin {

    @Shadow private BlockState state;

    @ModifyVariable(method = "updateBlockState", name = "railShape2", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/RailPlacementHelper;computeNeighbors(Lnet/minecraft/block/enums/RailShape;)V", shift = At.Shift.BEFORE))
    public RailShape updateBlockState(RailShape shape) {
        if (!shape.isAscending() || !(this.state.getBlock() instanceof AttachmentRailBlock)) return shape;
        if (shape == RailShape.ASCENDING_NORTH || shape == RailShape.ASCENDING_SOUTH)
            return RailShape.NORTH_SOUTH;
        return RailShape.EAST_WEST;
    }

}
