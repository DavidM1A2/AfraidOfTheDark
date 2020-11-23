package com.davidm1a2.afraidofthedark

import com.davidm1a2.afraidofthedark.AfraidOfTheDark.Companion.packetHandler
import com.davidm1a2.afraidofthedark.common.command.AOTDCommands
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModConfigHolder
import com.davidm1a2.afraidofthedark.common.event.*
import com.davidm1a2.afraidofthedark.common.event.register.*
import com.davidm1a2.afraidofthedark.common.network.packets.packetHandler.PacketHandler
import com.davidm1a2.afraidofthedark.proxy.ClientProxy
import com.davidm1a2.afraidofthedark.proxy.IProxy
import com.davidm1a2.afraidofthedark.proxy.ServerProxy
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import java.util.function.Supplier

/**
 * Main class run when the mod is started up
 *
 * @property packetHandler Packet handler used to send and receive AOTD packets
 */
@Mod(Constants.MOD_ID)
class AfraidOfTheDark {
    init {
        val forgeBus = MinecraftForge.EVENT_BUS
        val modBus = FMLJavaModLoadingContext.get().modEventBus
        val context = ModLoadingContext.get()

        val dimensionRegister = DimensionRegister()

        forgeBus.register(dimensionRegister)
        forgeBus.register(CapabilityHandler())
        forgeBus.register(FlaskOfSoulsHandler())
        forgeBus.register(NightmareHandler())
        forgeBus.register(VoidChestHandler())
        forgeBus.register(SpellStateHandler())
        forgeBus.register(SpellFreezeHandler())
        forgeBus.register(SpellCharmHandler())
        forgeBus.register(ArmorHandler())
        forgeBus.register(ModColorRegister())
        forgeBus.register(FurnaceFuelRegister())
        forgeBus.register(this)

        modBus.register(RegistryRegister())
        modBus.register(BlockRegister())
        modBus.register(TileEntityRegister())
        modBus.register(ItemRegister())
        modBus.register(EffectRegister())
        modBus.register(EntityRegister())
        modBus.register(BiomeRegister())
        modBus.register(SoundRegister())
        modBus.register(RecipeSerializerRegister())
        modBus.register(SpellPowerSourceRegister())
        modBus.register(SpellDeliveryMethodRegister())
        modBus.register(SpellEffectRegister())
        modBus.register(ResearchRegister())
        modBus.register(BoltEntryRegister())
        modBus.register(MeteorEntryRegister())
        modBus.register(ParticleRegister())
        modBus.register(StructureRegister())
        modBus.register(dimensionRegister)
        modBus.register(ConfigurationHandler())
        modBus.register(this)

        context.registerConfig(ModConfig.Type.CLIENT, ModConfigHolder.CLIENT_SPEC)
        context.registerConfig(ModConfig.Type.COMMON, ModConfigHolder.COMMON_SPEC)

        proxy.initializeResearchOverlayHandler()
        proxy.initializeEntityRenderers()
        proxy.initializeTileEntityRenderers()
        proxy.registerKeyBindings()
    }

    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun commonSetupEvent(event: FMLCommonSetupEvent) {
        CapabilityRegister.register()
        PacketRegister.initialize()
        EntityRegister.registerSpawnPlacements()
        DataSerializerRegister.register()
    }

    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun loadCompleteEvent(event: FMLLoadCompleteEvent) {
        proxy.initializeParticleFactories()
    }

    /**
     * Called with the forge initialization event
     *
     * @param event Initialization event is responsible for renders and recipes
     */
    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
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