/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethod;

import java.io.Serializable;

import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.entity.player.EntityPlayer;

public abstract class DeliveryMethod implements Serializable
{
	public abstract double getCost();

	public abstract void fireDeliveryMethod(DeliveryMethod previous, Spell callback, int spellStageIndex);

	public abstract void fireDeliveryMethod(EntityPlayer source, Spell callback, int spellStageIndex);
}
