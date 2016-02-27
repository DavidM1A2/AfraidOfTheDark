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
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.IDeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.IPowerSource;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class Spell implements Serializable
{
	private final String name;
	private final IPowerSource powerSource;
	private final Entry<IDeliveryMethod, List<IEffect>>[] spellStages;
	private final UUID spellID;
	private transient EntityPlayer spellOwner;

	public Spell(String name, IPowerSource powerSource, LinkedHashMap<IDeliveryMethod, List<IEffect>> spellStages, UUID spellID)
	{
		this.name = name;
		this.spellStages = new Entry[spellStages.entrySet().size()];
		spellStages.entrySet().toArray(this.spellStages);
		this.powerSource = powerSource;
		this.spellID = spellID;
	}

	public void instantiateSpell()
	{
		if (this.isSpellValid() && this.powerSource.attemptToCast(this))
			this.getSpellOwner().worldObj.spawnEntityInWorld(this.spellStages[0].getKey().createSpellEntity(this));
		else if (!this.isSpellValid())
			this.spellOwner.addChatMessage(new ChatComponentText("Invalid spell. Make sure to have delivery methods and a power source on your spell!"));
		else
			this.spellOwner.addChatMessage(new ChatComponentText(this.powerSource.notEnoughEnergyMsg()));
	}

	public double getCost()
	{
		double cost = 0;
		for (Entry<IDeliveryMethod, List<IEffect>> spellStage : this.spellStages)
		{
			cost = cost + spellStage.getKey().getCost();
			for (IEffect effect : spellStage.getValue())
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
		for (Entry<IDeliveryMethod, List<IEffect>> spellStage : this.spellStages)
			if (spellStage.getKey() == null)
				isValid = false;

		return isValid;
	}
	
	public Entry<IDeliveryMethod, List<IEffect>> getSpellStageByIndex(int index)
	{
		if (!hasSpellStage(index))
			return null;
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
