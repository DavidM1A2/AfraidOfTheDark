package com.davidm1a2.afraidofthedark.common.constants;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Class storing tool material constants. These are registered through the enum helper class and not through the standard forge system
 */
public class ModToolMaterials
{
    public static final Item.ToolMaterial BLADE_OF_EXHUMATION = EnumHelper.addToolMaterial("blade_of_exhumation", 0, 150, 0, 0, 0);
    public static final Item.ToolMaterial ASTRAL_SILVER = EnumHelper.addToolMaterial("astral_silver", 2, 250, 1, 3.0F, 20);
    public static final Item.ToolMaterial IGNEOUS = EnumHelper.addToolMaterial("igneous", 3, 600, 1, 5, 15);
    public static final Item.ToolMaterial STAR_METAL = EnumHelper.addToolMaterial("star_metal", 3, 600, 1, 4, 15);
}
