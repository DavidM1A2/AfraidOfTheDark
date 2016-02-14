/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.bolts;

import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityWoodenBolt;

import net.minecraft.client.renderer.entity.RenderManager;

//A bolt renderer, for wooden bolts
public class WoodenBoltRender<T extends EntityWoodenBolt> extends BoltRender<T> {
	public WoodenBoltRender(final RenderManager renderManager) {
		super(renderManager, "afraidofthedark:textures/entity/woodenBolt.png");
	}
}
