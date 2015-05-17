package com.DavidM1A2.AfraidOfTheDark.armor;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public class IgneousArmor extends AOTDArmor
{
	public IgneousArmor(ArmorMaterial armorMaterial, int renderIndex, int type)
	{
		super(armorMaterial, renderIndex, type);
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
		this.setUnlocalizedName((type == 0) ? "igneousHelmet" : (type == 1) ? "igneousChestplate" : (type == 2) ? "igneousLeggings" : "igneousBoots");
	}

	@Override
	//This is pretty self explanatory
	public String getArmorTexture(ItemStack armor, Entity entity, int slot, String type)
	{
		if (armor.getItem() == ModItems.igneousLeggings)
		{
			return "afraidofthedark:textures/armor/igneous_2.png";
		}
		else
		{
			return "afraidofthedark:textures/armor/igneous_1.png";
		}
	}
}
