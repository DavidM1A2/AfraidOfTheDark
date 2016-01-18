/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.spell;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTObjectWriter;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class EntitySpell extends Entity implements IMCAnimatedEntity
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
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
		if (this.ticksExisted >= this.getSpellLifeInTicks())
		{
			this.setDead();
		}
		this.updateSpellSpecificLogic();
	}

	@Override
	protected void entityInit()
	{
	}

	protected abstract void updateSpellSpecificLogic();

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

	public void spellStageComplete()
	{
		this.spellSource.spellStageCallback(this.spellStageIndex);
	}

	public void performEffect(BlockPos location)
	{

	}

	public void performEffect(Entity entity)
	{

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

	public abstract int getSpellLifeInTicks();
}
