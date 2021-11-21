package org.moon.ozmone;

import net.fabricmc.api.ModInitializer;
import org.moon.ozmone.content.ModBlocks;
import org.moon.ozmone.content.ModItems;

import static org.moon.ozmone.Util.*;

public class Ozmone implements ModInitializer {

    @Override
    public void onInitialize() {
        LOGGER.debug("Starting game with ozmone!");
        ModBlocks.register();
        ModItems.register();
    }
}
