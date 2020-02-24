/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.proxy

import com.davidm1a2.afraidofthedark.client.entity.bolt.*
import com.davidm1a2.afraidofthedark.client.entity.enaria.RenderEnaria
import com.davidm1a2.afraidofthedark.client.entity.enaria.RenderGhastlyEnaria
import com.davidm1a2.afraidofthedark.client.entity.enchantedSkeleton.RenderEnchantedSkeleton
import com.davidm1a2.afraidofthedark.client.entity.spell.projectile.RenderSpellProjectile
import com.davidm1a2.afraidofthedark.client.entity.splinterDrone.RenderSplinterDrone
import com.davidm1a2.afraidofthedark.client.entity.splinterDrone.RenderSplinterDroneProjectile
import com.davidm1a2.afraidofthedark.client.entity.werewolf.RenderWerewolf
import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings.KEY_BINDING_LIST
import com.davidm1a2.afraidofthedark.client.tileEntity.voidChest.TileEntityVoidChestRenderer
import com.davidm1a2.afraidofthedark.common.entity.bolt.*
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityEnaria
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityGhastlyEnaria
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.EntitySpellProjectile
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDroneProjectile
import com.davidm1a2.afraidofthedark.common.entity.werewolf.EntityWerewolf
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityVoidChest
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreenBook
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
class ClientProxy : CommonProxy() {
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
    }

    /**
     * Called to initialize tile entity renderers
     */
    override fun initializeTileEntityRenderers() {
        // Tell MC to render our void chest tile entity with the special renderer
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVoidChest::class.java, TileEntityVoidChestRenderer)
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
        Minecraft.getMinecraft().displayGuiScreen(GuiScreenBook(entityPlayer, HINT_BOOK_PAGES, false))
    }

    companion object {
        // A hint book itemstack used purely to open the book GUI, it's never actually given to the player
        private val HINT_BOOK_PAGES = createHintBook()

        /**
         * Creates a hint book to be used purely for displaying the book GUI
         *
         * @return The itemstack representing the hint book
         */
        private fun createHintBook(): ItemStack {
            val toReturn = ItemStack(Items.WRITTEN_BOOK, 1, 0)
            NBTHelper.setString(toReturn, "title", "Insanity's Heights")
            NBTHelper.setString(toReturn, "author", "Foul Ole Ron")
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
            pages.appendTag(NBTTagString("To whomever finds this: don't stay here. This place is evil. I have been stuck here for longer than I can remember. I can hear the abyss calling to me. It beckons me to jump, calling my name. I've found all of the notes, but I cannot"))
            pages.appendTag(NBTTagString("leave with them. There are ten scrolls hidden here. Three are in the tallest tower, with two being near the top and one being near the bottom. The saw mill whispers such sweet things to be. The stone tower says that it has two"))
            pages.appendTag(NBTTagString("gifts for me. What pretty things they have, so many rings. Enaria's bones whisper to me from her grave. I'm sorry; we tried to save you! Her whispers make me want to hide inside of the log. The roof top rooms are hiding something"))
            pages.appendTag(NBTTagString("from me. They always stay quiet when I am near. I know they are keeping secrets from me! What has it told you? What has the monolith told you to make you stop talking to me? Answer me Enaria! Where have you gone? Have you left me?"))
            pages.appendTag(NBTTagString("You said we would be together forever!"))
            return pages
        }
    }
}