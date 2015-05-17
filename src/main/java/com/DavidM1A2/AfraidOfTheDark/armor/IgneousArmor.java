package com.DavidM1A2.AfraidOfTheDark.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		super.onArmorTick(world, player, itemStack);
		if (player.inventory.armorInventory[0] != null && player.inventory.armorInventory[1] != null && player.inventory.armorInventory[2] != null && player.inventory.armorInventory[3] != null)
		{
			if (player.inventory.armorInventory[0].getItem() instanceof IgneousArmor && player.inventory.armorInventory[1].getItem() instanceof IgneousArmor && player.inventory.armorInventory[2].getItem() instanceof IgneousArmor
					&& player.inventory.armorInventory[3].getItem() instanceof IgneousArmor)
			{
				if (player.isBurning())
				{
					player.extinguish();
				}
			}
		}
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
