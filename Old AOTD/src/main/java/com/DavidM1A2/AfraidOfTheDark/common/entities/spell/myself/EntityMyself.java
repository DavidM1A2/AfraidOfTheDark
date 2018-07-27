package com.DavidM1A2.AfraidOfTheDark.common.entities.spell.myself;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellHitInfo;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityMyself extends EntitySpell
{
	private EntityLivingBase entityLivingBase;

	public EntityMyself(World world)
	{
		super(world);
	}

	public EntityMyself(World world, Spell callback, int spellStageIndex, EntityLivingBase entityLivingBase)
	{
		super(world, callback, spellStageIndex);
		this.entityLivingBase = entityLivingBase;
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		if (entityLivingBase != null)
			this.setPosition(entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ);
		else
		{
			this.terminateSpell();
			this.setDead();
		}
	}

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return null;
	}

	@Override
	public void updateSpellSpecificLogic()
	{
		if (this.getTicksAlive() == 1 && entityLivingBase != null)
		{
			this.performEffect(new SpellHitInfo(AfraidOfTheDark.proxy.getSpellOwner(this.getSpellSource()), entityLivingBase));
			this.spellStageComplete();
			this.setDead();
		}
	}

	@Override
	public float getSpellEntityWidth()
	{
		return 0;
	}

	@Override
	public float getSpellEntityHeight()
	{
		return 0;
	}

	@Override
	public int getSpellLifeInTicks()
	{
		return 20;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	public boolean canBeAttackedWithItem()
	{
		return false;
	}

	@Override
	public boolean isBurning()
	{
		return false;
	}

	@Override
	public boolean isImmuneToExplosions()
	{
		return true;
	}

	@Override
	public boolean isInvisible()
	{
		return true;
	}

	public EntityLivingBase getTarget()
	{
		return this.entityLivingBase;
	}
}
