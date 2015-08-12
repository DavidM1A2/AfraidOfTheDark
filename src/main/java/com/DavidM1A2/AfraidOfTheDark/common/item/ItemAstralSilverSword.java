/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDSword;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

// Silversword item which is a sword
public class ItemAstralSilverSword extends AOTDSword
{
	public ItemAstralSilverSword()
	{
		super(Constants.AOTDToolMaterials.astralSilver);
		this.setUnlocalizedName("astralSilverSword");
	}

	// When left clicking attack from silver weapon damage
	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity)
	{
		if (Research.isResearched(player, ResearchTypes.AstralSilver))
		{
			if (entity instanceof EntityWerewolf)
			{
				if (HasStartedAOTD.get(player))
				{
					entity.attackEntityFrom(Constants.AOTDDamageSources.getSilverDamage(player), 12.0F);
				}
			}
			return super.onLeftClickEntity(stack, player, entity);
		}
		else
		{
			entity.attackEntityFrom(DamageSource.causePlayerDamage(player), this.func_150931_i());
			return true;
		}
	}
}
