/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

// Setup the IronBolt renderer
public class IronBoltRender extends BoltRender
{
	public IronBoltRender(final RenderManager p_i46179_1_)
	{
		super(p_i46179_1_);
	}

	public void doRender(final EntityIronBolt entity, final double d0, final double d1, final double d2, final float f, final float f1)
	{
		this.doRender(entity, d0, d1, d2, f, f1);
	}

	@Override
	protected ResourceLocation getEntityTexture(final Entity entity)
	{
		return new ResourceLocation("afraidofthedark:textures/entity/ironBolt.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityBolt entity)
	{
		return new ResourceLocation("afraidofthedark:textures/entity/ironBolt.png");
	}
}
