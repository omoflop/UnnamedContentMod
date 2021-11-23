package org.moon.ozmone;

import io.github.alkyaly.enumextender.EnumExtender;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.moon.ozmone.content.common.ModBlocks;
import org.moon.ozmone.content.common.ModEntities;
import org.moon.ozmone.content.common.ModItems;
import org.moon.ozmone.content.common.entity.ContainerMinecartEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.moon.ozmone.Util.LOGGER;

public class Ozmone implements ModInitializer {

    public static AbstractMinecartEntity.Type CONTAINER;

    public static HashMap<BlockPos, Integer> redstoneRecievedOverrides = new HashMap<>();

    @Override
    public void onInitialize() {
        LOGGER.debug("Starting game with ozmone!");
        ModBlocks.register();
        ModItems.register();
        ModEntities.register();

        extendMinecartTypeEnum();
        ServerTickEvents.END_WORLD_TICK.register(world -> {
             if (redstoneRecievedOverrides.size() > 0) {
                 ArrayList<BlockPos> keys = new ArrayList<>(redstoneRecievedOverrides.keySet());
                 BlockPos key = keys.get(world.random.nextInt(keys.size()));
                 if (Util.getEntityAtBlockPos(ContainerMinecartEntity.class, key, world) == null) {
                     redstoneRecievedOverrides.remove(key);
                 }
             }
        });
    }

    private void extendMinecartTypeEnum() {
        CONTAINER = EnumExtender.addToEnum(AbstractMinecartEntity.Type.class, null, "CONTAINER", Map.of());
    }
}
