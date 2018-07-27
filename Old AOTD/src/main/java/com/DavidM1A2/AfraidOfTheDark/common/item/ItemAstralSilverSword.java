/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.common.entities.ICanTakeSilverDamage;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDSword;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDamageSources;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDToolMaterials;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

// Silversword item which is a sword
public class ItemAstralSilverSword extends AOTDSword
{
	public ItemAstralSilverSword()
	{
		super(AOTDToolMaterials.AstralSilver.getToolMaterial());
		this.setUnlocalizedName("astral_silver_sword");
		this.setRegistryName("astral_silver_sword");
	}

	// When left clicking attack from silver weapon damage
	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity)
	{
		if (player.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.AstralSilver))
		{
			if (entity instanceof ICanTakeSilverDamage)
			{
				if (player.getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD())
				{
					entity.attackEntityFrom(AOTDDamageSources.causeSilverDamage(player), 12.0F);
				}
			}
			return super.onLeftClickEntity(stack, player, entity);
		}
		else
		{
			entity.attackEntityFrom(DamageSource.causePlayerDamage(player), this.getDamage(stack));
			return true;
		}
	}
}
