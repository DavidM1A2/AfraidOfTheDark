package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
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
		registry.registerAll(ModResearches.RESEARCH_LIST);
	}
}
