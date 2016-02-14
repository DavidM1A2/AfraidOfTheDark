/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.bolts;

import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIgneousBolt;

import net.minecraft.client.renderer.entity.RenderManager;

// A bolt renderer, for igneous bolts
public class IgneousBoltRender<T extends EntityIgneousBolt> extends BoltRender<T> {
	public IgneousBoltRender(final RenderManager renderManager) {
		super(renderManager, "afraidofthedark:textures/entity/igneousBolt.png");
	}
}
