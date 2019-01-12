package com.DavidM1A2.afraidofthedark.common.handler;

import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.research.base.Research;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register research event and registers all of our researches
 */
public class ResearchRegister
{
	/**
	 * Called by forge to register any of our researches
	 *
	 * @param event The event to register to
	 */
	@SubscribeEvent
	public void registerResearch(RegistryEvent.Register<Research> event)
	{
		IForgeRegistry<Research> registry = event.getRegistry();

		// Register each research in our research list
		for (Research research : ModResearches.RESEARCH_LIST)
		{
			registry.register(research);
		}
	}
}
