package com.DavidM1A2.AfraidOfTheDark.debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

public class DebugSpammer
{
	@SubscribeEvent
	public void debug(LivingUpdateEvent e)
	{
		if (e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) e.entityLiving;

			LogHelper.info(LoadResearchData.get(p));
		}
	}
}
