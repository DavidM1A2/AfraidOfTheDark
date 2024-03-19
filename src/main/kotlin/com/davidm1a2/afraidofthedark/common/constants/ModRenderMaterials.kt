package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.client.resources.model.Material
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.InventoryMenu

object ModRenderMaterials {
    val IGNEOUS_SHIELD = Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation(Constants.MOD_ID, "entity/igneous_shield"))

    val ATLAS_TO_SPRITE = arrayOf(
        IGNEOUS_SHIELD
    ).groupBy { it.atlasLocation() }
}