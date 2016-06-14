/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;

import net.minecraft.nbt.NBTTagCompound;

public interface ISpellComponent extends Serializable
{
	/**
	 * 
	 * @return The enum constant representing this class
	 */
	public abstract ISpellComponentEnum getType();

	/**
	 * 
	 * @param compound
	 *            The compound to write the current power source's data to
	 */
	public abstract void writeToNBT(NBTTagCompound compound);

	/**
	 * 
	 * @param compound
	 *            The compound to read the current power source's data from
	 */
	public abstract void readFromNBT(NBTTagCompound compound);
}
