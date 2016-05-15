/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.IDeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Effect;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;

import net.minecraft.nbt.NBTTagCompound;

public class SpellStage implements Serializable
{
	private IDeliveryMethod deliveryMethod;
	private List<IEffect> effects;

	public SpellStage(NBTTagCompound spellStageData)
	{
		this.readFromNBT(spellStageData);
	}

	public SpellStage(IDeliveryMethod deliveryMethod, List<IEffect> effects)
	{
		this.deliveryMethod = deliveryMethod;
		this.effects = effects;
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
				cost = cost + this.deliveryMethod.getStageMultiplier() * effect.getCost();
			}
			return cost;
		}
	}

	public IDeliveryMethod getDeliveryMethod()
	{
		return this.deliveryMethod;
	}

	public List<IEffect> getEffects()
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
		compound.setInteger("numberOfEffects", effects.size());
		for (int i = 0; i < this.effects.size(); i++)
		{
			NBTTagCompound effectData = new NBTTagCompound();
			this.effects.get(i).writeToNBT(effectData);
			compound.setTag("effect " + i, effectData);
		}
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		NBTTagCompound deliveryMethodData = compound.getCompoundTag("deliveryMethod");
		this.deliveryMethod = DeliveryMethod.create(deliveryMethodData);
		int numberOfEffects = compound.getInteger("numberOfEffects");
		this.effects = new ArrayList<IEffect>();
		for (int i = 0; i < numberOfEffects; i++)
		{
			NBTTagCompound effectData = compound.getCompoundTag("effect " + i);
			this.effects.add(Effect.create(effectData));
		}
	}
}
