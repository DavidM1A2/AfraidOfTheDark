package com.davidm1a2.afraidofthedark.client.entity.bolt;

import com.davidm1a2.afraidofthedark.common.constants.Constants;
import com.davidm1a2.afraidofthedark.common.entity.bolt.EntitySilverBolt;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Class used to render a silver bolt
 */
public class RenderSilverBolt extends RenderBolt<EntitySilverBolt>
{
    /**
     * Constructor takes a render manager
     *
     * @param renderManager The manager given to us by Minecraft
     */
    public RenderSilverBolt(RenderManager renderManager)
    {
        super(renderManager, new ResourceLocation(Constants.MOD_ID, "textures/entity/silver_bolt.png"));
    }
}
