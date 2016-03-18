/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.IDeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;
import com.DavidM1A2.AfraidOfTheDark.common.utility.SpellUtility;

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
		this.deliveryMethod.writeToNBT(deliveryMethodData);
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
		this.deliveryMethod = (IDeliveryMethod) SpellUtility.createSpellComponentFromNBT(deliveryMethodData);
		int numberOfEffects = compound.getInteger("numberOfEffects");
		this.effects = new ArrayList<IEffect>();
		for (int i = 0; i < numberOfEffects; i++)
		{
			NBTTagCompound effectData = compound.getCompoundTag("effect " + i);
			this.effects.add((IEffect) SpellUtility.createSpellComponentFromNBT(effectData));
		}
	}
}
