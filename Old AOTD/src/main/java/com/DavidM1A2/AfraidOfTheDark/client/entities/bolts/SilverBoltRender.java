/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.bolts;

import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntitySilverBolt;

import net.minecraft.client.renderer.entity.RenderManager;

// Setup the silver bolt renderer
public class SilverBoltRender<T extends EntitySilverBolt> extends BoltRender<T>
{
	public SilverBoltRender(final RenderManager renderManager)
	{
		super(renderManager, "afraidofthedark:textures/entity/silver_bolt.png");
	}
}
