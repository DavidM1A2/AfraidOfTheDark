package com.DavidM1A2.afraidofthedark.client.entity.bolt;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityIronBolt;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Class used to render an iron bolt
 */
public class RenderIronBolt extends RenderBolt<EntityIronBolt>
{
    /**
     * Constructor takes a render manager
     *
     * @param renderManager The manager given to us by Minecraft
     */
    public RenderIronBolt(RenderManager renderManager)
    {
        super(renderManager, new ResourceLocation(Constants.MOD_ID, "textures/entity/iron_bolt.png"));
    }
}