/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.io.Serializable;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDEntityData;

import net.minecraft.entity.player.EntityPlayer;

public class Spell implements Serializable
{
	private String name = "";
	private IPowerSource powerSource;
	private List<SpellStage> spellStages;

	public Spell(String name, IPowerSource powerSource, List<SpellStage> spellStages)
	{
		this.name = name;
		this.spellStages = spellStages;
		this.powerSource = powerSource;
	}

	public void instantiateSpell(EntityPlayer entityPlayer)
	{
		this.powerSource.usePower(entityPlayer, this.getCost());
		this.spellStages.get(0).fireStage(entityPlayer, this);
	}

	public void spellStageCallback()
	{

	}

	public double getCost()
	{
		double cost = 0;
		for (SpellStage spellStage : this.spellStages)
			cost = cost + spellStage.getCost();
		return cost;
	}

	public boolean canCast(EntityPlayer entityPlayer)
	{
		if (AOTDEntityData.get(entityPlayer).getVitaeLevel() > this.powerSource.getPowerLevel())
		{
			return true;
		}
		return false;
	}

	// Getters/Setters
	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}
}
