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
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

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
			this.spellStages[0].getKey().fireDeliveryMethod(entityPlayer, this, 0);
		}
		else if (!this.isSpellValid())
		{
			entityPlayer.addChatMessage(new ChatComponentText("Invalid spell. Make sure to have delivery methods and a power source on your spell!"));
		}
		else
		{
			entityPlayer.addChatMessage(new ChatComponentText(this.powerSource.getNotEnoughPowerMsg()));
		}
	}

	public void spellStageCallback(int spellStageIndex)
	{
		DeliveryMethod previous = this.spellStages[spellStageIndex].getKey();
		spellStageIndex = spellStageIndex + 1;
		if (this.spellStages.length == spellStageIndex)
		{
			LogHelper.info("Spell over");
			return;
		}
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
}
