/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.debug;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Insanity;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class DebugSpammer
{
	@SubscribeEvent
	public void debug(final LivingUpdateEvent e)
	{
		if (e.entityLiving instanceof EntityPlayer)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) e.entityLiving;

			//			if (entityPlayer.getHeldItem() != null)
			//			{
			//				LogHelper.info(NBTHelper.getInt(entityPlayer.getHeldItem(), "storedVitae"));
			//			}

			//addLight(entityPlayer);
			if (true)
			{
				String toDebug = "";
				toDebug = toDebug + "\nThe player has started AOTD: " + HasStartedAOTD.get(entityPlayer);
				toDebug = toDebug + "\nThe player is " + Insanity.get(entityPlayer) + "% insane.";
				for (int i = 0; i < ResearchTypes.values().length; i++)
				{
					if (Research.get(entityPlayer).getBoolean(Research.RESEARCH_DATA + ResearchTypes.values()[i].toString()))
					{
						toDebug = toDebug + "\n" + ResearchTypes.values()[i].formattedString() + " is unlocked.";
					}
				}

				toDebug = "";
				final Iterator i = entityPlayer.getActivePotionEffects().iterator();

				while (i.hasNext())
				{
					final PotionEffect next = (PotionEffect) i.next();
					toDebug = toDebug + "\n" + next.getEffectName();
					toDebug = toDebug + "  " + next.getAmplifier();
				}

				//				if (!toDebug.isEmpty())
				//				{
				//					LogHelper.info(toDebug);
				//				}
			}
		}
	}
}
