package com.DavidM1A2.afraidofthedark.client.entity.bolt;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityWoodenBolt;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Class used to render a wooden bolt
 */
public class RenderWoodenBolt extends RenderBolt<EntityWoodenBolt>
{
	/**
	 * Constructor takes a render manager
	 *
	 * @param renderManager The manager given to us by Minecraft
	 */
	public RenderWoodenBolt(RenderManager renderManager)
	{
		super(renderManager, new ResourceLocation(Constants.MOD_ID, "textures/entity/wooden_bolt.png"));
	}
}
