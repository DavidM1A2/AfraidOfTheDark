package com.davidm1a2.afraidofthedark.common.potion.base

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.client.Minecraft
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import java.awt.Color

/**
 * Base class for all AOTD potions
 *
 * @constructor sets item properties
 * @param registryName The name of the potion in the game registry
 * @param indexX       The X index of the position on the texture sheet
 * @param indexY       The Y index of the position on the texture sheet
 * @param isBad        True if the potion is 'bad', false otherwise
 * @param color        The color of the potion
 */
open class AOTDPotion(registryName: String, indexX: Int, indexY: Int, isBad: Boolean, color: Color) :
    Potion(isBad, color.hashCode()) {
    init {
        this.setRegistryName("${Constants.MOD_ID}:$registryName")
        setIconIndex(indexX, indexY)
    }

    /**
     * Whenever MC wants to render a potion effect inject our custom textures and then render it
     *
     * @return The index of our custom potion texture sheet
     */
    @OnlyIn(Dist.CLIENT)
    override fun getStatusIconIndex(): Int {
        Minecraft.getInstance().textureManager.bindTexture(AOTD_POTION_TEXTURES)
        return super.getStatusIconIndex()
    }

    /**
     * True if the duration is greater than 0
     *
     * @param duration  The duration left on the drink
     * @param amplifier The potion amplifier
     * @return True if the potion is ready, false otherwise
     */
    override fun isReady(duration: Int, amplifier: Int): Boolean {
        return duration >= 1
    }

    companion object {
        // Due to the crappy MC design all potion textures are on one PNG, so here we load that sheet of textures in
        private val AOTD_POTION_TEXTURES =
            ResourceLocation(Constants.MOD_ID, "textures/potion_effect/potion_effects.png")
    }
}