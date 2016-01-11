/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts;

import net.minecraft.client.renderer.entity.RenderManager;

// A bolt renderer, for igneous bolts
public class IgneousBoltRender extends BoltRender
{
	public IgneousBoltRender(final RenderManager renderManager)
	{
		super(renderManager, "afraidofthedark:textures/entity/igneousBolt.png");
	}
}
