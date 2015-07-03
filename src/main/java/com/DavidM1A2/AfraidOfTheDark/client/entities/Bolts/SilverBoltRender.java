/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityIronBolt;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

// Setup the silver bolt renderer
public class SilverBoltRender extends BoltRender
{
	public SilverBoltRender(final RenderManager p_i46179_1_)
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
		return new ResourceLocation("afraidofthedark:textures/entity/silverBolt.png");
	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityBolt entity)
	{
		return new ResourceLocation("afraidofthedark:textures/entity/silverBolt.png");
	}
}
