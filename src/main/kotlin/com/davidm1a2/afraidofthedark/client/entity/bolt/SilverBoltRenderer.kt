package com.davidm1a2.afraidofthedark.client.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.bolt.SilverBoltEntity
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.util.ResourceLocation

/**
 * Class used to render a silver bolt
 *
 * @constructor takes a render manager
 * @param renderManager The manager given to us by Minecraft
 * @property boltTexture A resource location containing the bolt texture
 */
class SilverBoltRenderer(renderManager: EntityRendererManager) : BoltRenderer<SilverBoltEntity>(renderManager) {
    override val boltTexture = ResourceLocation(Constants.MOD_ID, "textures/entity/silver_bolt.png")
}
