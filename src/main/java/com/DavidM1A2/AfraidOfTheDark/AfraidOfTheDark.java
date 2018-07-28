package com.DavidM1A2.AfraidOfTheDark;

import com.DavidM1A2.AfraidOfTheDark.common.constants.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.constants.Constants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/*
 * Main class run when the mod is started up
 */
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, guiFactory = Constants.GUI_FACTORY_CLASS, acceptedMinecraftVersions = Constants.MC_VERSION)
public class AfraidOfTheDark
{
	/**
	 * Singleton design pattern used here
	 */
	@Mod.Instance(Constants.MOD_ID)
	public static AfraidOfTheDark INSTANCE;

	/**
	 * Called with the forge pre-initialization event
	 *
	 * @param event Pre-init used to register events and various other things (see class names for what each line does)
	 */
	@Mod.EventHandler
	public void preInitialization(final FMLPreInitializationEvent event)
	{
		// We begin by loading our "afraidofthedark.cfg" file which enables us to change mod settings
		ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
		// We initialize the configuration handler from the suggested file
		configurationHandler.initFromConfigurationFile(event.getSuggestedConfigurationFile());
		// If any changes in the config are detected, we trigger an event that we listen to here
		MinecraftForge.EVENT_BUS.register(configurationHandler);
	}

	/**
	 * Called with the forge initialization event
	 *
	 * @param event Initialization event is responsible for renders and recipes
	 */
	@Mod.EventHandler
	public void initialization(final FMLInitializationEvent event)
	{

	}

	/**
	 * Called with the forge post-initialization event
	 *
	 * @param event  Register the research achieved overlay on the client side only
	 */
	@Mod.EventHandler
	public void postInitialization(final FMLPostInitializationEvent event)
	{

	}

	/**
	 * Called when the server gets initialized
	 *
	 * @param event Register commands when the server starts
	 */
	@Mod.EventHandler
	public void serverStartingEvent(final FMLServerStartingEvent event)
	{

	}
}
