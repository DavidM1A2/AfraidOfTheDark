/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import net.minecraft.nbt.NBTTagCompound;

public abstract class Effect implements IEffect
{
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setString("type", "effect");
		compound.setInteger("id", this.getType().getID());
	}
}
