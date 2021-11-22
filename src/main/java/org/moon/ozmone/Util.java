package org.moon.ozmone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.moon.ozmone.mixin.AbstractMinecartEntityAccessorMixin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class Util {

    public static final Logger LOGGER = LoggerFactory.getLogger("ozmone");

    public static Box blockPosToBox(BlockPos pos) {
        float x = pos.getX();
        float y = pos.getY();
        float z = pos.getZ();

        return new Box(x + 0.1, y, z + 0.1, x + 0.9, y + 1, z + 0.9);
    }

    public static BlockState getMinecartBlock(AbstractMinecartEntity entity) {
        return Block.getStateFromRawId(entity.getDataTracker().get(AbstractMinecartEntityAccessorMixin.getCustomBlockID()));
    }

    public static <T extends Entity> T getEntityAtBlockPos(Class<T> clazz, BlockPos pos, World world) {
        List<T> entities = world.getEntitiesByType(TypeFilter.instanceOf(clazz), blockPosToBox(pos), p -> p.getBlockPos().equals(pos));
        if (entities.size() == 0) return null;
        return entities.get(entities.size() - 1);
    }

    public static <I extends AbstractMinecartEntity,O extends AbstractMinecartEntity> void copyMinecartData(I originalEntity, O newEntity) {
        DataTracker oldData = originalEntity.getDataTracker();
        DataTracker newData = newEntity.getDataTracker();
        newData.writeUpdatedEntries(oldData.getAllEntries());

        newEntity.setPosition(originalEntity.getPos());
        newEntity.setVelocity(originalEntity.getVelocity());

        AbstractMinecartEntityAccessorMixin originalEntityAccess = (AbstractMinecartEntityAccessorMixin) originalEntity;
        AbstractMinecartEntityAccessorMixin newEntityAccess      = (AbstractMinecartEntityAccessorMixin) newEntity;

        newEntityAccess.setClientX(originalEntityAccess.getClientX());
        newEntityAccess.setClientY(originalEntityAccess.getClientY());
        newEntityAccess.setClientZ(originalEntityAccess.getClientZ());
        newEntityAccess.setClientXVelocity(originalEntityAccess.getClientXVelocity());
        newEntityAccess.setClientYVelocity(originalEntityAccess.getClientYVelocity());
        newEntityAccess.setClientZVelocity(originalEntityAccess.getClientZVelocity());
        newEntityAccess.setClientPitch(originalEntityAccess.getClientPitch());
        newEntityAccess.setClientYaw(originalEntityAccess.getClientYaw());
        newEntityAccess.setClientInterpolationSteps(originalEntityAccess.getClientInterpolationSteps());
    }
}

