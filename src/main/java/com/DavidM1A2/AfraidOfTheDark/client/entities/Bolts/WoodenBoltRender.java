/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityBolt;

public class WoodenBoltRender extends BoltRender
{
	public WoodenBoltRender(final RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(final Entity entity)
	{
		return new ResourceLocation("afraidofthedark:textures/entity/woodenBolt.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityBolt entity)
	{
		return new ResourceLocation("afraidofthedark:textures/entity/woodenBolt.png");
	}
}
