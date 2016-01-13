/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.bolts;

import net.minecraft.client.renderer.entity.RenderManager;

// Setup the IronBolt renderer
public class IronBoltRender extends BoltRender
{
	public IronBoltRender(final RenderManager renderManager)
	{
		super(renderManager, "afraidofthedark:textures/entity/ironBolt.png");
	}
}
