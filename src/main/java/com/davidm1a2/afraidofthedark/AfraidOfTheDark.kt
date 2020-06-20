package com.davidm1a2.afraidofthedark

import com.davidm1a2.afraidofthedark.AfraidOfTheDark.Companion.packetHandler
import com.davidm1a2.afraidofthedark.client.keybindings.KeyInputEventHandler
import com.davidm1a2.afraidofthedark.common.command.AOTDCommands
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModConfigHolder
import com.davidm1a2.afraidofthedark.common.event.*
import com.davidm1a2.afraidofthedark.common.event.register.*
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.PacketHandler
import com.davidm1a2.afraidofthedark.common.worldGeneration.AOTDWorldGenerator
import com.davidm1a2.afraidofthedark.common.worldGeneration.WorldHeightMapper
import com.davidm1a2.afraidofthedark.common.worldGeneration.WorldStructurePlanner
import com.davidm1a2.afraidofthedark.proxy.ClientProxy
import com.davidm1a2.afraidofthedark.proxy.IProxy
import com.davidm1a2.afraidofthedark.proxy.ServerProxy
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.thread.EffectiveSide
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import java.util.function.Supplier

/**
 * Main class run when the mod is started up
 *
 * @property packetHandler Packet handler used to send and receive AOTD packets
 * @property worldGenerator World generator used to generate structures in the world
 */
@Mod(Constants.MOD_ID)
class AfraidOfTheDark {
    init {
        val eventBus = FMLJavaModLoadingContext.get().modEventBus
        val context = ModLoadingContext.get()

        // If any changes in the config are detected, we trigger an event that we listen to here
        eventBus.register(ConfigurationHandler())
        // Register our block handler used to add all of our blocks to the game
        eventBus.register(BlockRegister())
        // Register our tile entity handler used to add all of our tile entities to the game
        eventBus.register(TileEntityRegister())
        // Register our item handler used to add all of our items to the game
        eventBus.register(ItemRegister())
        // Register our potion handler used to add all of our potions to the game
        eventBus.register(PotionRegister())
        // Register our entity handler used to add all of our entities to the game
        eventBus.register(EntityRegister())
        // Register our biome handler used to add all of our mod biomes to the game
        eventBus.register(BiomeRegister())
        // Register our sound handler used to add all of our mod sounds to the game
        eventBus.register(SoundRegister())
        // Register our sprite handler used to add all of our mod sprites for particles to the game
        eventBus.register(SpriteRegister())
        // Register our recipe handler used to add all of our mod recipe shapes to the game
        eventBus.register(RecipeRegister())
        // Register our structure handler used to add all of our mod structures to the game
        eventBus.register(StructureRegister())
        // Register our spell power source handler to add all of our mod spell power sources to the game
        eventBus.register(SpellPowerSourceRegister())
        // Register our spell delivery method handler to add all of our mod spell delivery methods to the game
        eventBus.register(SpellDeliveryMethodRegister())
        // Register our spell effect handler to add all of our mod spell effects to the game
        eventBus.register(SpellEffectRegister())
        // Register our research handler used to add all of our mod researches to the game
        eventBus.register(ResearchRegister())
        // Register our bolt entry handler used to add all of our mod bolt entries to the game
        eventBus.register(BoltEntryRegister())
        // Register our meteor entry handler used to add all of our mod meteor entries to the game
        eventBus.register(MeteorEntryRegister())
        // Register our mod dimensions
        val dimensionRegister = DimensionRegister()
        eventBus.register(dimensionRegister)
        MinecraftForge.EVENT_BUS.register(dimensionRegister)
        // Register our research overlay display to draw on the screen, only need to do this client side
        if (EffectiveSide.get() == LogicalSide.CLIENT) {
            eventBus.register(proxy.researchOverlay)
            eventBus.register(KeyInputEventHandler)
        }
        // Forward any capability events to our capability handler
        eventBus.register(CapabilityHandler())
        // Forward any chunk creation events to our world generation height mapper
        eventBus.register(WorldHeightMapper())
        // Forward any chunk creation events to our world structure planner. Use our world RNG to create a seed
        eventBus.register(WorldStructurePlanner())
        // Register mod furnace fuels
        eventBus.register(FurnaceFuelRegister())
        // Register our flask of souls handler
        eventBus.register(FlaskOfSoulsHandler())
        // Register our nightmare handler
        eventBus.register(NightmareHandler())
        // Register our void chest handler
        eventBus.register(VoidChestHandler())
        // Register our spell state handler
        eventBus.register(SpellStateHandler())
        // Register our spell freeze handler
        eventBus.register(SpellFreezeHandler())
        // Register our spell charm handler
        eventBus.register(SpellCharmHandler())
        // Register our block color handler used to color aotd leaves
        eventBus.register(ModColorRegister())
        // Register our AOTD world generator
        // GameRegistry.registerWorldGenerator(worldGenerator, configurationHandler.worldGenPriority)
        // We also need to register our world gen server tick handler
        eventBus.register(AOTDWorldGenerator())
        context.registerConfig(ModConfig.Type.CLIENT, ModConfigHolder.CLIENT_SPEC)
        context.registerConfig(ModConfig.Type.SERVER, ModConfigHolder.SERVER_SPEC)
        // context.registerExtensionPoint(ExtensionPoint.GUIFACTORY) { AOTDGuiHandler() }
        // Register all AOTD packets
        PacketRegister.initialize()
        eventBus.register(this)
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
    @SubscribeEvent
    fun initialization(event: FMLServerAboutToStartEvent) {
        // Initialize spell effect overrides
        SpellEffectOverrideRegister.initialize()
        // Only used by the developer to create .schematic.meta files
        // SchematicDebugUtils.createSchematicMetaFiles()
    }

    /**
     * Called when the server gets initialized
     *
     * @param event Register commands when the server starts
     */
    @SubscribeEvent
    fun serverStartingEvent(event: FMLServerStartingEvent) {
        // Register mod commands
        AOTDCommands.register(event.commandDispatcher)
    }

    companion object {
        val packetHandler = PacketHandler()
        val proxy: IProxy = DistExecutor.runForDist<IProxy>({ Supplier { ClientProxy() } }, { Supplier { ServerProxy() } })
    }
}