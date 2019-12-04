package com.davidm1a2.afraidofthedark.common.constants

import net.minecraftforge.common.util.EnumHelper

/**
 * Class storing tool material constants. These are registered through the enum helper class and not through the standard forge system
 */
object ModToolMaterials
{
    val BLADE_OF_EXHUMATION = EnumHelper.addToolMaterial("blade_of_exhumation", 0, 150, 0f, 0f, 0)
    val ASTRAL_SILVER = EnumHelper.addToolMaterial("astral_silver", 2, 250, 1f, 3.0f, 20)
    val IGNEOUS = EnumHelper.addToolMaterial("igneous", 3, 600, 1f, 5f, 15)
    val STAR_METAL = EnumHelper.addToolMaterial("star_metal", 3, 600, 1f, 4f, 15)
}