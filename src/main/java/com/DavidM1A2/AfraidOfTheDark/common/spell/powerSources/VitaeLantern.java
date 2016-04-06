/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemVitaeLantern;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class VitaeLantern extends PowerSource
{
	@Override
	public boolean attemptToCast(Spell toCast)
	{
		double cost = toCast.getCost();
		EntityPlayer entityPlayer = AfraidOfTheDark.proxy.getSpellOwner(toCast);
		double totalLanternVitae = 0;
		for (int i = 0; i < entityPlayer.inventory.mainInventory.length; i++)
		{
			ItemStack current = entityPlayer.inventory.mainInventory[i];
			if (current != null && current.getItem() instanceof ItemVitaeLantern)
			{
				ItemVitaeLantern lantern = (ItemVitaeLantern) current.getItem();
				totalLanternVitae = totalLanternVitae + lantern.getStoredVitae(current);
			}
		}

		if (totalLanternVitae >= cost)
		{
			for (int i = 0; i < entityPlayer.inventory.mainInventory.length; i++)
			{
				ItemStack current = entityPlayer.inventory.mainInventory[i];
				if (current != null && current.getItem() instanceof ItemVitaeLantern)
				{
					ItemVitaeLantern lantern = (ItemVitaeLantern) current.getItem();
					if (cost - lantern.getStoredVitae(current) > 0)
					{
						cost = cost - lantern.getStoredVitae(current);
						lantern.addVitae(current, -lantern.getStoredVitae(current));
					}
					else
					{
						lantern.addVitae(current, (int) -cost);
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public String notEnoughEnergyMsg()
	{
		return "I do not have enough vitae in my lantern(s) to cast this.";
	}

	@Override
	public PowerSources getType()
	{
		return PowerSources.VitaeLantern;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
	}
}
