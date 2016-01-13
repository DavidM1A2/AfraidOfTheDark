/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.spell;

import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTObjectWriter;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntitySpell extends Entity
{
	private static final String SPELL_SOURCE = "spellSource";
	private Spell spellSource;
	private static final String SPELL_STAGE_KEY = "spellStageIndex";
	private int spellStageIndex;

	public EntitySpell(World worldIn)
	{
		super(worldIn);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		this.updateSpellLogic();
	}

	@Override
	protected void entityInit()
	{
	}

	protected abstract void updateSpellLogic();

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		this.spellStageIndex = compound.getInteger(SPELL_STAGE_KEY);
		this.spellSource = (Spell) NBTObjectWriter.readObjectFromNBT(SPELL_SOURCE, compound);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger(SPELL_STAGE_KEY, this.spellStageIndex);
		NBTObjectWriter.writeObjectToNBT(SPELL_SOURCE, this.spellSource, compound);
	}

	public void spellComplete()
	{
		this.spellSource.spellStageCallback(this.spellSource.getSpellStage(this.spellStageIndex).getKey(), this.spellStageIndex);
	}

	public void setSpellSource(Spell spell)
	{
		this.spellSource = spell;
	}

	public Spell getSpellSource()
	{
		return this.spellSource;
	}

	public void setSpellStageIndex(int index)
	{
		this.spellStageIndex = index;
	}
}
