/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.IPowerSource;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.PowerSource;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;

public class Spell implements Serializable
{
	private String name;
	private IPowerSource powerSource;
	private List<SpellStage> spellStages = new ArrayList<SpellStage>();
	private UUID spellID;
	private UUID spellOwner;

	public Spell(NBTTagCompound spellData)
	{
		this.readFromNBT(spellData);
	}

	public Spell(EntityPlayer owner, String name, IPowerSource powerSource, UUID spellID)
	{
		this.name = name;
		this.powerSource = powerSource;
		this.spellID = spellID;
		this.spellOwner = owner.getPersistentID();
	}

	public void writeToNBT(NBTTagCompound spellCompound)
	{
		spellCompound.setString("spellName", this.name);
		NBTTagCompound powerSourceData = new NBTTagCompound();
		if (this.powerSource != null)
			this.powerSource.writeToNBT(powerSourceData);
		else
			powerSourceData.setBoolean("null", true);
		spellCompound.setTag("spellPowerSource", powerSourceData);
		spellCompound.setInteger("numberOfSpellStages", this.spellStages.size());
		for (int i = 0; i < spellStages.size(); i++)
		{
			NBTTagCompound spellStage = new NBTTagCompound();
			spellStages.get(i).writeToNBT(spellStage);
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
		this.powerSource = PowerSource.create(powerSourceData);
		int numberOfSpellStages = spellCompound.getInteger("numberOfSpellStages");
		for (int i = 0; i < numberOfSpellStages; i++)
		{
			NBTTagCompound spellStageData = spellCompound.getCompoundTag("spellStage " + i);
			spellStages.add(new SpellStage(spellStageData));
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
		if (entityPlayer != null && !entityPlayer.worldObj.isRemote)
		{
			if (entityPlayer.dimension != AOTDDimensions.Nightmare.getWorldID())
			{
				if (this.isSpellValid() && this.powerSource.attemptToCast(this))
					for (EntitySpell entitySpell : this.spellStages.get(0).getDeliveryMethod().createSpellEntity(this))
						entityPlayer.worldObj.spawnEntityInWorld(entitySpell);
				else if (!this.isSpellValid())
					entityPlayer.addChatMessage(new TextComponentString("Invalid spell. Make sure to have delivery methods on each spell stage and a power source!"));
				else
					entityPlayer.addChatMessage(new TextComponentString(this.powerSource.notEnoughEnergyMsg()));
			}
			else
				entityPlayer.addChatMessage(new TextComponentString("My mind is too clouded to cast spells here"));
		}
		else
			LogHelper.info("Attempted to instantiate a spell on an offline player...");
	}

	public double getCost()
	{
		double cost = 0;
		double currentCostMultiplier = 1.0;
		for (SpellStage spellStage : this.spellStages)
		{
			cost = currentCostMultiplier * (cost + spellStage.getCost());
			double newCostMultiplier = currentCostMultiplier * (spellStage.getDeliveryMethod() != null ? spellStage.getDeliveryMethod().getStageMultiplier() : 1.0D);
			currentCostMultiplier = MathHelper.clamp_double(newCostMultiplier, 1.0, Double.MAX_VALUE);
		}
		return cost;
	}

	private boolean isSpellValid()
	{
		boolean isValid = true;

		if (this.powerSource == null)
			isValid = false;
		if (this.spellStages.size() == 0)
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
		return this.spellStages.get(index);
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

	public void setPowerSource(IPowerSource powerSource)
	{
		this.powerSource = powerSource;
	}

	public IPowerSource getPowerSource()
	{
		return this.powerSource;
	}

	public List<SpellStage> getSpellStages()
	{
		return this.spellStages;
	}

	@Override
	public String toString()
	{
		String toReturn = "";

		toReturn = toReturn + "--------- Spell Printout: ---------\nName: " + this.getName() + "\n";
		toReturn = toReturn + "Power Source: " + this.getPowerSource() + "\n";
		toReturn = toReturn + "Spell has " + this.spellStages.size() + " spell stages.";

		return toReturn;
	}
}
