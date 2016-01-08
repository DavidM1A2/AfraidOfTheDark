/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;

import net.minecraft.entity.player.EntityPlayer;

public interface IPowerSource extends Serializable
{
	public abstract double getPowerLevel();

	public abstract void usePower(EntityPlayer entityPlayer, double spellCost);
}
