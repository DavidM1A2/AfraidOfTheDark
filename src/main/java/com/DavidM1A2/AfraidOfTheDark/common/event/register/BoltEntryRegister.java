package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModBoltEntries;
import com.DavidM1A2.afraidofthedark.common.registry.bolt.BoltEntry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register bolt entry event and registers all of our bolt entries
 */
public class BoltEntryRegister
{
	/**
	 * Called by forge to register any of our bolt entries
	 *
	 * @param event The event to register to
	 */
	@SubscribeEvent
	public void registerBoltEntries(RegistryEvent.Register<BoltEntry> event)
	{
		// Grab the registry for bolt entries
		IForgeRegistry<BoltEntry> registry = event.getRegistry();

		// Register all of our mod bolt entries
		registry.registerAll(ModBoltEntries.BOLT_ENTRY_LIST);
	}
}
