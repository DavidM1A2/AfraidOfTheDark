package com.DavidM1A2.afraidofthedark;

import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.handler.*;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.PacketHandler;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.AOTDWorldGenerator;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.WorldHeightMapper;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.WorldStructurePlanner;
import com.DavidM1A2.afraidofthedark.proxy.IProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
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

	// Logger used to log any debug messages relating to AOTD
	private Logger logger;

	// Packet handler used to send and receive AOTD packets
	private final PacketHandler packetHandler = new PacketHandler(Constants.MOD_ID);

	// Configuration handler used to read and update the afraidofthedark.cfg file
	private ConfigurationHandler configurationHandler;

	/**
	 * Called with the forge pre-initialization event
	 *
	 * @param event Pre-init used to register events and various other things (see class names for what each line does)
	 */
	@Mod.EventHandler
	public void preInitialization(FMLPreInitializationEvent event)
	{
		// Grab a reference to the AOTD logger to debug with
		this.logger = event.getModLog();
		// We initialize the configuration handler from the suggested file
		this.configurationHandler = new ConfigurationHandler(event.getSuggestedConfigurationFile());
		// If any changes in the config are detected, we trigger an event that we listen to here
		MinecraftForge.EVENT_BUS.register(configurationHandler);
		// Register our block handler used to add all of our blocks to the game
		MinecraftForge.EVENT_BUS.register(new BlockRegister());
		// Register our item handler used to add all of our items to the game
		MinecraftForge.EVENT_BUS.register(new ItemRegister());
		// Register our entity handler used to add all of our entities to the game
		MinecraftForge.EVENT_BUS.register(new EntityRegister());
		// Register our biome handler used to add all of our mod biomes to the game
		MinecraftForge.EVENT_BUS.register(new BiomeRegister());
		// Register our sound handler used to add all of our mod sounds to the game
		MinecraftForge.EVENT_BUS.register(new SoundRegister());
		// Register our structure handler used to add all of our mod structures to the game
		MinecraftForge.EVENT_BUS.register(new StructureRegister());
		// Forward any capability events to our capability handler
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		// Forward any chunk creation events to our world generation height mapper
		MinecraftForge.EVENT_BUS.register(new WorldHeightMapper());
		// Forward any chunk creation events to our world structure planner
		MinecraftForge.EVENT_BUS.register(new WorldStructurePlanner());
		// Register our AOTD world generator
		GameRegistry.registerWorldGenerator(new AOTDWorldGenerator(), configurationHandler.getWorldGenPriority());
		// Register our GUI handler that lets us open UIs for specific players
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new AOTDGuiHandler());
		// Register all AOTD packets
		proxy.registerPackets();
		// Initialize entity renderers (client side only)
		proxy.initializeEntityRenderers();
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
	 * @return The AOTD logger to be used whenever logging any debug messages
	 */
	public Logger getLogger()
	{
		return logger;
	}

	/**
	 * @return The AOTD packet handler to send and receive packets
	 */
	public PacketHandler getPacketHandler()
	{
		return this.packetHandler;
	}

	/**
	 * @return The AOTD configuration handler used to manage the afraidofthedark.cfg file
	 */
	public ConfigurationHandler getConfigurationHandler()
	{
		return this.configurationHandler;
	}
}
