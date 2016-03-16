/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.IDeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;

public class SpellStage implements Serializable
{
	private IDeliveryMethod deliveryMethod;
	private ArrayList<IEffect> effects;

	public SpellStage(IDeliveryMethod deliveryMethod, ArrayList<IEffect> effects)
	{
		this.deliveryMethod = deliveryMethod;
		this.effects = effects;
	}

	public IDeliveryMethod getDeliveryMethod()
	{
		return this.deliveryMethod;
	}

	public List<IEffect> getEffects()
	{
		return this.effects;
	}
}
