/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.MathHelper;

public class GhastlyEnariaMoveHelper extends EntityMoveHelper
{
	private EntityGhastlyEnaria enaria;

	public GhastlyEnariaMoveHelper(EntityGhastlyEnaria enaria)
	{
		super(enaria);
		this.enaria = enaria;
	}

	@Override
	public void onUpdateMoveHelper()
	{
		if (this.update)
		{
			this.update = false;
			double d0 = this.posX - this.enaria.posX;
			double d1 = this.posY - this.enaria.posY;
			double d2 = this.posZ - this.enaria.posZ;
			double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);

			if (d3 != 0)
			{
				this.enaria.motionX += d0 / d3 * this.speed;
				this.enaria.motionY += d1 / d3 * this.speed;
				this.enaria.motionZ += d2 / d3 * this.speed;
			}
		}
	}
}