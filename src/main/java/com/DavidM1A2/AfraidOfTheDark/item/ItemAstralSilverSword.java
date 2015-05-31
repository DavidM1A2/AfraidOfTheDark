/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.entities.WereWolf.EntityWereWolf;
import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

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
