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

import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class Spell implements Serializable
{
	private final String name;
	private final PowerSource powerSource;
	private final Entry<DeliveryMethod, List<Effect>>[] spellStages;
	private final UUID spellID;
	private transient EntityPlayer spellOwner;

	public Spell(String name, PowerSource powerSource, LinkedHashMap<DeliveryMethod, List<Effect>> spellStages, UUID spellID)
	{
		this.name = name;
		this.spellStages = new Entry[spellStages.entrySet().size()];
		spellStages.entrySet().toArray(this.spellStages);
		this.powerSource = powerSource;
		this.spellID = spellID;
	}

	public void instantiateSpell()
	{
		if (this.isSpellValid() && this.tryToCastSpell())
			SpellEntityCreator.createAndSpawn(this.spellOwner.worldObj, null, this, 0);
		else if (!this.isSpellValid())
			this.spellOwner.addChatMessage(new ChatComponentText("Invalid spell. Make sure to have delivery methods and a power source on your spell!"));
		else
			this.spellOwner.addChatMessage(new ChatComponentText("Not enough power to cast spell"));
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
	
	private boolean tryToCastSpell()
	{
		switch (this.powerSource) 
		{
			case Self:
			{
				return true;
			}
		}
		return true;
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
	
	public Entry<DeliveryMethod, List<Effect>> getSpellStageByIndex(int index)
	{
		return this.spellStages[index];
	}
	
	public boolean hasSpellStage(int index)
	{
		return Utility.hasIndex(this.spellStages, index);
	}

	public void setSpellOwner(EntityPlayer spellOwner)
	{
		this.spellOwner = spellOwner;
	}

	public EntityPlayer getSpellOwner()
	{
		return this.spellOwner;
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
