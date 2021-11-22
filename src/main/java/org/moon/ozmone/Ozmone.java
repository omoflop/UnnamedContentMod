package org.moon.ozmone;

import io.github.alkyaly.enumextender.EnumExtender;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.moon.ozmone.content.common.ModBlocks;
import org.moon.ozmone.content.common.ModEntities;
import org.moon.ozmone.content.common.ModItems;

import java.util.Map;

import static org.moon.ozmone.Util.LOGGER;

public class Ozmone implements ModInitializer {

    public static AbstractMinecartEntity.Type CONTAINER;

    @Override
    public void onInitialize() {
        LOGGER.debug("Starting game with ozmone!");
        ModBlocks.register();
        ModItems.register();
        ModEntities.register();

        extendMinecartTypeEnum();
    }

    private void extendMinecartTypeEnum() {
        CONTAINER = EnumExtender.addToEnum(AbstractMinecartEntity.Type.class, null, "CONTAINER", Map.of());
    }
}
