/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.bolts;

import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIronBolt;

import net.minecraft.client.renderer.entity.RenderManager;

// Setup the IronBolt renderer
public class IronBoltRender<T extends EntityIronBolt> extends BoltRender<T>
{
	public IronBoltRender(final RenderManager renderManager)
	{
		super(renderManager, "afraidofthedark:textures/entity/iron_bolt.png");
	}
}
