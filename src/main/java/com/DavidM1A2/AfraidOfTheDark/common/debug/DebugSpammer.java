/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.debug;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DebugSpammer
{
	@SubscribeEvent
	public void debug(final LivingUpdateEvent e)
	{
		if (e.entityLiving instanceof EntityPlayer)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) e.entityLiving;

			LogHelper.info("Player can research dark forest? " + Research.canResearch(entityPlayer, ResearchTypes.DarkForest));
		}
	}
}
