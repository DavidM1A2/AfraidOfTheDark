package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockBush
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

/**
 * Base class for any bush blocks used by Afraid of the dark
 *
 * @constructor requires a material parameter that defines some block properties
 * @param baseName The name of the block to be used by the game registry
 * @param material The material of this block
 * @param displayInCreative True if the block should show up in creative, false otherwise
 */
abstract class AOTDBush(baseName: String, material: Material, displayInCreative: Boolean = true) : BlockBush(material)
{
    init
    {
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")
        setHardness(0.0F)
        soundType = SoundType.PLANT
        if (displayInCreative)
        {
            setCreativeTab(Constants.AOTD_CREATIVE_TAB)
        }
    }
}