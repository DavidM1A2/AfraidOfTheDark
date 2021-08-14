package com.davidm1a2.afraidofthedark

import com.davidm1a2.afraidofthedark.AfraidOfTheDark.Companion.packetHandler
import com.davidm1a2.afraidofthedark.client.keybindings.KeyInputEventHandler
import com.davidm1a2.afraidofthedark.common.command.AOTDCommands
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModConfigHolder
import com.davidm1a2.afraidofthedark.common.event.ArmorHandler
import com.davidm1a2.afraidofthedark.common.event.CapabilityHandler
import com.davidm1a2.afraidofthedark.common.event.ConfigurationHandler
import com.davidm1a2.afraidofthedark.common.event.FlaskOfSoulsHandler
import com.davidm1a2.afraidofthedark.common.event.NightmareHandler
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import com.davidm1a2.afraidofthedark.common.event.SpellCharmHandler
import com.davidm1a2.afraidofthedark.common.event.SpellFreezeHandler
import com.davidm1a2.afraidofthedark.common.event.SpellStateHandler
import com.davidm1a2.afraidofthedark.common.event.TeleportQueue
import com.davidm1a2.afraidofthedark.common.event.VoidChestHandler
import com.davidm1a2.afraidofthedark.common.event.register.BiomeRegister
import com.davidm1a2.afraidofthedark.common.event.register.BlockEntityRendererRegister
import com.davidm1a2.afraidofthedark.common.event.register.BlockRegister
import com.davidm1a2.afraidofthedark.common.event.register.BlockRenderTypeRegister
import com.davidm1a2.afraidofthedark.common.event.register.BoltEntryRegister
import com.davidm1a2.afraidofthedark.common.event.register.CapabilityRegister
import com.davidm1a2.afraidofthedark.common.event.register.ChunkGeneratorRegister
import com.davidm1a2.afraidofthedark.common.event.register.DataSerializerRegister
import com.davidm1a2.afraidofthedark.common.event.register.DimensionRenderInfoRegister
import com.davidm1a2.afraidofthedark.common.event.register.EffectRegister
import com.davidm1a2.afraidofthedark.common.event.register.EntityRegister
import com.davidm1a2.afraidofthedark.common.event.register.EntityRendererRegister
import com.davidm1a2.afraidofthedark.common.event.register.EntitySpawnPlacementRegister
import com.davidm1a2.afraidofthedark.common.event.register.FeatureRegister
import com.davidm1a2.afraidofthedark.common.event.register.FurnaceFuelRegister
import com.davidm1a2.afraidofthedark.common.event.register.ItemModelPropertyRegister
import com.davidm1a2.afraidofthedark.common.event.register.ItemRegister
import com.davidm1a2.afraidofthedark.common.event.register.KeybindingRegister
import com.davidm1a2.afraidofthedark.common.event.register.MeteorEntryRegister
import com.davidm1a2.afraidofthedark.common.event.register.ModColorRegister
import com.davidm1a2.afraidofthedark.common.event.register.PacketRegister
import com.davidm1a2.afraidofthedark.common.event.register.ParticleRegister
import com.davidm1a2.afraidofthedark.common.event.register.RecipeSerializerRegister
import com.davidm1a2.afraidofthedark.common.event.register.RegistryRegister
import com.davidm1a2.afraidofthedark.common.event.register.ResearchRegister
import com.davidm1a2.afraidofthedark.common.event.register.SoundRegister
import com.davidm1a2.afraidofthedark.common.event.register.SpellDeliveryMethodRegister
import com.davidm1a2.afraidofthedark.common.event.register.SpellEffectOverrideRegister
import com.davidm1a2.afraidofthedark.common.event.register.SpellEffectRegister
import com.davidm1a2.afraidofthedark.common.event.register.SpellPowerSourceRegister
import com.davidm1a2.afraidofthedark.common.event.register.StructureGenerationRegister
import com.davidm1a2.afraidofthedark.common.event.register.StructureRegister
import com.davidm1a2.afraidofthedark.common.event.register.TileEntityRegister
import com.davidm1a2.afraidofthedark.common.network.packets.packetHandler.PacketHandler
import net.minecraftforge.common.MinecraftForge
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

        forgeBus.register(CapabilityHandler())
        forgeBus.register(FlaskOfSoulsHandler())
        forgeBus.register(NightmareHandler())
        forgeBus.register(VoidChestHandler())
        forgeBus.register(SpellStateHandler())
        forgeBus.register(SpellFreezeHandler())
        forgeBus.register(SpellCharmHandler())
        forgeBus.register(ArmorHandler())
        forgeBus.register(FurnaceFuelRegister())
        forgeBus.register(teleportQueue)
        forgeBus.register(AOTDCommands())
        forgeBus.register(KeyInputEventHandler())
        forgeBus.register(researchOverlay)
        forgeBus.register(StructureGenerationRegister())

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
        modBus.register(BoltEntryRegister())
        modBus.register(MeteorEntryRegister())
        modBus.register(ParticleRegister())
        modBus.register(FeatureRegister())
        modBus.register(StructureRegister())
        modBus.register(ConfigurationHandler())
        modBus.register(ModColorRegister())
        modBus.register(BlockRenderTypeRegister())
        modBus.register(EntityRendererRegister())
        modBus.register(BlockEntityRendererRegister())
        modBus.register(DimensionRenderInfoRegister())
        modBus.register(ItemModelPropertyRegister())
        modBus.register(KeybindingRegister())
        modBus.register(CapabilityRegister())
        modBus.register(PacketRegister())
        modBus.register(EntitySpawnPlacementRegister())
        modBus.register(DataSerializerRegister())
        modBus.register(ChunkGeneratorRegister())
        modBus.register(SpellEffectOverrideRegister())
        modBus.register(BiomeRegister())

        context.registerConfig(ModConfig.Type.CLIENT, ModConfigHolder.CLIENT_SPEC)
        context.registerConfig(ModConfig.Type.COMMON, ModConfigHolder.COMMON_SPEC)
    }

    companion object {
        val packetHandler = PacketHandler()
        val teleportQueue = TeleportQueue()
        val researchOverlay = ResearchOverlayHandler()
    }
}