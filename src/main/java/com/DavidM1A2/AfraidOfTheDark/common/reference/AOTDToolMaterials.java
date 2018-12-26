package com.DavidM1A2.AfraidOfTheDark.common.reference;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public enum AOTDToolMaterials
{
	AstralSilver(EnumHelper.addToolMaterial("silverTool", 2, 250, 1, 3.0F, 20)),
	Igneous(EnumHelper.addToolMaterial("igneousTool", 3, 600, 1, 5, 15)),
	StarMetal(EnumHelper.addToolMaterial("starMetalTool", 3, 600, 1, 4, 15)),
	BladeOfExhumation(EnumHelper.addToolMaterial("bladeOfExhumation", 0, 150, 0, 0, 0));

	private ToolMaterial toolMaterial;

	private AOTDToolMaterials(ToolMaterial toolMaterial)
	{
		this.toolMaterial = toolMaterial;
	}

	public ToolMaterial getToolMaterial()
	{
		return this.toolMaterial;
	}
}
