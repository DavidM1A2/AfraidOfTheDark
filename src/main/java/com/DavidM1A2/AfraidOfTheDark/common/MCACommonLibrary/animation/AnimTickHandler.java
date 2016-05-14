package com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class AnimTickHandler
{
	private List<IMCAnimatedEntity> activeEntitiesClient = new LinkedList<IMCAnimatedEntity>();
	private static AnimTickHandler instance = new AnimTickHandler();

	public static AnimTickHandler getInstance()
	{
		return AnimTickHandler.instance;
	}

	public void addEntity(IMCAnimatedEntity entity)
	{
		if (ConfigurationHandler.enableAOTDAnimations)
			synchronized (this.activeEntitiesClient)
			{
				if (!this.activeEntitiesClient.contains(entity))
					activeEntitiesClient.add(entity);
			}
	}

	@SubscribeEvent
	public void onClientDisconnected(GuiOpenEvent event)
	{
		if (event.gui instanceof GuiMainMenu)
			this.activeEntitiesClient.clear();
	}

	//Called when the client ticks. 
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (!activeEntitiesClient.isEmpty())
		{
			if (event.phase == Phase.START)
			{
				Iterator<IMCAnimatedEntity> entities = this.activeEntitiesClient.iterator();
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
