package com.davidm1a2.afraidofthedark.proxy

import com.davidm1a2.afraidofthedark.client.entity.bolt.*
import com.davidm1a2.afraidofthedark.client.entity.enaria.RenderEnaria
import com.davidm1a2.afraidofthedark.client.entity.enaria.RenderGhastlyEnaria
import com.davidm1a2.afraidofthedark.client.entity.enchantedFrog.RenderEnchantedFrog
import com.davidm1a2.afraidofthedark.client.entity.enchantedSkeleton.RenderEnchantedSkeleton
import com.davidm1a2.afraidofthedark.client.entity.spell.projectile.RenderSpellProjectile
import com.davidm1a2.afraidofthedark.client.entity.splinterDrone.RenderSplinterDrone
import com.davidm1a2.afraidofthedark.client.entity.splinterDrone.RenderSplinterDroneProjectile
import com.davidm1a2.afraidofthedark.client.entity.werewolf.RenderWerewolf
import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings.KEY_BINDING_LIST
import com.davidm1a2.afraidofthedark.client.tileEntity.spellAltar.TileEntitySpellAltarRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.voidChest.TileEntityVoidChestRenderer
import com.davidm1a2.afraidofthedark.common.entity.bolt.*
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityEnaria
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityGhastlyEnaria
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EntityEnchantedFrog
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.EntitySpellProjectile
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDroneProjectile
import com.davidm1a2.afraidofthedark.common.entity.werewolf.EntityWerewolf
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntitySpellAltar
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityVoidChest
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreenBook
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry

/**
 * Proxy that is only to be instantiated on the CLIENT side
 *
 * @property researchOverlay Research overlay handler used to show when a player unlocks a research
 */
class ClientProxy : IProxy {
    override val researchOverlay = ResearchOverlayHandler()

    /**
     * Called to initialize entity renderers
     */
    override fun initializeEntityRenderers() {
        // Register all of our renderers
        RenderingRegistry.registerEntityRenderingHandler(EntityEnchantedSkeleton::class.java) {
            RenderEnchantedSkeleton(
                it
            )
        }
        RenderingRegistry.registerEntityRenderingHandler(EntityWerewolf::class.java) { RenderWerewolf(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityGhastlyEnaria::class.java) { RenderGhastlyEnaria(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDrone::class.java) { RenderSplinterDrone(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDroneProjectile::class.java) {
            RenderSplinterDroneProjectile(
                it
            )
        }
        RenderingRegistry.registerEntityRenderingHandler(EntityEnaria::class.java) { RenderEnaria(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityWoodenBolt::class.java) { RenderWoodenBolt(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityIronBolt::class.java) { RenderIronBolt(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntitySilverBolt::class.java) { RenderSilverBolt(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityIgneousBolt::class.java) { RenderIgneousBolt(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityStarMetalBolt::class.java) { RenderStarMetalBolt(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile::class.java) { RenderSpellProjectile(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityEnchantedFrog::class.java) { RenderEnchantedFrog(it) }
    }

    /**
     * Called to initialize tile entity renderers
     */
    override fun initializeTileEntityRenderers() {
        // Tell MC to render our special tile entities with the special renderer
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVoidChest::class.java, TileEntityVoidChestRenderer)
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpellAltar::class.java, TileEntitySpellAltarRenderer)
    }

    /**
     * Called to register any key bindings
     */
    override fun registerKeyBindings() {
        for (keyBinding in KEY_BINDING_LIST) {
            ClientRegistry.registerKeyBinding(keyBinding)
        }
    }

    /**
     * Opens the "Insanity's Heights" book on the client side, does nothing server side
     *
     * @param entityPlayer The player that opened the book
     */
    override fun showInsanitysHeightsBook(entityPlayer: EntityPlayer) {
        // A hint book itemstack used purely to open the book GUI, it's never actually given to the player
        val hintBook = createHintBook()
        Minecraft.getMinecraft().displayGuiScreen(GuiScreenBook(entityPlayer, hintBook, false))
    }

    /**
     * Creates a hint book to be used purely for displaying the book GUI
     *
     * @return The itemstack representing the hint book
     */
    private fun createHintBook(): ItemStack {
        val toReturn = ItemStack(Items.WRITTEN_BOOK, 1, 0)
        NBTHelper.setString(toReturn, "title", I18n.format("nightmarebook.title"))
        NBTHelper.setString(toReturn, "author", I18n.format("nightmarebook.author"))
        NBTHelper.setBoolean(toReturn, "resolved", true)
        toReturn.tagCompound!!.setTag("pages", createPages())
        return toReturn
    }

    /**
     * Creates a tag list of strings representing pages in the insanity's heights book
     *
     * @return Creates a list of pages to be used by the book
     */
    private fun createPages(): NBTTagList {
        val pages = NBTTagList()
        val bookText = I18n.format("nightmarebook.text").split(";;")
        bookText.forEach {
            pages.appendTag(NBTTagString(it))
        }
        return pages
    }
}