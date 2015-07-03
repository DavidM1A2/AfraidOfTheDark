/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf;

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
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.packets.TellClientToPlayAnimation;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class EntityWerewolf extends EntityMob implements IMCAnimatedEntity
{
	protected AnimationHandler animHandler = new AnimationHandlerWerewolf(this);
	// setup movespeed, agroRange, and followRange
	private static double moveSpeed = .43D;
	private static double agroRange = 16.0D;
	private static double followRange = 32.0D;

	// AI wanderer and watcher
	private EntityAIWander myWanderer = new EntityAIWander(this, EntityWerewolf.moveSpeed * 2);
	private EntityAIWatchClosest myWatchClosest = new EntityAIWatchClosest(this, EntityPlayer.class, (float) EntityWerewolf.agroRange);
	private final CustomWerewolfTargetLocator myTargetLocator = new CustomWerewolfTargetLocator(this, EntityPlayer.class, 10, true);

	public EntityWerewolf(final World world)
	{
		// Set the model size
		super(world);
		this.setSize(1.8F, 0.8F);

		// Add various AI tasks
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
		this.tasks.addTask(7, this.myWanderer);
		this.tasks.addTask(8, this.myWatchClosest);
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		// Use custom werewolf target locator
		this.targetTasks.addTask(2, myTargetLocator);
	}

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return animHandler;
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float strafe, float forward)
	{
		if (this.motionX > 0.05 || this.motionZ > 0.05)
		{
			if (!animHandler.isAnimationActive("Bite") && !animHandler.isAnimationActive("Run"))
			{
				animHandler.activateAnimation("Run", 0);
			}
		}
		super.moveEntityWithHeading(strafe, forward);
	}

	// Apply entity attributes
	@Override
	protected void applyEntityAttributes()
	{
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(EntityWerewolf.followRange);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.5D);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(EntityWerewolf.moveSpeed);
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
		return (float) EntityWerewolf.moveSpeed;
	}

	// Only take damage from silver weapons
	@Override
	public boolean attackEntityFrom(final DamageSource damageSource, final float damage)
	{
		if (damageSource.damageType.equals(Constants.AOTDDamageSources.silverDamage.damageType) || damageSource.damageType.equals(DamageSource.outOfWorld))
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
		if (!animHandler.isAnimationActive("Bite"))
		{
			animHandler.activateAnimation("Bite", 0);
			AfraidOfTheDark.getSimpleNetworkWrapper().sendToAllAround(new TellClientToPlayAnimation("Bite", this.getEntityId()), new TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 15));
		}
		return super.attackEntityAsMob(entity);
	}

	@Override
	public float getEyeHeight()
	{
		return 1.3f;
	}

	// This is used to set movespeed and agro range during full moons
	public static void setMoveSpeedAndAgroRange(final double _moveSpeed, final double _agroRange, final double _followRange)
	{
		EntityWerewolf.moveSpeed = _moveSpeed;
		EntityWerewolf.agroRange = _agroRange;
		EntityWerewolf.followRange = _followRange;
	}

	// Various getters and setters
	public EntityAIWander getWanderer()
	{
		return this.myWanderer;
	}

	public void setWanderer()
	{
		this.myWanderer = new EntityAIWander(this, EntityWerewolf.moveSpeed * 2);
	}

	public EntityAIWatchClosest getMyWatchClosest()
	{
		return this.myWatchClosest;
	}

	public void setMyWatchClosest()
	{
		this.myWatchClosest = new EntityAIWatchClosest(this, EntityPlayer.class, (float) EntityWerewolf.agroRange);
	}
}
