/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.WereWolf;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class EntityWereWolf extends EntityMob
{
	// setup movespeed, agroRange, and followRange
	private static double moveSpeed = .43D;
	private static double agroRange = 16.0D;
	private static double followRange = 32.0D;

	// AI wanderer and watcher
	private EntityAIWander myWanderer = new EntityAIWander(this, EntityWereWolf.moveSpeed * 10);
	private EntityAIWatchClosest myWatchClosest = new EntityAIWatchClosest(this, EntityPlayer.class, (float) EntityWereWolf.agroRange);

	public EntityWereWolf(final World world)
	{
		// Set the model size
		super(world);
		this.setSize(.7F, 2);

		// Add various AI tasks
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
		this.tasks.addTask(7, this.myWanderer);
		this.tasks.addTask(8, this.myWatchClosest);
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		// Use custom werewolf target locator
		this.targetTasks.addTask(2, new CustomWerewolfTargetLocator(this, EntityPlayer.class, 10, true));

	}

	// Apply entity attributes
	@Override
	protected void applyEntityAttributes()
	{
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0D);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(EntityWereWolf.followRange);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.0D);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(EntityWereWolf.moveSpeed);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(20.0D);
		}
	}

	// Get werewolf sound
	@Override
	protected String getLivingSound()
	{
		return "afraidofthedark:werewolf";
	}

	// Get werewolf's volume
	@Override
	protected float getSoundVolume()
	{
		return 1.0F;
	}

	// Get the AI movespeed
	@Override
	public float getAIMoveSpeed()
	{
		return (float) EntityWereWolf.moveSpeed;
	}

	// Only take damage from silver weapons
	@Override
	public boolean attackEntityFrom(final DamageSource damageSource, final float damage)
	{
		if (damageSource.damageType.equals(Refrence.silverWeapon.damageType) || damageSource.damageType.equals(DamageSource.outOfWorld))
		{
			return super.attackEntityFrom(damageSource, damage);
		}
		else
		{
			return super.attackEntityFrom(DamageSource.generic, 1);
		}
	}

	@Override
	public boolean attackEntityAsMob(final Entity entity)
	{
		if (entity instanceof EntityPlayer)
		{
			final EntityPlayer thePlayer = (EntityPlayer) entity;
			if (LoadResearchData.canResearch(thePlayer, ResearchTypes.WerewolfExamination))
			{
				LoadResearchData.unlockResearchSynced(thePlayer, ResearchTypes.WerewolfExamination, Side.SERVER, true);
			}
		}
		return super.attackEntityAsMob(entity);
	}

	@Override
	public float getEyeHeight()
	{
		return 2.0f;
	}

	// This is used to set movespeed and agro range during full moons
	public static void setMoveSpeedAndAgroRange(final double _moveSpeed, final double _agroRange, final double _followRange)
	{
		EntityWereWolf.moveSpeed = _moveSpeed;
		EntityWereWolf.agroRange = _agroRange;
		EntityWereWolf.followRange = _followRange;
	}

	// Various getters and setters
	public EntityAIWander getWanderer()
	{
		return this.myWanderer;
	}

	public void setWanderer()
	{
		this.myWanderer = new EntityAIWander(this, EntityWereWolf.moveSpeed * 10);
	}

	public EntityAIWatchClosest getMyWatchClosest()
	{
		return this.myWatchClosest;
	}

	public void setMyWatchClosest()
	{
		this.myWatchClosest = new EntityAIWatchClosest(this, EntityPlayer.class, (float) EntityWereWolf.agroRange);
	}
}
