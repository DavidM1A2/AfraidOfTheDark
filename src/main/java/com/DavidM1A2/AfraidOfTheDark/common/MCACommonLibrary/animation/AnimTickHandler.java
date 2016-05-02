package com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class AnimTickHandler
{
	private List<IMCAnimatedEntity> activeEntities = new ArrayList<IMCAnimatedEntity>();

	public AnimTickHandler()
	{
		FMLCommonHandler.instance().bus().register(this);
	}

	public synchronized void addEntity(IMCAnimatedEntity entity)
	{
		if (ConfigurationHandler.enableAOTDAnimations)
			activeEntities.add(entity);
	}

	//Called when the client ticks. 
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (!activeEntities.isEmpty())
		{
			if (event.phase == Phase.START)
			{
				Iterator<IMCAnimatedEntity> entities = this.activeEntities.iterator();
				while (entities.hasNext())
				{
					IMCAnimatedEntity entity = entities.next();

					entity.getAnimationHandler().animationsUpdate();

					if (((Entity) entity).isDead)
						entities.remove();
				}
			}
		}
	}

	//Called when the server ticks. Usually 20 ticks a second. 
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event)
	{
		if (!activeEntities.isEmpty())
		{
			if (event.phase == Phase.START)
			{
				Iterator<IMCAnimatedEntity> entities = this.activeEntities.iterator();
				while (entities.hasNext())
				{
					IMCAnimatedEntity entity = entities.next();

					entity.getAnimationHandler().animationsUpdate();

					if (((Entity) entity).isDead)
						entities.remove();
				}
			}
		}
	}
}
