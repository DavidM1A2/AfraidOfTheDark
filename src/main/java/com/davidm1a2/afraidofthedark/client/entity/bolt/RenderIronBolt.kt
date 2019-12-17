package com.davidm1a2.afraidofthedark.client.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.bolt.EntityIronBolt
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation

/**
 * Class used to render an iron bolt
 *
 * @constructor takes a render manager
 * @param renderManager The manager given to us by Minecraft
 * @property boltTexture A resource location containing the bolt texture
 */
class RenderIronBolt(renderManager: RenderManager) : RenderBolt<EntityIronBolt>(renderManager)
{
    override val boltTexture = ResourceLocation(Constants.MOD_ID, "textures/entity/iron_bolt.png")
}