/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponent;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethods;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Effect;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Effects;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.PowerSource;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.PowerSources;

import net.minecraft.nbt.NBTTagCompound;

public class SpellUtility
{
	public static ISpellComponent createSpellComponentFromNBT(NBTTagCompound compound)
	{
		if (compound.getBoolean("null"))
			return null;
		String type = compound.getString("type");
		if (type.equals("deliveryMethod"))
			return createDeliveryMethodFromNBT(compound);
		else if (type.equals("effect"))
			return createEffectFromNBT(compound);
		else if (type.equals("powerSource"))
			return createPowerSourceFromNBT(compound);
		else
			return null;
	}

	private static DeliveryMethod createDeliveryMethodFromNBT(NBTTagCompound compound)
	{
		DeliveryMethod deliveryMethod = null;

		int id = compound.getInteger("id");

		for (DeliveryMethods enumDeliveryMethod : DeliveryMethods.values())
		{
			if (id == enumDeliveryMethod.getID())
			{
				deliveryMethod = enumDeliveryMethod.newInstance();
				deliveryMethod.readFromNBT(compound);
				break;
			}
		}

		return deliveryMethod;

	}

	private static Effect createEffectFromNBT(NBTTagCompound compound)
	{
		Effect effect = null;

		int id = compound.getInteger("id");

		for (Effects enumEffect : Effects.values())
		{
			if (id == enumEffect.getID())
			{
				effect = enumEffect.newInstance();
				effect.readFromNBT(compound);
				break;
			}
		}

		return effect;
	}

	private static PowerSource createPowerSourceFromNBT(NBTTagCompound compound)
	{
		PowerSource powerSource = null;

		int id = compound.getInteger("id");

		for (PowerSources enumPowerSource : PowerSources.values())
		{
			if (id == enumPowerSource.getID())
			{
				powerSource = enumPowerSource.newInstance();
				powerSource.readFromNBT(compound);
				break;
			}
		}

		return powerSource;
	}
}
