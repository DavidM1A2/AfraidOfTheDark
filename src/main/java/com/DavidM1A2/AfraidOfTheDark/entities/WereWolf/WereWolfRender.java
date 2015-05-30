/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.entities.WereWolf;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

// Render the werewolf
public class WereWolfRender extends RenderLiving
{
	private static final ResourceLocation myTexture = new ResourceLocation("afraidofthedark:textures/entity/WereWolf.png");

	public WereWolfRender(final RenderManager renderManager, final ModelBase modelBase, final float size)
	{
		super(renderManager, modelBase, size);
	}

	@Override
	protected ResourceLocation getEntityTexture(final Entity entity)
	{
		return WereWolfRender.myTexture;
	}
}
