/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.debug;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.playerData.Insanity;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

public class DebugSpammer
{
	@SubscribeEvent
	public void debug(LivingUpdateEvent e)
	{
		if (e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer) e.entityLiving;

			if (true)
			{
				String toDebug = "";
				toDebug = toDebug + "\nThe player has started AOTD: " + HasStartedAOTD.get(entityPlayer);
				toDebug = toDebug + "\nThe player is " + Insanity.get(entityPlayer) + "% insane.";
				for (int i = 0; i < ResearchTypes.values().length; i++)
				{
					if (LoadResearchData.get(entityPlayer).getBoolean(LoadResearchData.RESEARCH_DATA + ResearchTypes.values()[i].toString()))
					{
						toDebug = toDebug + "\n" + ResearchTypes.values()[i].formattedString() + " is unlocked.";
					}
				}
				
				toDebug = "";
				Iterator i = entityPlayer.getActivePotionEffects().iterator();
				
				while (i.hasNext())
				{
					PotionEffect next =  (PotionEffect) i.next();
					toDebug = toDebug + "\n" + next.getEffectName();
					toDebug = toDebug + "  " + next.getAmplifier();
				}
				
				if (!toDebug.isEmpty())
				{
					LogHelper.info(toDebug);
				}				
			}
		}
	}
}
