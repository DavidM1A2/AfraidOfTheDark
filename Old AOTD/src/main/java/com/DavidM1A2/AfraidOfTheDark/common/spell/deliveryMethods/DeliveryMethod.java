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
		compound.setInteger("id", this.getType().getID());
		compound.setBoolean("null", false);
	}

	public static DeliveryMethod create(NBTTagCompound compound)
	{
		if (!compound.getBoolean("null"))
		{
			if (compound.getString("type").equals("deliveryMethod"))
			{
				int id = compound.getInteger("id");
				for (DeliveryMethods deliveryMethod : DeliveryMethods.values())
					if (deliveryMethod.getID() == id)
					{
						DeliveryMethod toReturn = deliveryMethod.newInstance();
						toReturn.readFromNBT(compound);
						return toReturn;
					}
			}
		}
		return null;
	}
}
