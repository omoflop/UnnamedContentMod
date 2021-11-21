package org.moon.ozmone;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.client.render.RenderLayer;
import org.moon.ozmone.content.ModBlocks;

@Environment(EnvType.CLIENT)
public class OzmoneClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMapImpl.INSTANCE.putBlock(ModBlocks.ATTACHMENT_RAIL, RenderLayer.getCutout());
    }
}
