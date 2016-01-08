/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public class SpellStage implements Serializable
{
	private IDeliveryMethod deliveryMethod;
	private List<IEffect> effects = new ArrayList<IEffect>();

	public SpellStage(IDeliveryMethod deliveryMethod, List<IEffect> effects)
	{
		this.deliveryMethod = deliveryMethod;
		this.effects = effects;
	}

	public void fireStage(EntityPlayer entityPlayer, Spell spell)
	{

	}

	public void stageComplete()
	{

	}

	public double getCost()
	{
		double cost = 0;
		for (IEffect effect : this.effects)
			cost = cost + effect.getCost();
		cost = cost + deliveryMethod.getCost();
		return cost;
	}
}
