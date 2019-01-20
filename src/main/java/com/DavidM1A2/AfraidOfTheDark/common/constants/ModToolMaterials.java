package com.DavidM1A2.afraidofthedark.common.constants;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Class storing tool material constants. These are registered through the enum helper class and not through the standard forge system
 */
public class ModToolMaterials
{
	public static final Item.ToolMaterial BLADE_OF_EXHUMATION = EnumHelper.addToolMaterial("blade_of_exhumation", 0, 150, 0, 0, 0);
}
