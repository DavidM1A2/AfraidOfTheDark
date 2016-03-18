/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.util.UUID;

import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.IPowerSource;
import com.DavidM1A2.AfraidOfTheDark.common.utility.SpellUtility;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

public class Spell
{
	private String name;
	private IPowerSource powerSource;
	private SpellStage[] spellStages;
	private UUID spellID;
	private EntityPlayer spellOwner;

	public Spell(NBTTagCompound spellData)
	{
		this.readFromNBT(spellData);
	}

	public Spell(String name, IPowerSource powerSource, SpellStage[] spellStages, UUID spellID)
	{
		this.name = name;
		this.spellStages = spellStages;
		this.powerSource = powerSource;
		this.spellID = spellID;
	}

	public void writeToNBT(NBTTagCompound spellCompound)
	{
		spellCompound.setString("spellName", this.name);
		NBTTagCompound powerSource = new NBTTagCompound();
		this.powerSource.writeToNBT(powerSource);
		spellCompound.setTag("spellPowerSource", powerSource);
		spellCompound.setInteger("numberOfSpellStages", this.spellStages.length);
		for (int i = 0; i < spellStages.length; i++)
		{
			NBTTagCompound spellStage = new NBTTagCompound();
			spellStages[i].writeToNBT(spellStage);
			spellCompound.setTag("spellStage " + i, spellStage);
		}
		spellCompound.setLong("UUIDMost", this.getSpellUUID().getMostSignificantBits());
		spellCompound.setLong("UUIDLeast", this.getSpellUUID().getLeastSignificantBits());
	}

	public void readFromNBT(NBTTagCompound spellCompound)
	{
		this.name = spellCompound.getString("spellName");
		NBTTagCompound powerSourceData = spellCompound.getCompoundTag("spellPowerSource");
		this.powerSource = (IPowerSource) SpellUtility.createSpellComponentFromNBT(powerSourceData);
		int numberOfSpellStages = spellCompound.getInteger("numberOfSpellStages");
		this.spellStages = new SpellStage[numberOfSpellStages];
		for (int i = 0; i < numberOfSpellStages; i++)
		{
			NBTTagCompound spellStageData = spellCompound.getCompoundTag("spellStage " + i);
			spellStages[i] = new SpellStage(spellStageData);
		}
		Long mostSignificantBits = spellCompound.getLong("UUIDMost");
		Long leastSignificantBits = spellCompound.getLong("UUIDLeast");
		this.spellID = new UUID(mostSignificantBits, leastSignificantBits);
	}

	public void instantiateSpell()
	{
		if (this.isSpellValid() && this.powerSource.attemptToCast(this))
			this.getSpellOwner().worldObj.spawnEntityInWorld(this.spellStages[0].getDeliveryMethod().createSpellEntity(this));
		else if (!this.isSpellValid())
			this.spellOwner.addChatMessage(new ChatComponentText("Invalid spell. Make sure to have delivery methods and a power source on your spell!"));
		else
			this.spellOwner.addChatMessage(new ChatComponentText(this.powerSource.notEnoughEnergyMsg()));
	}

	public double getCost()
	{
		double cost = 0;
		for (SpellStage spellStage : this.spellStages)
		{
			cost = cost + spellStage.getDeliveryMethod().getCost();
			for (IEffect effect : spellStage.getEffects())
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
		for (SpellStage spellStage : this.spellStages)
			if (spellStage.getDeliveryMethod() == null)
				isValid = false;

		return isValid;
	}

	public SpellStage getSpellStageByIndex(int index)
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
