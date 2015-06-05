/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.DavidM1A2.AfraidOfTheDark.common.entities.WereWolf.EntityWereWolf;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

// Silversword item which is a sword
public class ItemAstralSilverSword extends AOTDSword
{
	public ItemAstralSilverSword()
	{
		super(Refrence.astralSilver);
		this.setUnlocalizedName("astralSilverSword");
	}

	// When left clicking attack from silver weapon damage
	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity)
	{
		if (entity instanceof EntityWereWolf)
		{
			if (HasStartedAOTD.get(player))
			{
				entity.attackEntityFrom(Refrence.silverWeapon, 6F);
			}
		}
		return false;
	}
}
