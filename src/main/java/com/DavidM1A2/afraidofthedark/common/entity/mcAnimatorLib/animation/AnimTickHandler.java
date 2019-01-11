package com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.DavidM1A2.afraidofthedark.common.handler.ConfigurationHandler;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class AnimTickHandler
{
	private List<IMCAnimatedEntity> activeEntitiesClient = Collections.synchronizedList(new LinkedList<>());
	private static AnimTickHandler instance = new AnimTickHandler();

	public static AnimTickHandler getInstance()
	{
		return AnimTickHandler.instance;
	}

	public synchronized void addEntity(IMCAnimatedEntity entity)
	{
		if (AfraidOfTheDark.INSTANCE.getConfigurationHandler().enableAOTDAnimations())
			if (!this.activeEntitiesClient.contains(entity))
				activeEntitiesClient.add(entity);
	}

	@SubscribeEvent
	public void onClientDisconnected(GuiOpenEvent event)
	{
		if (event.getGui() instanceof GuiMainMenu)
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
				synchronized (this.activeEntitiesClient)
				{
					for (ListIterator<IMCAnimatedEntity> entities = this.activeEntitiesClient.listIterator(); entities.hasNext();)
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
}
