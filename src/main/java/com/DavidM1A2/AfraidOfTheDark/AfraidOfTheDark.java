/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;

import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.commands.InsanityCheck;
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
import com.DavidM1A2.AfraidOfTheDark.proxy.IProxy;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Refrence.MOD_ID, name = Refrence.MOD_NAME, version = Refrence.VERSION, guiFactory = Refrence.GUI_FACTORY_CLASS)
public class AfraidOfTheDark
{
	@Mod.Instance(Refrence.MOD_ID)
	public static AfraidOfTheDark instance;

	public static SimpleNetworkWrapper channelNew; // ADDED

	@SidedProxy(clientSide = Refrence.CLIENT_PROXY_CLASS, serverSide = Refrence.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	@Mod.EventHandler
	public void preInitialization(FMLPreInitializationEvent event)
	{
		// Initialize configuration
		ConfigurationHandler.initializataion(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
		// Initialize any player events
		MinecraftForge.EVENT_BUS.register(new PlayerController());
		// Initialize any world events
		MinecraftForge.EVENT_BUS.register(new WorldEvents());
		// Initialize mod items
		ModItems.initialize();
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
		// Initialize renderers
		proxy.registerRenderThings();
		// Initialize the mod channel
		proxy.registerChannel();

		LogHelper.info("Pre-Initialization Complete");
	}

	@Mod.EventHandler
	public void initialization(FMLInitializationEvent event)
	{
		// Initialize Recipes
		ModRecipes.initialize();
		// Initialize key input handler
		FMLCommonHandler.instance().bus().register(new KeyInputEventHandler());
		LogHelper.info("Initialization Complete");
	}

	@Mod.EventHandler
	public void postInitialization(FMLPostInitializationEvent event)
	{
		// Initialize biome registry
		BiomeDictionary.registerAllBiomes();
		LogHelper.info("Post-Initialization Complete");
	}

	@Mod.EventHandler
	public void serverStartingEvent(FMLServerStartingEvent event)
	{
		// Register any player commands
		event.registerServerCommand(new InsanityCheck());
	}
}
