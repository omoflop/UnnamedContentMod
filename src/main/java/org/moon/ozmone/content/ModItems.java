package org.moon.ozmone.content;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ModItems {

    public static final Item ATTACHMENT_RAIL = new BlockItem(ModBlocks.ATTACHMENT_RAIL, new FabricItemSettings().group(ItemGroup.TRANSPORTATION));
    public static final Item REFLECTOR = new BlockItem(ModBlocks.REFLECTOR, new FabricItemSettings().group(ItemGroup.REDSTONE));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier("ozmone:attachment_rail"), ATTACHMENT_RAIL);
        Registry.register(Registry.ITEM, new Identifier("ozmone:reflector"), REFLECTOR);
    }

}
