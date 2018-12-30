package com.DavidM1A2.afraidofthedark.common.handler;


import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * Class used to register any custom registries we are using. Because registry registering happens before
 * pre-initialization, we must mark this class as being a mod event bus subscriber
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class RegistryRegister
{
	/**
	 * Called to add any custom new registries to the game. This must be static in order for the event
	 * bus subscriber annotation to fire and add our custom registry
	 *
	 * @param event The event specifying we can now add new registries into the game
	 */
	@SubscribeEvent
	public static void registryRegister(RegistryEvent.NewRegistry event)
	{
		// Create a new registry for structures with the name 'afraidofthedark:structures'
		new RegistryBuilder<Structure>()
				.setType(Structure.class)
				.setName(new ResourceLocation(Constants.MOD_ID, "structures"))
				.create();
	}
}
