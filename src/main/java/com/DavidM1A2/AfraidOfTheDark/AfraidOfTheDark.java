/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.client.gui.ResearchAchieved;
import com.DavidM1A2.AfraidOfTheDark.commands.InsanityCheck;
import com.DavidM1A2.AfraidOfTheDark.debug.DebugSpammer;
import com.DavidM1A2.AfraidOfTheDark.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.handler.KeyInputEventHandler;
import com.DavidM1A2.AfraidOfTheDark.handler.PlayerController;
import com.DavidM1A2.AfraidOfTheDark.handler.WorldEvents;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModEntities;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModFurnaceRecipies;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModGeneration;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModOreDictionaryCompatability;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModRecipes;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModThreads;
import com.DavidM1A2.AfraidOfTheDark.proxy.IProxy;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

@Mod(modid = Refrence.MOD_ID, name = Refrence.MOD_NAME, version = Refrence.VERSION, guiFactory = Refrence.GUI_FACTORY_CLASS)
public class AfraidOfTheDark
{
	@Mod.Instance(Refrence.MOD_ID)
	public static AfraidOfTheDark instance;

	private static SimpleNetworkWrapper channelNew;

	@SidedProxy(clientSide = Refrence.CLIENT_PROXY_CLASS, serverSide = Refrence.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	@Mod.EventHandler
	public void preInitialization(FMLPreInitializationEvent event)
	{
		// Initialize configuration
		ConfigurationHandler.initializataion(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
		// Initialize any player events
		PlayerController controller = new PlayerController();
		MinecraftForge.EVENT_BUS.register(controller);
		FMLCommonHandler.instance().bus().register(controller);
		// Initialize any world events
		MinecraftForge.EVENT_BUS.register(new WorldEvents());
		// Initialize debug file to spam chat with variables
		MinecraftForge.EVENT_BUS.register(new DebugSpammer());
		// Initialize mod items
		ModItems.initialize(event.getSide());
		// Initialize mod blocks
		ModBlocks.initialize();
		// Initialize mod entities
		ModEntities.intialize();
		// Initialize mod world generation
		ModGeneration.initialize();
		// Initialize furnace recipes
		ModFurnaceRecipies.initialize();
		// Initialize mod biomes
		ModBiomes.initialize();
		// Initialize the ORE-Dictionary compatability
		ModOreDictionaryCompatability.initialize();
		// Initialize GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		// Initialize key bindings
		proxy.registerKeyBindings();
		// Initialize the mod channel
		proxy.registerChannel();
		// Setup mod threads
		ModThreads.register();

		LogHelper.info("Pre-Initialization Complete");
	}

	@Mod.EventHandler
	public void initialization(FMLInitializationEvent event)
	{
		// Initialize Recipes
		ModRecipes.initialize();
		// Initialize mod item renderers
		ModItems.initializeRenderers(event.getSide());
		// Initialize block renderers
		ModBlocks.initializeRenderers(event.getSide());
		// Initialize key input handler
		FMLCommonHandler.instance().bus().register(new KeyInputEventHandler());
		// Initialize renderers
		proxy.registerRenderThings();
		LogHelper.info("Initialization Complete");
	}

	@Mod.EventHandler
	public void postInitialization(FMLPostInitializationEvent event)
	{
		if (event.getSide() == Side.CLIENT)
		{
			Refrence.researchAchievedOverlay = new ResearchAchieved(Minecraft.getMinecraft());
		}
		LogHelper.info("Post-Initialization Complete");
	}

	@Mod.EventHandler
	public void serverStartingEvent(FMLServerStartingEvent event)
	{
		// Register any player commands
		event.registerServerCommand(new InsanityCheck());
	}

	@Mod.EventHandler
	public void serverStartedEvent(FMLServerStartedEvent event)
	{
		// Launch any threads to  be used in game
		ModThreads.startInGameThreads();
	}

	@Mod.EventHandler
	public void serverStoppedEvent(FMLServerStoppedEvent event)
	{
		// Stop any ingame threads
		ModThreads.stopInGameThreads();
	}

	public static SimpleNetworkWrapper getSimpleNetworkWrapper()
	{
		return AfraidOfTheDark.channelNew;
	}

	public static void setSimpleNetworkWrapper(SimpleNetworkWrapper wrapper)
	{
		AfraidOfTheDark.channelNew = wrapper;
	}
}
