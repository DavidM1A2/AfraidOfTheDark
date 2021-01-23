package com.davidm1a2.afraidofthedark.proxy

import com.davidm1a2.afraidofthedark.client.entity.bolt.*
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
import com.davidm1a2.afraidofthedark.common.entity.bolt.*
import com.davidm1a2.afraidofthedark.common.entity.enaria.EnariaEntity
import com.davidm1a2.afraidofthedark.common.entity.enaria.GhastlyEnariaEntity
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EnchantedSkeletonEntity
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.SpellProjectileEntity
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneEntity
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneProjectileEntity
import com.davidm1a2.afraidofthedark.common.entity.werewolf.WerewolfEntity
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import com.davidm1a2.afraidofthedark.common.tileEntity.VoidChestTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.EnariasAltarTileEntity
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.ReadBookScreen
import net.minecraft.client.gui.screen.ReadBookScreen.WrittenBookInfo
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.StringNBT
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry

/**
 * Proxy that is only to be instantiated on the CLIENT side
 *
 * @property researchOverlay Research overlay handler used to show when a player unlocks a research
 */
class ClientProxy : IProxy {
    override val researchOverlay = ResearchOverlayHandler()

    override fun registerHandlers() {
        MinecraftForge.EVENT_BUS.register(KeyInputEventHandler())
        MinecraftForge.EVENT_BUS.register(researchOverlay)
    }

    override fun initializeEntityRenderers() {
        // Register all of our renderers
        RenderingRegistry.registerEntityRenderingHandler(EnchantedSkeletonEntity::class.java) { EnchantedSkeletonRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(WerewolfEntity::class.java) { WerewolfRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(GhastlyEnariaEntity::class.java) { GhastlyEnariaRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(SplinterDroneEntity::class.java) { SplinterDroneRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(SplinterDroneProjectileEntity::class.java) { SplinterDroneProjectileRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(EnariaEntity::class.java) { EnariaRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(WoodenBoltEntity::class.java) { WoodenBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(IronBoltEntity::class.java) { IronBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(SilverBoltEntity::class.java) { SilverBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(IgneousBoltEntity::class.java) { IgneousBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(StarMetalBoltEntity::class.java) { StarMetalBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(SpellProjectileEntity::class.java) { SpellProjectileRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(EnchantedFrogEntity::class.java) { EnchantedFrogRenderer(it) }
    }

    override fun initializeTileEntityRenderers() {
        // Tell MC to render our special tile entities with the special renderer
        ClientRegistry.bindTileEntitySpecialRenderer(VoidChestTileEntity::class.java, TileEntityVoidChestRenderer())
        ClientRegistry.bindTileEntitySpecialRenderer(EnariasAltarTileEntity::class.java, TileEntityEnariasAltarRenderer())
    }

    override fun registerKeyBindings() {
        ModKeybindings.KEY_BINDING_LIST.forEach { ClientRegistry.registerKeyBinding(it) }
    }

    override fun showInsanitysHeightsBook() {
        // A hint book itemstack used purely to open the book GUI, it's never actually given to the player
        val hintBook = createHintBook()
        Minecraft.getInstance().displayGuiScreen(ReadBookScreen(WrittenBookInfo(hintBook)))
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
            pages.add(StringNBT(it))
        }
        return pages
    }
}