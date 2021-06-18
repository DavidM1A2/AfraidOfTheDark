package com.davidm1a2.afraidofthedark

import com.davidm1a2.afraidofthedark.AfraidOfTheDark.Companion.packetHandler
import com.davidm1a2.afraidofthedark.common.command.AOTDCommands
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModConfigHolder
import com.davidm1a2.afraidofthedark.common.event.ArmorHandler
import com.davidm1a2.afraidofthedark.common.event.CapabilityHandler
import com.davidm1a2.afraidofthedark.common.event.ConfigurationHandler
import com.davidm1a2.afraidofthedark.common.event.FlaskOfSoulsHandler
import com.davidm1a2.afraidofthedark.common.event.NightmareHandler
import com.davidm1a2.afraidofthedark.common.event.SpellCharmHandler
import com.davidm1a2.afraidofthedark.common.event.SpellFreezeHandler
import com.davidm1a2.afraidofthedark.common.event.SpellStateHandler
import com.davidm1a2.afraidofthedark.common.event.VoidChestHandler
import com.davidm1a2.afraidofthedark.common.event.register.BiomeRegister
import com.davidm1a2.afraidofthedark.common.event.register.BlockRegister
import com.davidm1a2.afraidofthedark.common.event.register.BoltEntryRegister
import com.davidm1a2.afraidofthedark.common.event.register.CapabilityRegister
import com.davidm1a2.afraidofthedark.common.event.register.DataSerializerRegister
import com.davidm1a2.afraidofthedark.common.event.register.DimensionRegister
import com.davidm1a2.afraidofthedark.common.event.register.EffectRegister
import com.davidm1a2.afraidofthedark.common.event.register.EntityRegister
import com.davidm1a2.afraidofthedark.common.event.register.FurnaceFuelRegister
import com.davidm1a2.afraidofthedark.common.event.register.ItemRegister
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
import com.davidm1a2.afraidofthedark.common.event.register.StructureRegister
import com.davidm1a2.afraidofthedark.common.event.register.TeleportQueue
import com.davidm1a2.afraidofthedark.common.event.register.TileEntityRegister
import com.davidm1a2.afraidofthedark.common.network.packets.packetHandler.PacketHandler
import com.davidm1a2.afraidofthedark.proxy.ClientProxy
import com.davidm1a2.afraidofthedark.proxy.IProxy
import com.davidm1a2.afraidofthedark.proxy.ServerProxy
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.lwjgl.opengl.GL11
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
        forgeBus.register(FurnaceFuelRegister())
        forgeBus.register(teleportQueue)
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
        modBus.register(ModColorRegister())
        modBus.register(this)

        context.registerConfig(ModConfig.Type.CLIENT, ModConfigHolder.CLIENT_SPEC)
        context.registerConfig(ModConfig.Type.COMMON, ModConfigHolder.COMMON_SPEC)

        proxy.registerHandlers()
        proxy.registerKeyBindings()
    }

    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun clientSetupEvent(event: FMLClientSetupEvent) {
        proxy.initializeEntityRenderers()
        proxy.initializeTileEntityRenderers()
    }

    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun commonSetupEvent(event: FMLCommonSetupEvent) {
        CapabilityRegister.register()
        PacketRegister.initialize()
        EntityRegister.registerSpawnPlacements()
        DataSerializerRegister.register()
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

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    fun test(event: RenderWorldLastEvent) {
        val cameraPos = Minecraft.getInstance().gameRenderer.activeRenderInfo.projectedView
        Minecraft.getInstance().textureManager.bindTexture(ResourceLocation(Constants.MOD_ID, "textures/block/eldritch_stone.png"))
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.buffer
        GlStateManager.pushMatrix()
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
        GlStateManager.disableCull()
        GlStateManager.translatef(-cameraPos.x.toFloat(), -cameraPos.y.toFloat() + 100, -cameraPos.z.toFloat())
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        bufferBuilder.pos(-0.5, 1.0, -0.5)
            .tex(0.0, 0.0)
            .endVertex()

        bufferBuilder.pos(0.5, 1.0, -0.5)
            .tex(1.0, 0.0)
            .endVertex()

        bufferBuilder.pos(0.5, 1.0, 0.5)
            .tex(1.0, 1.0)
            .endVertex()

        bufferBuilder.pos(-0.5, 1.0, 0.5)
            .tex(0.0, 1.0)
            .endVertex()

        tessellator.draw()
        GlStateManager.enableCull()
        GlStateManager.popMatrix()
    }

    companion object {
        val packetHandler = PacketHandler()
        val proxy: IProxy = DistExecutor.runForDist({ Supplier { ClientProxy() } }, { Supplier { ServerProxy() } })
        val teleportQueue = TeleportQueue()
    }
}