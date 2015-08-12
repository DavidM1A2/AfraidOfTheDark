/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.packets.TellClientToPlayAnimation;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

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
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;

public class EntityWerewolf extends EntityMob implements IMCAnimatedEntity
{
	protected AnimationHandler animHandler = new AnimationHandlerWerewolf(this);
	// setup movespeed, agroRange, and followRange
	private static final double moveSpeed = .43D;
	private static final double agroRange = 16.0D;
	private static final double followRange = 32.0D;

	// AI wanderer and watcher
	private EntityAIWander myWanderer = new EntityAIWander(this, EntityWerewolf.moveSpeed * 2);
	private EntityAIWatchClosest myWatchClosest = new EntityAIWatchClosest(this, EntityPlayer.class, (float) EntityWerewolf.agroRange);
	private final CustomWerewolfTargetLocator myTargetLocator = new CustomWerewolfTargetLocator(this, EntityPlayer.class, 10, true);

	public EntityWerewolf(final World world)
	{
		// Set the model size
		super(world);
		this.setSize(1.8F, 1.6F);

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

	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);

		if (cause instanceof EntityDamageSource)
		{
			if (cause.getEntity() instanceof EntityPlayer)
			{
				if (((EntityPlayer) cause.getEntity()).inventory.consumeInventoryItem(Items.glass_bottle))
				{
					this.dropItem(ModItems.werewolfBlood, 1);
				}
			}
		}
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float strafe, float forward)
	{
		if (this.motionX > 0.05 || this.motionZ > 0.05 || this.motionX < -0.05 || this.motionZ < -0.05)
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
		return this.myTargetLocator.targetEntity == null ? "afraidofthedark:werewolfIdle" : "afraidofthedark:werewolfAgro";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "afraidofthedark:werewolfDeath";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "afraidofthedark:werewolfHurt";
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
		if (damageSource instanceof EntityDamageSource)
		{
			if (((EntityDamageSource) damageSource).damageType.equals("silverDamage"))
			{
				return super.attackEntityFrom(damageSource, damage);
			}
		}
		return super.attackEntityFrom(DamageSource.generic, 1);
	}

	@Override
	public boolean attackEntityAsMob(final Entity entity)
	{
		if (!animHandler.isAnimationActive("Bite"))
		{
			animHandler.activateAnimation("Bite", 0);
			AfraidOfTheDark.getSimpleNetworkWrapper().sendToAllAround(new TellClientToPlayAnimation("Bite", this.getEntityId()), new TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 15));
		}

		boolean x = super.attackEntityAsMob(entity);

		if (entity instanceof EntityPlayer)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) entity;

			if (entityPlayer.getHealth() != 0)
			{
				if (Research.canResearch(entityPlayer, ResearchTypes.WerewolfExamination))
				{
					Research.unlockResearchSynced(entityPlayer, ResearchTypes.WerewolfExamination, Side.SERVER, true);
				}
			}
		}

		return x;
	}

	@Override
	public float getEyeHeight()
	{
		return 1.3f;
	}
}
