package org.moon.ozmone.content.common;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.moon.ozmone.content.common.entity.ContainerMinecartEntity;

public class ModEntities {

    public static final EntityType<ContainerMinecartEntity> CONTAINER_MINECART = EntityType.Builder.create(ContainerMinecartEntity::new, SpawnGroup.MISC).setDimensions(0.98F, 0.7F).maxTrackingRange(8).build("container_minecart");

    public static void register() {
        Registry.register(Registry.ENTITY_TYPE, new Identifier("ozmone:container_minecart"), CONTAINER_MINECART);
    }

}
