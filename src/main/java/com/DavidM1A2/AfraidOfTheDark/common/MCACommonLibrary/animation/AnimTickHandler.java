package com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation;

import java.util.ArrayList;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class AnimTickHandler
{
	//private ArrayList<IMCAnimatedEntity> activeEntities = new ArrayList<IMCAnimatedEntity>();
	//private ArrayList<IMCAnimatedEntity> removableEntities = new ArrayList<IMCAnimatedEntity>();

	private ArrayList<IMCAnimatedEntity> activeEntitiesServer = new ArrayList<IMCAnimatedEntity>();
	private ArrayList<IMCAnimatedEntity> removableEntitiesServer = new ArrayList<IMCAnimatedEntity>();
	private ArrayList<IMCAnimatedEntity> activeEntitiesClient = new ArrayList<IMCAnimatedEntity>();
	private ArrayList<IMCAnimatedEntity> removableEntitiesClient = new ArrayList<IMCAnimatedEntity>();

	public AnimTickHandler()
	{
		FMLCommonHandler.instance().bus().register(this);
	}

	public void addEntity(IMCAnimatedEntity entity)
	{
		activeEntitiesServer.add(entity);
		activeEntitiesClient.add(entity);
	}

	//Called when the client ticks. 
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (!activeEntitiesClient.isEmpty())
		{
			if (event.phase == Phase.START)
			{
				for (IMCAnimatedEntity entity : activeEntitiesClient)
				{
					entity.getAnimationHandler().animationsUpdate();

					if (((Entity) entity).isDead)
					{
						removableEntitiesClient.add(entity);
					}
				}

				for (IMCAnimatedEntity entity : removableEntitiesClient)
				{
					activeEntitiesClient.remove(entity);
				}
				removableEntitiesClient.clear();
			}
		}
	}

	//Called when the server ticks. Usually 20 ticks a second. 
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event)
	{
		if (!activeEntitiesServer.isEmpty())
		{
			if (event.phase == Phase.START)
			{
				for (IMCAnimatedEntity entity : activeEntitiesServer)
				{
					entity.getAnimationHandler().animationsUpdate();

					if (((Entity) entity).isDead)
					{
						removableEntitiesServer.add(entity);
					}
				}

				for (IMCAnimatedEntity entity : removableEntitiesServer)
				{
					activeEntitiesServer.remove(entity);
				}
				removableEntitiesServer.clear();
			}
		}
	}

	//Called when a new frame is displayed (See fps) 
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event)
	{
	}

	//Called when the world ticks
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event)
	{
	}
}
