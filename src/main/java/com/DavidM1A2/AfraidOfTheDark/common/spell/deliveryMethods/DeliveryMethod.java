/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods;

import net.minecraft.nbt.NBTTagCompound;

public abstract class DeliveryMethod implements IDeliveryMethod
{
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setString("type", "deliveryMethod");
	}
}
