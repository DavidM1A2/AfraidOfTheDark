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
		compound.setBoolean("null", false);
	}

	public static Effect create(NBTTagCompound compound)
	{
		if (!compound.getBoolean("null"))
		{
			if (compound.getString("type").equals("effect"))
			{
				int id = compound.getInteger("id");
				for (Effects effect : Effects.values())
					if (effect.getID() == id)
					{
						Effect toReturn = effect.newInstance();
						toReturn.readFromNBT(compound);
						return toReturn;
					}
			}
		}
		return null;
	}
}
