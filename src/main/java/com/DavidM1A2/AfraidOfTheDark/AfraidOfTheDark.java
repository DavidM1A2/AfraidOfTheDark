package com.DavidM1A2.afraidofthedark;

import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.DavidM1A2.afraidofthedark.client.keybindings.KeyInputEventHandler;
import com.DavidM1A2.afraidofthedark.common.command.AOTDCommands;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.event.*;
import com.DavidM1A2.afraidofthedark.common.event.register.*;
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
import net.minecraftforge.fml.relauncher.Side;
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
    // Packet handler used to send and receive AOTD packets
    private final PacketHandler packetHandler = new PacketHandler(Constants.MOD_ID);
    // World generator used to generate structures in the world
    private final AOTDWorldGenerator worldGenerator = new AOTDWorldGenerator();
    // Logger used to log any debug messages relating to AOTD
    private Logger logger;
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
        // Register our potion handler used to add all of our potions to the game
        MinecraftForge.EVENT_BUS.register(new PotionRegister());
        // Register our entity handler used to add all of our entities to the game
        MinecraftForge.EVENT_BUS.register(new EntityRegister());
        // Register our biome handler used to add all of our mod biomes to the game
        MinecraftForge.EVENT_BUS.register(new BiomeRegister());
        // Register our sound handler used to add all of our mod sounds to the game
        MinecraftForge.EVENT_BUS.register(new SoundRegister());
        // Register our recipe handler used to add all of our mod recipe shapes to the game
        MinecraftForge.EVENT_BUS.register(new RecipeRegister());
        // Register our structure handler used to add all of our mod structures to the game
        MinecraftForge.EVENT_BUS.register(new StructureRegister());
        // Register our research handler used to add all of our mod researches to the game
        MinecraftForge.EVENT_BUS.register(new ResearchRegister());
        // Register our bolt entry handler used to add all of our mod bolt entries to the game
        MinecraftForge.EVENT_BUS.register(new BoltEntryRegister());
        // Register our meteor entry handler used to add all of our mod meteor entries to the game
        MinecraftForge.EVENT_BUS.register(new MeteorEntryRegister());
        // Register our mod dimensions
        DimensionRegister.initialize();
        // Register our research overlay display to draw on the screen, only need to do this client side
        if (event.getSide() == Side.CLIENT)
        {
            MinecraftForge.EVENT_BUS.register(proxy.getResearchOverlay());
        }
        // Forward any capability events to our capability handler
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
        // Forward any chunk creation events to our world generation height mapper
        MinecraftForge.EVENT_BUS.register(new WorldHeightMapper());
        // Forward any chunk creation events to our world structure planner. Use our world RNG to create a seed
        MinecraftForge.EVENT_BUS.register(new WorldStructurePlanner());
        // Register mod furnace fuels
        MinecraftForge.EVENT_BUS.register(new FurnaceFuelRegister());
        // Register our flask of souls handler
        MinecraftForge.EVENT_BUS.register(new FlaskOfSoulsHandler());
        // Register our nightmare handler
        MinecraftForge.EVENT_BUS.register(new NightmareHandler());
        // Register our void chest handler
        MinecraftForge.EVENT_BUS.register(new VoidChestHandler());
        // Register our block color handler used to color aotd leaves
        MinecraftForge.EVENT_BUS.register(new ModColorRegister());
        // Register our AOTD world generator
        GameRegistry.registerWorldGenerator(worldGenerator, configurationHandler.getWorldGenPriority());
        // We also need to register our world gen server tick handler
        MinecraftForge.EVENT_BUS.register(worldGenerator);
        // Register our GUI handler that lets us open UIs for specific players
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new AOTDGuiHandler());
        // Register all AOTD packets
        proxy.registerPackets();
        // Initialize entity renderers (client side only)
        proxy.initializeEntityRenderers();
        // Initialize tile entity renderers (also client side)
        proxy.initializeTileEntityRenderers();
        // Register game key bindings
        proxy.registerKeyBindings();
    }

    /**
     * Called with the forge initialization event
     *
     * @param event Initialization event is responsible for renders and recipes
     */
    @Mod.EventHandler
    public void initialization(FMLInitializationEvent event)
    {
        // Initialize any ore-dictionary entries
        proxy.initializeOreDictionary();
        // Initialize furnace recipes
        FurnaceRecipeRegister.initialize();
        // Register our key input event handler client side
        if (event.getSide() == Side.CLIENT)
        {
            MinecraftForge.EVENT_BUS.register(new KeyInputEventHandler());
        }
    }

    /**
     * Called with the forge post-initialization event
     *
     * @param event Register the research achieved overlay on the client side only
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
        // Register mod commands
        event.registerServerCommand(new AOTDCommands());
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

    /**
     * @return The world generator used to generate AOTD structures
     */
    public AOTDWorldGenerator getWorldGenerator()
    {
        return worldGenerator;
    }
}
