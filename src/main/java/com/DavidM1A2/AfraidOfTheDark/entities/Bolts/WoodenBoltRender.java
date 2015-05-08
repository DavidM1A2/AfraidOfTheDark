/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.entities.Bolts;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class WoodenBoltRender extends BoltRender
{
	public WoodenBoltRender(RenderManager p_i46179_1_)
	{
		super(p_i46179_1_);
	}

	// Setup the wooden bolt renderer
	public void doRender(EntityIronBolt entity, double d0, double d1, double d2, float f, float f1)
	{
		this.doRender(entity, d0, d1, d2, f, f1);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return new ResourceLocation("afraidofthedark:textures/entity/woodenBolt.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBolt entity)
	{
		return new ResourceLocation("afraidofthedark:textures/entity/woodenBolt.png");
	}
}
