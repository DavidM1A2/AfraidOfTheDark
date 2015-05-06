package com.DavidM1A2.AfraidOfTheDark.debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DebugSpammer
{
	@SubscribeEvent
	public void debug(LivingUpdateEvent e)
	{
		if (e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) e.entityLiving;
			// LogHelper.info(Insanity.get(p));
		}
	}
}
