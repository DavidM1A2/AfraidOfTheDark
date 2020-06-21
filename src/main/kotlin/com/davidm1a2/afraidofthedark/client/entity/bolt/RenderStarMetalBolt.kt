package com.davidm1a2.afraidofthedark.client.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.bolt.EntityStarMetalBolt
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation

/**
 * Class used to render a star metal bolt
 *
 * @constructor Constructor takes a render manager
 * @param renderManager The manager given to us by Minecraft
 * @property boltTexture A resource location containing the bolt texture
 */
class RenderStarMetalBolt(renderManager: RenderManager) : RenderBolt<EntityStarMetalBolt>(renderManager) {
    override val boltTexture = ResourceLocation(Constants.MOD_ID, "textures/entity/star_metal_bolt.png")
}
