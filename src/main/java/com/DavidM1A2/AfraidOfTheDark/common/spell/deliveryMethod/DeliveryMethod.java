/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethod;

import java.io.Serializable;

import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.util.BlockPos;

public abstract class DeliveryMethod implements Serializable
{
	public abstract double getCost();

	public abstract void fireDeliveryMethod(DeliveryMethod previous, Spell callback, int spellStageIndex, BlockPos previousDeliveryMethodLocation);
}
