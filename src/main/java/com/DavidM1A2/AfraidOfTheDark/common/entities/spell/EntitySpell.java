/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.spell;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;

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
	private static final String SPELL_TICKS_ALIVE = "spellTicksAlive";
	private int ticksAlive;
	private static final String SPELL_COLOR = "spellColor";
	private float[] color = new float[4];

	public EntitySpell(World world)
	{
		super(world);
		this.setSize(this.getSpellEntityWidth(), this.getSpellEntityHeight());
		color[0] = color[1] = color[2] = color[3] = 1.0f;
	}

	public EntitySpell(World world, Spell callback, int spellStageIndex)
	{
		this(world);
		this.spellSource = callback;
		this.spellStageIndex = spellStageIndex;
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
		this.ticksAlive++;
		if (this.ticksAlive >= this.getSpellLifeInTicks())
		{
			this.spellStageComplete();
			this.setDead();
		}
		else
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
		this.spellSource = new Spell(compound.getCompoundTag(SPELL_SOURCE));
		this.ticksAlive = compound.getInteger(SPELL_TICKS_ALIVE);
		this.color[0] = compound.getFloat(SPELL_COLOR + "r");
		this.color[1] = compound.getFloat(SPELL_COLOR + "g");
		this.color[2] = compound.getFloat(SPELL_COLOR + "b");
		this.color[3] = compound.getFloat(SPELL_COLOR + "a");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger(SPELL_STAGE_KEY, this.spellStageIndex);
		NBTTagCompound spellData = new NBTTagCompound();
		this.spellSource.writeToNBT(spellData);
		compound.setTag(SPELL_SOURCE, spellData);
		compound.setInteger(SPELL_TICKS_ALIVE, this.ticksAlive);
		compound.setFloat(SPELL_COLOR + "r", this.color[0]);
		compound.setFloat(SPELL_COLOR + "g", this.color[1]);
		compound.setFloat(SPELL_COLOR + "b", this.color[2]);
		compound.setFloat(SPELL_COLOR + "a", this.color[3]);
	}

	public void spellStageComplete()
	{
		this.spellStageIndex = this.spellStageIndex + 1;
		if (!this.worldObj.isRemote)
		{
			if (!this.spellSource.hasSpellStage(spellStageIndex))
			{
				return;
			}
			this.worldObj.spawnEntityInWorld(this.getSpellSource().getSpellStageByIndex(this.spellStageIndex).getDeliveryMethod().createSpellEntity(this, spellStageIndex));
		}
	}

	public void terminateSpell()
	{
		this.spellStageIndex = Integer.MAX_VALUE - 1;
		this.spellStageComplete();
	}

	public void performEffect(BlockPos location)
	{
		if (!this.worldObj.isRemote)
			for (IEffect effect : this.getSpellSource().getSpellStageByIndex(this.getSpellStageIndex()).getEffects())
			{
				effect.performEffect(location, this.worldObj);
			}
		return;
	}

	public void performEffect(Entity entity)
	{
		if (!this.worldObj.isRemote)
			for (IEffect effect : this.getSpellSource().getSpellStageByIndex(this.getSpellStageIndex()).getEffects())
			{
				effect.performEffect(entity);
			}
		return;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	public int getTicksAlive()
	{
		return this.ticksAlive;
	}

	public Spell getSpellSource()
	{
		return this.spellSource;
	}

	public int getSpellStageIndex()
	{
		return this.spellStageIndex;
	}

	public void setSpellColor(float r, float g, float b, float a)
	{
		this.color[0] = r;
		this.color[1] = g;
		this.color[2] = b;
		this.color[3] = a;
	}

	public float[] getSpellColor()
	{
		return this.color;
	}

	@Override
	public boolean isImmuneToExplosions()
	{
		return true;
	}

	public abstract float getSpellEntityWidth();

	public abstract float getSpellEntityHeight();

	public abstract int getSpellLifeInTicks();
}
