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
import com.davidm1a2.afraidofthedark.client.keybindings.KeyInputEventHandler
import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings.KEY_BINDING_LIST
import com.davidm1a2.afraidofthedark.client.particle.*
import com.davidm1a2.afraidofthedark.client.tileEntity.TileEntityVoidChestRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar.TileEntityEnariasAltarRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
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
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityVoidChest
import com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar.TileEntityEnariasAltar
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreenBook
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraft.util.EnumHand
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

    override fun initializeResearchOverlayHandler() {
        MinecraftForge.EVENT_BUS.register(researchOverlay)
    }

    override fun initializeEntityRenderers() {
        // Register all of our renderers
        RenderingRegistry.registerEntityRenderingHandler(EntityEnchantedSkeleton::class.java) { RenderEnchantedSkeleton(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityWerewolf::class.java) { RenderWerewolf(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityGhastlyEnaria::class.java) { RenderGhastlyEnaria(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDrone::class.java) { RenderSplinterDrone(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDroneProjectile::class.java) { RenderSplinterDroneProjectile(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityEnaria::class.java) { RenderEnaria(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityWoodenBolt::class.java) { RenderWoodenBolt(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityIronBolt::class.java) { RenderIronBolt(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntitySilverBolt::class.java) { RenderSilverBolt(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityIgneousBolt::class.java) { RenderIgneousBolt(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityStarMetalBolt::class.java) { RenderStarMetalBolt(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile::class.java) { RenderSpellProjectile(it) }
        RenderingRegistry.registerEntityRenderingHandler(EntityEnchantedFrog::class.java) { RenderEnchantedFrog(it) }
    }

    override fun initializeTileEntityRenderers() {
        // Tell MC to render our special tile entities with the special renderer
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVoidChest::class.java, TileEntityVoidChestRenderer())
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnariasAltar::class.java, TileEntityEnariasAltarRenderer())
    }

    override fun initializeParticleFactories() {
        val particleManager = Minecraft.getInstance().particles

        particleManager.registerFactory(ModParticles.ENARIA_BASIC_ATTACK) { _, worldIn, x, y, z, _, _, _ -> ParticleEnariaBasicAttack(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.ENARIAS_ALTAR) { _, worldIn, x, y, z, _, _, _ -> ParticleEnariasAltar(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.ENARIA_SPELL_CAST) { _, worldIn, x, y, z, _, _, _ -> ParticleEnariaSpellCast(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.ENARIA_SPELL_CAST_2) { _, worldIn, x, y, z, xSpeed, _, zSpeed ->
            ParticleEnariaSpellCast2(
                worldIn,
                x,
                y,
                z,
                xSpeed,
                zSpeed
            )
        }
        particleManager.registerFactory(ModParticles.ENARIA_TELEPORT) { _, worldIn, x, y, z, _, _, _ -> ParticleEnariaTeleport(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.ENCHANTED_FROG_SPAWN) { _, worldIn, x, y, z, xSpeed, _, zSpeed ->
            ParticleEnchantedFrogSpawn(
                worldIn,
                x,
                y,
                z,
                xSpeed,
                zSpeed
            )
        }
        particleManager.registerFactory(ModParticles.SMOKE_SCREEN) { _, worldIn, x, y, z, _, _, _ -> ParticleSmokeScreen(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.SPELL_CAST) { _, worldIn, x, y, z, _, _, _ -> ParticleSpellCast(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.SPELL_HIT) { _, worldIn, x, y, z, _, _, _ -> ParticleSpellHit(worldIn, x, y, z) }
        particleManager.registerFactory(ModParticles.SPELL_LASER) { _, worldIn, x, y, z, _, _, _ -> ParticleSpellLaser(worldIn, x, y, z) }
    }

    override fun registerKeyBindings() {
        MinecraftForge.EVENT_BUS.register(KeyInputEventHandler())
        KEY_BINDING_LIST.forEach { ClientRegistry.registerKeyBinding(it) }
    }

    override fun showInsanitysHeightsBook(entityPlayer: EntityPlayer) {
        // A hint book itemstack used purely to open the book GUI, it's never actually given to the player
        val hintBook = createHintBook()
        Minecraft.getInstance().displayGuiScreen(GuiScreenBook(entityPlayer, hintBook, false, EnumHand.MAIN_HAND))
    }

    private fun createHintBook(): ItemStack {
        val toReturn = ItemStack(Items.WRITTEN_BOOK, 1)
        NBTHelper.setString(toReturn, "title", I18n.format("nightmarebook.title"))
        NBTHelper.setString(toReturn, "author", I18n.format("nightmarebook.author"))
        NBTHelper.setBoolean(toReturn, "resolved", true)
        toReturn.tag!!.setTag("pages", createPages())
        return toReturn
    }

    private fun createPages(): NBTTagList {
        val pages = NBTTagList()
        val bookText = I18n.format("nightmarebook.text").split(";;")
        bookText.forEach {
            pages.add(NBTTagString(it))
        }
        return pages
    }
}