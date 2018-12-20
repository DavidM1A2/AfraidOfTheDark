package com.DavidM1A2.afraidofthedark;

import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.handler.BiomeRegister;
import com.DavidM1A2.afraidofthedark.common.handler.BlockRegister;
import com.DavidM1A2.afraidofthedark.common.handler.ConfigurationHandler;
import com.DavidM1A2.afraidofthedark.common.handler.ItemRegister;
import com.DavidM1A2.afraidofthedark.proxy.IProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

/**
 * Main class run when the mod is started up
 */
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, guiFactory = Constants.GUI_FACTORY_CLASS, acceptedMinecraftVersions = Constants.MC_VERSION)
public class AfraidOfTheDark
{
	// Singleton design pattern used here
	@Mod.Instance(Constants.MOD_ID)
	public static AfraidOfTheDark INSTANCE;

	// Sided proxy initialized distinguish client & server side
	@SidedProxy(clientSide = Constants.CLIENT_PROXY_CLASS, serverSide = Constants.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	// Logger used to log any debug messages relating to AOtD
	private Logger aotdLog;

	/**
	 * Called with the forge pre-initialization event
	 *
	 * @param event Pre-init used to register events and various other things (see class names for what each line does)
	 */
	@Mod.EventHandler
	public void preInitialization(FMLPreInitializationEvent event)
	{
		// Grab a reference to the AOTD logger to debug with
		this.aotdLog = event.getModLog();
		// We begin by loading our "afraidofthedark.cfg" file which enables us to change mod settings
		ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
		// We initialize the configuration handler from the suggested file
		configurationHandler.initFromConfigurationFile(event.getSuggestedConfigurationFile());
		// If any changes in the config are detected, we trigger an event that we listen to here
		MinecraftForge.EVENT_BUS.register(configurationHandler);
		// Register our block handler used to add all of our blocks to the game
		MinecraftForge.EVENT_BUS.register(new BlockRegister());
		// Register our item handler used to add all of our items to the game
		MinecraftForge.EVENT_BUS.register(new ItemRegister());
		// Register our biome handler used to add all of our mod biomes to the game
		MinecraftForge.EVENT_BUS.register(new BiomeRegister());
		// Register our GUI handler that lets us open UIs for specific players
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new AOTDGuiHandler());
	}

	/**
	 * Called with the forge initialization event
	 *
	 * @param event Initialization event is responsible for renders and recipes
	 */
	@Mod.EventHandler
	public void initialization(FMLInitializationEvent event)
	{
		// Initialize leaf renderers (client side only)
		proxy.initializeLeafRenderers();
		// Initialize any ore-dictionary entries
		proxy.initializeOreDictionary();
		// Initialize smelting recipes
		proxy.initializeSmeltingRecipes();
	}

	/**
	 * Called with the forge post-initialization event
	 *
	 * @param event  Register the research achieved overlay on the client side only
	 */
	@Mod.EventHandler
	public void postInitialization(FMLPostInitializationEvent event)
	{

	}

	/**
	 * Called when the server gets initialized
	 *
	 * @param event Register commands when the server starts
	 */
	@Mod.EventHandler
	public void serverStartingEvent(FMLServerStartingEvent event)
	{

	}

	/**
	 * @return Returns the AOTD logger to be used whenever logging any debug messages
	 */
	public Logger getLogger()
	{
		return aotdLog;
	}
}
