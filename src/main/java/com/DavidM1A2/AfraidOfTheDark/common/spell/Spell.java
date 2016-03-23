/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;
import java.util.UUID;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.IPowerSource;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.SpellUtility;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

public class Spell implements Serializable
{
	private String name;
	private IPowerSource powerSource;
	private SpellStage[] spellStages;
	private UUID spellID;
	private UUID spellOwner;

	public Spell(NBTTagCompound spellData)
	{
		this.readFromNBT(spellData);
	}

	public Spell(EntityPlayer owner, String name, IPowerSource powerSource, SpellStage[] spellStages, UUID spellID)
	{
		this.name = name;
		this.spellStages = spellStages;
		this.powerSource = powerSource;
		this.spellID = spellID;
		this.spellOwner = owner.getPersistentID();
	}

	public void writeToNBT(NBTTagCompound spellCompound)
	{
		spellCompound.setString("spellName", this.name);
		NBTTagCompound powerSourceData = new NBTTagCompound();
		if (this.powerSource != null)
		{
			this.powerSource.writeToNBT(powerSourceData);
			powerSourceData.setBoolean("null", false);
		}
		else
			powerSourceData.setBoolean("null", true);
		spellCompound.setTag("spellPowerSource", powerSourceData);
		spellCompound.setInteger("numberOfSpellStages", this.spellStages.length);
		for (int i = 0; i < spellStages.length; i++)
		{
			NBTTagCompound spellStage = new NBTTagCompound();
			spellStages[i].writeToNBT(spellStage);
			spellCompound.setTag("spellStage " + i, spellStage);
		}
		spellCompound.setLong("UUIDMost", this.getSpellUUID().getMostSignificantBits());
		spellCompound.setLong("UUIDLeast", this.getSpellUUID().getLeastSignificantBits());
		spellCompound.setLong("UUIDMostPlayer", this.getSpellOwner().getMostSignificantBits());
		spellCompound.setLong("UUIDLeastPlayer", this.getSpellOwner().getLeastSignificantBits());
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
		Long mostSignificantBitsPlayer = spellCompound.getLong("UUIDMostPlayer");
		Long leastSignificantBitsPlayer = spellCompound.getLong("UUIDLeastPlayer");
		this.spellOwner = new UUID(mostSignificantBitsPlayer, leastSignificantBitsPlayer);
	}

	public void instantiateSpell()
	{
		EntityPlayer entityPlayer = AfraidOfTheDark.proxy.getSpellOwner(this);
		if (entityPlayer != null)
		{
			if (this.isSpellValid() && this.powerSource.attemptToCast(this))
				entityPlayer.worldObj.spawnEntityInWorld(this.spellStages[0].getDeliveryMethod().createSpellEntity(this));
			else if (!this.isSpellValid())
				entityPlayer.addChatMessage(new ChatComponentText("Invalid spell. Make sure to have delivery methods and a power source on your spell!"));
			else
				entityPlayer.addChatMessage(new ChatComponentText(this.powerSource.notEnoughEnergyMsg()));
		}
		else
			LogHelper.info("Attempted to instantiate a spell on an offline player...");
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
		this.spellOwner = spellOwner.getPersistentID();
	}

	public UUID getSpellOwner()
	{
		return this.spellOwner;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public UUID getSpellUUID()
	{
		return this.spellID;
	}

	public IPowerSource getPowerSource()
	{
		return this.powerSource;
	}

	public SpellStage[] getSpellStages()
	{
		return this.spellStages;
	}

	@Override
	public String toString()
	{
		String toReturn = "";

		toReturn = toReturn + "--------- Spell Printout: ---------\nName: " + this.getName() + "\n";
		toReturn = toReturn + "Power Source: " + this.getPowerSource() + "\n";
		toReturn = toReturn + "Spell has " + this.spellStages.length + " spell stages.";

		return toReturn;
	}
}
