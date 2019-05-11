package com.DavidM1A2.afraidofthedark.client.entity.bolt;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityStarMetalBolt;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Class used to render a star metal bolt
 */
public class RenderStarMetalBolt extends RenderBolt<EntityStarMetalBolt>
{
    /**
     * Constructor takes a render manager
     *
     * @param renderManager The manager given to us by Minecraft
     */
    public RenderStarMetalBolt(RenderManager renderManager)
    {
        super(renderManager, new ResourceLocation(Constants.MOD_ID, "textures/entity/star_metal_bolt.png"));
    }
}
