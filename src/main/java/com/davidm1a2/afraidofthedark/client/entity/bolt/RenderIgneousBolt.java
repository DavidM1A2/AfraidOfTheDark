package com.davidm1a2.afraidofthedark.client.entity.bolt;

import com.davidm1a2.afraidofthedark.common.constants.Constants;
import com.davidm1a2.afraidofthedark.common.entity.bolt.EntityIgneousBolt;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Class used to render an igneous bolt
 */
public class RenderIgneousBolt extends RenderBolt<EntityIgneousBolt>
{
    /**
     * Constructor takes a render manager
     *
     * @param renderManager The manager given to us by Minecraft
     */
    public RenderIgneousBolt(RenderManager renderManager)
    {
        super(renderManager, new ResourceLocation(Constants.MOD_ID, "textures/entity/igneous_bolt.png"));
    }
}