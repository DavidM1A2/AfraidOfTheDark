package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public enum AOTDArmorMaterials
{
	StarMetal(EnumHelper.addArmorMaterial("starMetal", "texture", 100, new int[] { 3, 8, 6, 3 }, 20)), Igneous(EnumHelper.addArmorMaterial("igneous", "texture", 100, new int[] { 3, 8, 6, 3 }, 20));

	private ArmorMaterial armorMaterial;

	private AOTDArmorMaterials(ArmorMaterial armorMaterial)
	{
		this.armorMaterial = armorMaterial;
	}

	public ArmorMaterial getArmorMaterial()
	{
		return this.armorMaterial;
	}
}
