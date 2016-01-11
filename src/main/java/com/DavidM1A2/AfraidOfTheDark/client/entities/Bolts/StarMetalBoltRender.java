/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts;

import net.minecraft.client.renderer.entity.RenderManager;

//A bolt renderer, for star metal bolts
public class StarMetalBoltRender extends BoltRender
{
	public StarMetalBoltRender(final RenderManager renderManager)
	{
		super(renderManager, "afraidofthedark:textures/entity/starMetalBolt.png");
	}
}
