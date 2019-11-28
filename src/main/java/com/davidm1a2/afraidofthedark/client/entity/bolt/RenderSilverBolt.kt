package com.davidm1a2.afraidofthedark.client.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.bolt.EntitySilverBolt
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation

/**
 * Class used to render a silver bolt
 *
 * @constructor takes a render manager
 * @param renderManager The manager given to us by Minecraft
 * @property boltTexture A resource location containing the bolt texture
 */
class RenderSilverBolt(renderManager: RenderManager) : RenderBolt<EntitySilverBolt>(renderManager)
{
    override val boltTexture: ResourceLocation
        get() = ResourceLocation(Constants.MOD_ID, "textures/entity/silver_bolt.png")
}
