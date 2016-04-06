/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import net.minecraft.nbt.NBTTagCompound;

public abstract class PowerSource implements IPowerSource
{
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setString("type", "powerSource");
		compound.setInteger("id", this.getType().getID());
	}
}
