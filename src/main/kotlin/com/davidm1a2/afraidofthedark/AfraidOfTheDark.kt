package com.davidm1a2.afraidofthedark

import com.davidm1a2.afraidofthedark.AfraidOfTheDark.Companion.packetHandler
import com.davidm1a2.afraidofthedark.client.ClientProxy
import com.davidm1a2.afraidofthedark.client.keybindings.KeyInputEventHandler
import com.davidm1a2.afraidofthedark.common.IProxy
import com.davidm1a2.afraidofthedark.common.command.AOTDCommands
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModConfigHolder
import com.davidm1a2.afraidofthedark.common.event.*
import com.davidm1a2.afraidofthedark.common.event.register.*
import com.davidm1a2.afraidofthedark.common.network.handler.PacketHandler
import com.davidm1a2.afraidofthedark.server.ServerProxy
import com.davidm1a2.afraidofthedark.server.event.PlayerBiomeEventHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

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

        val researchTriggerHandler = ResearchTriggerHandler()
        forgeBus.register(CapabilityHandler())
        forgeBus.register(FlaskOfSoulsHandler())
        forgeBus.register(VitaeLanternHandler())
        forgeBus.register(NightmareHandler())
        forgeBus.register(VoidChestHandler())
        forgeBus.register(SpellStateHandler())
        forgeBus.register(SpellFreezeHandler())
        forgeBus.register(SpellCharmHandler())
        forgeBus.register(SpellLunarHandler())
        forgeBus.register(SpellSolarHandler())
        forgeBus.register(SpellThermalHandler())
        forgeBus.register(SpellInnateHandler())
        forgeBus.register(ArmorHandler())
        forgeBus.register(ShieldHandler())
        forgeBus.register(FurnaceFuelRegister())
        forgeBus.register(teleportQueue)
        forgeBus.register(AOTDCommands())
        forgeBus.register(KeyInputEventHandler())
        forgeBus.register(researchTriggerHandler)
        forgeBus.register(StructureGenerationRegister())
        forgeBus.register(PlayerBiomeEventHandler())
        forgeBus.register(SpellWardHandler())
        forgeBus.register(SpellPowerSourceHandler())
        forgeBus.register(EldritchItemHandler())

        modBus.register(RegistryRegister())
        modBus.register(BlockRegister())
        modBus.register(TileEntityRegister())
        modBus.register(ItemRegister())
        modBus.register(EffectRegister())
        modBus.register(EntityRegister())
        modBus.register(SoundRegister())
        modBus.register(RecipeSerializerRegister())
        modBus.register(SpellPowerSourceRegister())
        modBus.register(SpellDeliveryMethodRegister())
        modBus.register(SpellEffectRegister())
        modBus.register(ResearchRegister())
        modBus.register(MeteorEntryRegister())
        modBus.register(ParticleRegister())
        modBus.register(FeatureRegister())
        modBus.register(StructureRegister())
        modBus.register(ConfigurationHandler())
        modBus.register(CapabilityRegister())
        modBus.register(PacketRegister(proxy.researchOverlayHandler))
        modBus.register(EntitySpawnPlacementRegister())
        modBus.register(DataSerializerRegister())
        modBus.register(ChunkGeneratorRegister())
        modBus.register(BiomeRegister())
        modBus.register(ResearchTriggerRegister(researchTriggerHandler))
        modBus.register(LootConditionRegister())
        modBus.register(LootModifierSerializerRegister())

        context.registerConfig(ModConfig.Type.CLIENT, ModConfigHolder.CLIENT_SPEC)
        context.registerConfig(ModConfig.Type.COMMON, ModConfigHolder.COMMON_SPEC)

        proxy.registerSidedHandlers(forgeBus, modBus)
    }

    companion object {
        val packetHandler = PacketHandler()
        val teleportQueue = TeleportQueue()
        val proxy: IProxy = DistExecutor.unsafeRunForDist({ DistExecutor.SafeSupplier { ClientProxy() } },
            { DistExecutor.SafeSupplier { ServerProxy() } })
    }
}