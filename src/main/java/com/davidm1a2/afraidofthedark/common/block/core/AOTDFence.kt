package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockFence
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material


/**
 * Base class for any fence blocks used by Afraid of the dark
 *
 * @constructor requires a material parameter that defines some block properties
 * @param baseName The name of the block to be used by the game registry
 * @param material The material of this block
 * @param mapColor The color used to tint the fence's texture
 * @param displayInCreative True if the block should show up in creative, false otherwise
 */
abstract class AOTDFence(
    baseName: String,
    material: Material,
    mapColor: MapColor? = null,
    displayInCreative: Boolean = true
) : BlockFence(material, mapColor ?: material.materialMapColor) {
    init {
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")
        this.setHardness(2.0f)
        this.setResistance(5.0f)
        this.soundType = SoundType.WOOD
        if (displayInCreative) {
            setCreativeTab(Constants.AOTD_CREATIVE_TAB)
        }
    }
}