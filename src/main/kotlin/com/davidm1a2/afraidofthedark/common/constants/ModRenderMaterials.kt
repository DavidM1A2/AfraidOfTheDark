package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.client.renderer.model.RenderMaterial
import net.minecraft.inventory.container.PlayerContainer
import net.minecraft.util.ResourceLocation

object ModRenderMaterials {
    val IGNEOUS_SHIELD = RenderMaterial(PlayerContainer.BLOCK_ATLAS, ResourceLocation(Constants.MOD_ID, "entity/igneous_shield"))

    val ATLAS_TO_SPRITE = arrayOf(
        IGNEOUS_SHIELD
    ).groupBy { it.atlasLocation() }
}