/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.bolts;

import net.minecraft.client.renderer.entity.RenderManager;

// Setup the silver bolt renderer
public class SilverBoltRender extends BoltRender
{
	public SilverBoltRender(final RenderManager renderManager)
	{
		super(renderManager, "afraidofthedark:textures/entity/silverBolt.png");
	}
}
