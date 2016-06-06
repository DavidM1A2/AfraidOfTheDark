/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;

import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.IDeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Effect;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;

import net.minecraft.nbt.NBTTagCompound;

public class SpellStage implements Serializable
{
	private IDeliveryMethod deliveryMethod = null;
	private IEffect[] effects = new IEffect[4];

	public SpellStage(NBTTagCompound spellStageData)
	{
		this.readFromNBT(spellStageData);
	}

	public SpellStage()
	{
	}

	public double getCost()
	{
		if (deliveryMethod == null)
			return 0;
		else
		{
			double cost = 0;
			cost = cost + deliveryMethod.getCost();
			for (IEffect effect : this.effects)
			{
				if (effect != null)
					cost = cost + this.deliveryMethod.getStageMultiplier() * effect.getCost();
			}
			return cost;
		}
	}

	public IDeliveryMethod getDeliveryMethod()
	{
		return this.deliveryMethod;
	}

	public void setDeliveryMethod(IDeliveryMethod deliveryMethod)
	{
		this.deliveryMethod = deliveryMethod;
	}

	public IEffect[] getEffects()
	{
		return this.effects;
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound deliveryMethodData = new NBTTagCompound();
		if (this.deliveryMethod != null)
			this.deliveryMethod.writeToNBT(deliveryMethodData);
		else
			deliveryMethodData.setBoolean("null", true);

		compound.setTag("deliveryMethod", deliveryMethodData);
		for (int i = 0; i < this.effects.length; i++)
		{
			NBTTagCompound effectData = new NBTTagCompound();
			if (this.effects[i] != null)
				this.effects[i].writeToNBT(effectData);
			compound.setTag("effect " + i, effectData);
		}
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		NBTTagCompound deliveryMethodData = compound.getCompoundTag("deliveryMethod");
		this.deliveryMethod = DeliveryMethod.create(deliveryMethodData);
		for (int i = 0; i < this.effects.length; i++)
		{
			NBTTagCompound effectData = compound.getCompoundTag("effect " + i);
			this.effects[i] = Effect.create(effectData);
		}
	}
}
