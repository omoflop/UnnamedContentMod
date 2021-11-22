package org.moon.ozmone.content.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import org.moon.ozmone.content.common.ModEntities;

public class ModEntityRenderers {

    public static void register() {
        EntityRendererRegistry.register(ModEntities.CONTAINER_MINECART, context -> new MinecartEntityRenderer<>(context, EntityModelLayers.MINECART));
    }

}
