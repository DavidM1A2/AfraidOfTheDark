package com.davidm1a2.afraidofthedark.client.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation

class AOTDBoltRenderer<T : BoltEntity>(name: String, renderContext: EntityRendererProvider.Context) : BoltRenderer<T>(renderContext) {
    override val boltTexture: ResourceLocation = ResourceLocation(Constants.MOD_ID, "textures/entity/$name.png")
}