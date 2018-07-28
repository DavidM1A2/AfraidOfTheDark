package com.DavidM1A2.AfraidOfTheDark.common.reference;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;

public enum AOTDArmorMaterials
{
	StarMetal(EnumHelper.addArmorMaterial("starMetal", "texture", 100, new int[] { 3, 8, 6, 3 }, 20, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0f)), 
	Igneous(EnumHelper.addArmorMaterial("igneous", "texture", 100, new int[] { 3, 8, 6, 3 }, 20,  SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0f));

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
