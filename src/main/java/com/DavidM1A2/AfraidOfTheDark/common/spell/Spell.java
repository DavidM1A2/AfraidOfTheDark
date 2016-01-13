/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethod.DeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Effect;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSource.PowerSource;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class Spell implements Serializable
{
	private final String name;
	private final PowerSource powerSource;
	private final Entry<DeliveryMethod, List<Effect>>[] spellStages;
	private final UUID spellID;

	public Spell(String name, PowerSource powerSource, LinkedHashMap<DeliveryMethod, List<Effect>> spellStages, UUID spellID)
	{
		this.name = name;
		this.spellStages = new Entry[spellStages.entrySet().size()];
		spellStages.entrySet().toArray(this.spellStages);
		this.powerSource = powerSource;
		this.powerSource.setParentSpell(this);
		this.spellID = spellID;
	}

	public void instantiateSpell(EntityPlayer entityPlayer)
	{
		if (this.isSpellValid() && this.powerSource.canCastMySpell(entityPlayer))
		{
			this.powerSource.castSpell(entityPlayer);
			this.spellStages[0].getKey().fireDeliveryMethod(null, this, 0);
		}
		else
		{
			entityPlayer.addChatMessage(new ChatComponentText(this.powerSource.getNotEnoughPowerMsg()));
		}
	}

	public void spellStageCallback(DeliveryMethod previous, int spellStageIndex)
	{
		spellStageIndex = spellStageIndex + 1;
		this.spellStages[spellStageIndex].getKey().fireDeliveryMethod(previous, this, spellStageIndex);
	}

	public double getCost()
	{
		double cost = 0;
		for (Entry<DeliveryMethod, List<Effect>> spellStage : this.spellStages)
		{
			cost = cost + spellStage.getKey().getCost();
			for (Effect effect : spellStage.getValue())
			{
				cost = cost + effect.getCost();
			}
		}
		return cost;
	}

	private boolean isSpellValid()
	{
		boolean isValid = true;

		if (this.powerSource == null)
			isValid = false;
		if (this.spellStages.length == 0)
			isValid = false;
		for (Entry<DeliveryMethod, List<Effect>> spellStage : this.spellStages)
			if (spellStage.getKey() == null)
				isValid = false;

		return isValid;
	}

	public String getName()
	{
		return this.name;
	}

	public UUID getSpellUUID()
	{
		return this.spellID;
	}

	public Entry<DeliveryMethod, List<Effect>> getSpellStage(int index)
	{
		return this.spellStages[index];
	}
}
