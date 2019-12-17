package com.davidm1a2.afraidofthedark.client.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.bolt.EntityWoodenBolt
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation

/**
 * Class used to render a wooden bolt
 *
 * @constructor Constructor takes a render manager
 * @param renderManager The manager given to us by Minecraft
 * @property boltTexture A resource location containing the bolt texture
 */
class RenderWoodenBolt(renderManager: RenderManager) : RenderBolt<EntityWoodenBolt>(renderManager)
{
    override val boltTexture = ResourceLocation(Constants.MOD_ID, "textures/entity/wooden_bolt.png")
}
