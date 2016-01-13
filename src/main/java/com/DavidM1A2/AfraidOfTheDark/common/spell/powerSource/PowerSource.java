/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSource;

import java.io.Serializable;

import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.entity.player.EntityPlayer;

public abstract class PowerSource implements Serializable
{
	private Spell spell;

	public void setParentSpell(Spell spell)
	{
		this.spell = spell;
	}

	public abstract boolean canCastMySpell(EntityPlayer entityPlayer);

	public abstract void castSpell(EntityPlayer entityPlayer);

	public abstract String getNotEnoughPowerMsg();
}
