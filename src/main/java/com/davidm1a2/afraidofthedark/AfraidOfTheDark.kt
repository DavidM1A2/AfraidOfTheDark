package com.davidm1a2.afraidofthedark

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler
import com.davidm1a2.afraidofthedark.client.keybindings.KeyInputEventHandler
import com.davidm1a2.afraidofthedark.common.command.AOTDCommands
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.event.*
import com.davidm1a2.afraidofthedark.common.event.register.*
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.PacketHandler
import com.davidm1a2.afraidofthedark.common.worldGeneration.AOTDWorldGenerator
import com.davidm1a2.afraidofthedark.common.worldGeneration.WorldHeightMapper
import com.davidm1a2.afraidofthedark.common.worldGeneration.WorldStructurePlanner
import com.davidm1a2.afraidofthedark.proxy.IProxy
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.Logger

/**
 * Main class run when the mod is started up
 *
 * @property packetHandler Packet handler used to send and receive AOTD packets
 * @property worldGenerator World generator used to generate structures in the world
 * @property logger Logger used to log any debug messages relating to AOTD
 * @property configurationHandler Configuration handler used to read and update the afraidofthedark.cfg file
 */
@Mod(
    modid = Constants.MOD_ID,
    name = Constants.MOD_NAME,
    version = Constants.MOD_VERSION,
    guiFactory = Constants.GUI_FACTORY_CLASS,
    acceptedMinecraftVersions = Constants.MC_VERSION
)
class AfraidOfTheDark
{
    val packetHandler = PacketHandler(Constants.MOD_ID)
    val worldGenerator = AOTDWorldGenerator()
    lateinit var logger: Logger
        private set
    lateinit var configurationHandler: ConfigurationHandler
        private set

    /**
     * Called with the forge pre-initialization event
     *
     * @param event Pre-init used to register events and various other things (see class names for what each line does)
     */
    @Mod.EventHandler
    fun preInitialization(event: FMLPreInitializationEvent)
    {
        // Grab a reference to the AOTD logger to debug with
        logger = event.modLog
        // We initialize the configuration handler from the suggested file
        configurationHandler = ConfigurationHandler(event.suggestedConfigurationFile)
        // If any changes in the config are detected, we trigger an event that we listen to here
        MinecraftForge.EVENT_BUS.register(configurationHandler)
        // Register our block handler used to add all of our blocks to the game
        MinecraftForge.EVENT_BUS.register(BlockRegister())
        // Register our item handler used to add all of our items to the game
        MinecraftForge.EVENT_BUS.register(ItemRegister())
        // Register our potion handler used to add all of our potions to the game
        MinecraftForge.EVENT_BUS.register(PotionRegister())
        // Register our entity handler used to add all of our entities to the game
        MinecraftForge.EVENT_BUS.register(EntityRegister())
        // Register our biome handler used to add all of our mod biomes to the game
        MinecraftForge.EVENT_BUS.register(BiomeRegister())
        // Register our sound handler used to add all of our mod sounds to the game
        MinecraftForge.EVENT_BUS.register(SoundRegister())
        // Register our sprite handler used to add all of our mod sprites for particles to the game
        MinecraftForge.EVENT_BUS.register(SpriteRegister())
        // Register our recipe handler used to add all of our mod recipe shapes to the game
        MinecraftForge.EVENT_BUS.register(RecipeRegister())
        // Register our structure handler used to add all of our mod structures to the game
        MinecraftForge.EVENT_BUS.register(StructureRegister())
        // Register our spell power source handler to add all of our mod spell power sources to the game
        MinecraftForge.EVENT_BUS.register(SpellPowerSourceRegister())
        // Register our spell delivery method handler to add all of our mod spell delivery methods to the game
        MinecraftForge.EVENT_BUS.register(SpellDeliveryMethodRegister())
        // Register our spell effect handler to add all of our mod spell effects to the game
        MinecraftForge.EVENT_BUS.register(SpellEffectRegister())
        // Register our research handler used to add all of our mod researches to the game
        MinecraftForge.EVENT_BUS.register(ResearchRegister())
        // Register our bolt entry handler used to add all of our mod bolt entries to the game
        MinecraftForge.EVENT_BUS.register(BoltEntryRegister())
        // Register our meteor entry handler used to add all of our mod meteor entries to the game
        MinecraftForge.EVENT_BUS.register(MeteorEntryRegister())
        // Register our mod dimensions
        DimensionRegister.initialize()
        // Register our research overlay display to draw on the screen, only need to do this client side
        if (event.side == Side.CLIENT)
        {
            MinecraftForge.EVENT_BUS.register(proxy.researchOverlay)
        }
        // Forward any capability events to our capability handler
        MinecraftForge.EVENT_BUS.register(CapabilityHandler())
        // Forward any chunk creation events to our world generation height mapper
        MinecraftForge.EVENT_BUS.register(WorldHeightMapper())
        // Forward any chunk creation events to our world structure planner. Use our world RNG to create a seed
        MinecraftForge.EVENT_BUS.register(WorldStructurePlanner())
        // Register mod furnace fuels
        MinecraftForge.EVENT_BUS.register(FurnaceFuelRegister())
        // Register our flask of souls handler
        MinecraftForge.EVENT_BUS.register(FlaskOfSoulsHandler())
        // Register our nightmare handler
        MinecraftForge.EVENT_BUS.register(NightmareHandler())
        // Register our void chest handler
        MinecraftForge.EVENT_BUS.register(VoidChestHandler())
        // Register our spell state handler
        MinecraftForge.EVENT_BUS.register(SpellStateHandler())
        // Register our spell freeze handler
        MinecraftForge.EVENT_BUS.register(SpellFreezeHandler())
        // Register our spell charm handler
        MinecraftForge.EVENT_BUS.register(SpellCharmHandler())
        // Register our block color handler used to color aotd leaves
        MinecraftForge.EVENT_BUS.register(ModColorRegister())
        // Register our AOTD world generator
        GameRegistry.registerWorldGenerator(worldGenerator, configurationHandler.worldGenPriority)
        // We also need to register our world gen server tick handler
        MinecraftForge.EVENT_BUS.register(worldGenerator)
        // Register our GUI handler that lets us open UIs for specific players
        NetworkRegistry.INSTANCE.registerGuiHandler(this, AOTDGuiHandler())
        // Register all AOTD packets
        proxy.registerPackets()
        // Initialize entity renderers (client side only)
        proxy.initializeEntityRenderers()
        // Initialize tile entity renderers (also client side)
        proxy.initializeTileEntityRenderers()
        // Register game key bindings
        proxy.registerKeyBindings()
    }

    /**
     * Called with the forge initialization event
     *
     * @param event Initialization event is responsible for renders and recipes
     */
    @Mod.EventHandler
    fun initialization(event: FMLInitializationEvent)
    {
        // Initialize any ore-dictionary entries
        proxy.initializeOreDictionary()
        // Initialize furnace recipes
        FurnaceRecipeRegister.initialize()
        // Initialize spell effect overrides
        SpellEffectOverrideRegister.initialize()
        // Register our key input event handler client side
        if (event.side == Side.CLIENT)
        {
            MinecraftForge.EVENT_BUS.register(KeyInputEventHandler)
        }
        // Only used by the developer to create .schematic.meta files
        // SchematicDebugUtils.createSchematicMetaFiles();
    }

    /**
     * Called with the forge post-initialization event
     *
     * @param event Register the research achieved overlay on the client side only
     */
    @Mod.EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun postInitialization(event: FMLPostInitializationEvent)
    {
    }

    /**
     * Called when the server gets initialized
     *
     * @param event Register commands when the server starts
     */
    @Mod.EventHandler
    fun serverStartingEvent(event: FMLServerStartingEvent)
    {
        // Register mod commands
        event.registerServerCommand(AOTDCommands())
    }

    companion object
    {
        // Singleton design pattern used here
        @JvmStatic
        @Mod.Instance(Constants.MOD_ID)
        lateinit var INSTANCE: AfraidOfTheDark

        // Sided proxy initialized distinguish client & server side
        @JvmStatic
        @SidedProxy(clientSide = Constants.CLIENT_PROXY_CLASS, serverSide = Constants.SERVER_PROXY_CLASS)
        lateinit var proxy: IProxy
    }
}