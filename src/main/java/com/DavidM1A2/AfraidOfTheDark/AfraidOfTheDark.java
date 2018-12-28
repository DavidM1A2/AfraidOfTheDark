/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.client.gui.ResearchAchieved;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimTickHandler;
import com.DavidM1A2.AfraidOfTheDark.common.commands.AOTDCommands;
import com.DavidM1A2.AfraidOfTheDark.common.commands.CMDInsanityCheck;
import com.DavidM1A2.AfraidOfTheDark.common.debug.DebugSpammer;
import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.handler.FogRenderingEvents;
import com.DavidM1A2.AfraidOfTheDark.common.handler.GuiEventHandler;
import com.DavidM1A2.AfraidOfTheDark.common.handler.KeyInputEventHandler;
import com.DavidM1A2.AfraidOfTheDark.common.handler.PlayerController;
import com.DavidM1A2.AfraidOfTheDark.common.handler.WorldEvents;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModEntities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModFurnaceRecipies;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModGeneration;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModOreDictionaryCompatability;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModPotionEffects;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModRecipes;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.PacketHandler;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDSchematics;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.Schematic;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.proxy.IProxy;

import com.google.common.base.CaseFormat;
import io.netty.util.concurrent.DefaultThreadFactory;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.ArrayUtils;

/*
 * Main class run when the mod is started up
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class AfraidOfTheDark
{
	/**
	 * Singleton design pattern used here
	 */
	@Mod.Instance(Reference.MOD_ID)
	public static AfraidOfTheDark instance;

	/**
	 * Executor used in calling delayed code
	 */
	private final ScheduledExecutorService delayedExecutor = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("AfraidOfTheDark"));

	/**
	 * Channel for sending and receiving packets
	 */
	private final PacketHandler packetHandler = new PacketHandler("AOTD Packets");

	/**
	 * Sided proxy used to distinguish client & server side
	 */
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	/**
	 * @param event
	 *            Pre-init used to register events and various other things (see class names for what each line does)
	 */
	@Mod.EventHandler
	public void preInitialization(final FMLPreInitializationEvent event)
	{
		// Initialize configuration
		ConfigurationHandler.initializataion(event.getSuggestedConfigurationFile());
		ModCapabilities.initialize();
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
		// Initialize any player events
		final PlayerController controller = new PlayerController();
		MinecraftForge.EVENT_BUS.register(controller);
		MinecraftForge.EVENT_BUS.register(new FogRenderingEvents());
		MinecraftForge.TERRAIN_GEN_BUS.register(controller);
		FMLCommonHandler.instance().bus().register(controller);
		// Initialize any world events
		WorldEvents worldEvents = new WorldEvents();
		FMLCommonHandler.instance().bus().register(worldEvents);
		MinecraftForge.EVENT_BUS.register(worldEvents);
		if (event.getSide() == Side.CLIENT)
		{
			MinecraftForge.EVENT_BUS.register(new GuiEventHandler());
			// Register the animation handler
			MinecraftForge.EVENT_BUS.register(AnimTickHandler.getInstance());
		}
		// Initialize debug file to spam chat with variables
		if (Reference.isDebug)
			MinecraftForge.EVENT_BUS.register(new DebugSpammer());
		// Initialize mod blocks
		ModBlocks.initialize();
		// Initialize mod items
		ModItems.initialize(event.getSide());
		// Initialize mod entities
		ModEntities.intialize();
		// Initialize mod world generation
		ModGeneration.initialize();
		// Initialize furnace recipes
		ModFurnaceRecipies.initialize();
		// Initialize dimensions
		ModDimensions.intialize();
		// Initialize mod biomes
		ModBiomes.initialize();
		// Initialize the ORE-Dictionary compatability
		ModOreDictionaryCompatability.initialize();
		// Initialize GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(AfraidOfTheDark.instance, new GuiHandler());
		// Initialize key bindings
		AfraidOfTheDark.proxy.registerKeyBindings();
		// Initialize the mod channel
		AfraidOfTheDark.proxy.registerChannel();
		// Initialize font renderers
		AfraidOfTheDark.proxy.registerMiscelaneous();
		// Setup Potion effects
		ModPotionEffects.initialize();
		// Initialize renderers
		AfraidOfTheDark.proxy.registerEntityRenders();

		if (ConfigurationHandler.debugMessages)
		{
			LogHelper.info("Pre-Initialization Complete");
		}

		// Hack used to update schematics from 1.8.9 to 1.12
		for (AOTDSchematics schematics : AOTDSchematics.values())
		{
			Schematic schematic = schematics.getSchematic();
			try
			{
				OutputStream schemOutStream = new FileOutputStream(new File("./schem/" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, schematics.name()) + ".schematic"));
				NBTTagCompound nbtOut = new NBTTagCompound();

				nbtOut.setShort("Width", schematic.getWidth());
				nbtOut.setShort("Height", schematic.getHeight());
				nbtOut.setShort("Length", schematic.getLength());
				nbtOut.setTag("TileEntities", schematic.getTileentities());
				nbtOut.setTag("Entities", schematic.getEntities());

				int[] dataArr = new int[schematic.getData().length];
				byte[] rawData = schematic.getData();
				for (int i = 0; i < rawData.length; i++)
				{
					dataArr[i] = rawData[i];
				}
				NBTTagIntArray data = new NBTTagIntArray(dataArr);
				nbtOut.setTag("Data", data);

				short[] rawBlocks = schematic.getBlocks();
				NBTTagList blocks = new NBTTagList();
				for (short rawBlock : rawBlocks)
				{
					String registryName = Block.getBlockById(rawBlock).getRegistryName();
					registryName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, registryName);
					blocks.appendTag(new NBTTagString(registryName));
				}
				nbtOut.setTag("Blocks", blocks);

				CompressedStreamTools.writeCompressed(nbtOut, schemOutStream);
				schemOutStream.close();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		System.exit(0);
	}

	/**
	 * @param event
	 *            Initialization event is responsible for renders and recipes
	 */
	@Mod.EventHandler
	public void initialization(final FMLInitializationEvent event)
	{
		// Initialize Recipes
		ModRecipes.initialize();
		// Initialize mod item renderers
		AfraidOfTheDark.proxy.registerItemRenders();
		// Initialize block renderers
		AfraidOfTheDark.proxy.registerBlockRenders();
		AfraidOfTheDark.proxy.registerMiscRenders();
		// Initialize key input handler on client side only
		if (event.getSide() == Side.CLIENT)
		{
			FMLCommonHandler.instance().bus().register(new KeyInputEventHandler());
		}

		if (ConfigurationHandler.debugMessages)
		{
			LogHelper.info("Initialization Complete");
		}
	}

	/**
	 * @param event
	 *            Register the research achieved overlay on the client side only
	 */
	@Mod.EventHandler
	public void postInitialization(final FMLPostInitializationEvent event)
	{
		if (event.getSide() == Side.CLIENT)
		{
			ClientData.researchAchievedOverlay = new ResearchAchieved(Minecraft.getMinecraft());
		}

		if (ConfigurationHandler.debugMessages)
		{
			LogHelper.info("Post-Initialization Complete");
		}
	}

	/**
	 * @param event
	 *            Register commands when the server starts
	 */
	@Mod.EventHandler
	public void serverStartingEvent(final FMLServerStartingEvent event)
	{
		// Register any player commands
		if (Reference.isDebug)
		{
			event.registerServerCommand(new CMDInsanityCheck());
		}
		event.registerServerCommand(new AOTDCommands());
	}

	/**
	 * @return SimpleNetworkWrapper instance
	 */
	public PacketHandler getPacketHandler()
	{
		return this.packetHandler;
	}

	/**
	 * @return ScheduledExecutor instance
	 */
	public ScheduledExecutorService getDelayedExecutor()
	{
		return this.delayedExecutor;
	}
}
