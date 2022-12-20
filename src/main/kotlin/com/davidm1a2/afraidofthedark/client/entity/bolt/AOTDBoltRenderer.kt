package com.davidm1a2.afraidofthedark.client.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.util.ResourceLocation

class AOTDBoltRenderer<T : BoltEntity>(name: String, renderManager: EntityRendererManager) : BoltRenderer<T>(renderManager) {
    override val boltTexture: ResourceLocation = ResourceLocation(Constants.MOD_ID, "textures/entity/$name.png")
}