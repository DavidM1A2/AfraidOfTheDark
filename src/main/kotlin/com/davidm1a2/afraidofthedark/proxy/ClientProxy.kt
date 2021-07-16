package com.davidm1a2.afraidofthedark.proxy

import com.davidm1a2.afraidofthedark.client.entity.bolt.IgneousBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.bolt.IronBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.bolt.SilverBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.bolt.StarMetalBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.bolt.WoodenBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.enaria.EnariaRenderer
import com.davidm1a2.afraidofthedark.client.entity.enaria.GhastlyEnariaRenderer
import com.davidm1a2.afraidofthedark.client.entity.enchantedFrog.EnchantedFrogRenderer
import com.davidm1a2.afraidofthedark.client.entity.enchantedSkeleton.EnchantedSkeletonRenderer
import com.davidm1a2.afraidofthedark.client.entity.spell.projectile.SpellProjectileRenderer
import com.davidm1a2.afraidofthedark.client.entity.splinterDrone.SplinterDroneProjectileRenderer
import com.davidm1a2.afraidofthedark.client.entity.splinterDrone.SplinterDroneRenderer
import com.davidm1a2.afraidofthedark.client.entity.werewolf.WerewolfRenderer
import com.davidm1a2.afraidofthedark.client.keybindings.KeyInputEventHandler
import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings
import com.davidm1a2.afraidofthedark.client.tileEntity.TileEntityVoidChestRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar.TileEntityEnariasAltarRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import com.davidm1a2.afraidofthedark.common.event.register.ModColorRegister
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.ReadBookScreen
import net.minecraft.client.gui.screen.ReadBookScreen.WrittenBookInfo
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.StringNBT
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

/**
 * Proxy that is only to be instantiated on the CLIENT side
 *
 * @property researchOverlay Research overlay handler used to show when a player unlocks a research
 */
class ClientProxy : IProxy {
    override val researchOverlay = ResearchOverlayHandler()

    override fun registerHandlers() {
        val forgeBus = MinecraftForge.EVENT_BUS
        val modBus = FMLJavaModLoadingContext.get().modEventBus

        forgeBus.register(KeyInputEventHandler())
        forgeBus.register(researchOverlay)

        modBus.register(ModColorRegister())
    }

    override fun initializeEntityRenderers() {
        // Register all of our renderers
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ENCHANTED_SKELETON) { EnchantedSkeletonRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.WEREWOLF) { WerewolfRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GHASTLY_ENARIA) { GhastlyEnariaRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SPLINTER_DRONE) { SplinterDroneRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SPLINTER_DRONE_PROJECTILE) { SplinterDroneProjectileRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ENARIA) { EnariaRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.WOODEN_BOLT) { WoodenBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.IRON_BOLT) { IronBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SILVER_BOLT) { SilverBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.IGNEOUS_BOLT) { IgneousBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.STAR_METAL_BOLT) { StarMetalBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SPELL_PROJECTILE) { SpellProjectileRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ENCHANTED_FROG) { EnchantedFrogRenderer(it) }
    }

    override fun initializeTileEntityRenderers() {
        // Tell MC to render our special tile entities with the special renderer
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.VOID_CHEST) { TileEntityVoidChestRenderer(it) }
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.ENARIAS_ALTAR) { TileEntityEnariasAltarRenderer(it) }
    }

    override fun registerKeyBindings() {
        ModKeybindings.KEY_BINDING_LIST.forEach { ClientRegistry.registerKeyBinding(it) }
    }

    override fun showInsanitysHeightsBook() {
        // A hint book itemstack used purely to open the book GUI, it's never actually given to the player
        val hintBook = createHintBook()
        Minecraft.getInstance().displayGuiScreen(ReadBookScreen(WrittenBookInfo(hintBook)))
    }

    override fun initializeBlockRenderTypes() {
        RenderTypeLookup.setRenderLayer(ModBlocks.AMORPHOUS_ELDRITCH_METAL, RenderType.getTranslucent())
        RenderTypeLookup.setRenderLayer(ModBlocks.VOID_CHEST_PORTAL, RenderType.getTranslucent())
        RenderTypeLookup.setRenderLayer(ModBlocks.IMBUED_CACTUS, RenderType.getCutout())
        RenderTypeLookup.setRenderLayer(ModBlocks.IMBUED_CACTUS_BLOSSOM, RenderType.getCutout())
    }

    private fun createHintBook(): ItemStack {
        val toReturn = ItemStack(Items.WRITTEN_BOOK, 1)
        NBTHelper.setString(toReturn, "title", I18n.format("nightmarebook.title"))
        NBTHelper.setString(toReturn, "author", I18n.format("nightmarebook.author"))
        NBTHelper.setBoolean(toReturn, "resolved", true)
        toReturn.tag!!.put("pages", createPages())
        return toReturn
    }

    private fun createPages(): ListNBT {
        val pages = ListNBT()
        val bookText = I18n.format("nightmarebook.text").split(";;")
        bookText.forEach {
            pages.add(StringNBT.valueOf(it))
        }
        return pages
    }
}