package org.moon.ozmone.content;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.moon.ozmone.content.block.AttachmentRailBlock;
import org.moon.ozmone.content.block.ReflectorBlock;

public class ModBlocks {

    public static final AttachmentRailBlock ATTACHMENT_RAIL = new AttachmentRailBlock();
    public static final ReflectorBlock REFLECTOR = new ReflectorBlock();

    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier("ozmone:attachment_rail"), ATTACHMENT_RAIL);
        Registry.register(Registry.BLOCK, new Identifier("ozmone:reflector"), REFLECTOR);
    }

}
