package com.DavidM1A2.afraidofthedark.common.handler;

import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.DavidM1A2.afraidofthedark.common.constants.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register sound event and registers all of our sounds
 */
public class SoundRegister
{
	/**
	 * Called by forge to register any of our sounds
	 *
	 * @param event The event to register to
	 */
	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<SoundEvent> event)
	{
		IForgeRegistry<SoundEvent> registry = event.getRegistry();
		// Register all sounds in our mod
		registry.registerAll(ModSounds.SOUND_LIST);
	}
}
