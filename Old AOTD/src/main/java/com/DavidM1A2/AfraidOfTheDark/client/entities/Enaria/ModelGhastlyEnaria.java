/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.Enaria;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly.EntityGhastlyEnaria;

import net.minecraft.entity.Entity;

public class ModelGhastlyEnaria extends ModelEnaria
{
	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		EntityGhastlyEnaria entity = (EntityGhastlyEnaria) par1Entity;

		AnimationHandler.performAnimationInModel(parts, entity);

		//Render every non-child part
		body.render(par7);
	}
}
