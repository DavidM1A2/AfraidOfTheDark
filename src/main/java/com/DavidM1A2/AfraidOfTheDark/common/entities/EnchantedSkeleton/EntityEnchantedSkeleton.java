package com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.packets.TellClientToPlayAnimation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityEnchantedSkeleton extends EntityMob implements IMCAnimatedEntity
{
	protected AnimationHandler animHandler = new AnimationHandlerEnchantedSkeleton(this);
	// setup movespeed, agroRange, and followRange
	private static final double moveSpeed = .25D;
	private static final double agroRange = 16.0D;
	private static final double followRange = 32.0D;

	// AI wanderer and watcher

	public EntityEnchantedSkeleton(final World world)
	{
		// Set the model size
		super(world);
		this.setSize(0.8F, 2.0F);

		// Add various AI tasks
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWander(this, EntityEnchantedSkeleton.moveSpeed));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, (float) EntityEnchantedSkeleton.agroRange));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		// Use custom werewolf target locator
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));

		if (this.firstUpdate)
		{
			if (!this.animHandler.isAnimationActive("Spawn"))
			{
				this.animHandler.activateAnimation("Spawn", 0);
			}
		}
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
		if (animHandler.isAnimationActive("Spawn"))
		{
			return;
		}
		if (this.motionX > 0.05 || this.motionZ > 0.05 || this.motionX < -0.05 || this.motionZ < -0.05)
		{
			if (!animHandler.isAnimationActive("Attack") && !animHandler.isAnimationActive("Walk"))
			{
				animHandler.activateAnimation("Walk", 0);
			}
		}
		else
		{
			if (!animHandler.isAnimationActive("Attack") && !animHandler.isAnimationActive("Idle"))
			{
				animHandler.activateAnimation("Idle", 0);
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
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(EntityEnchantedSkeleton.followRange);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.5D);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(EntityEnchantedSkeleton.moveSpeed);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
		}
	}

	@Override
	public boolean attackEntityAsMob(final Entity entity)
	{
		if (!animHandler.isAnimationActive("Attack") && !animHandler.isAnimationActive("Spawn"))
		{
			animHandler.activateAnimation("Attack", 0);
			AfraidOfTheDark.getSimpleNetworkWrapper().sendToAllAround(new TellClientToPlayAnimation("Attack", this.getEntityId()), new TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 15));
		}

		boolean x = super.attackEntityAsMob(entity);

		//		if (entity instanceof EntityPlayer)
		//		{
		//			final EntityPlayer entityPlayer = (EntityPlayer) entity;
		//
		//			if (entityPlayer.getHealth() != 0)
		//			{
		//				if (Research.canResearch(entityPlayer, ResearchTypes.WerewolfExamination))
		//				{
		//					Research.unlockResearchSynced(entityPlayer, ResearchTypes.WerewolfExamination, Side.SERVER, true);
		//				}
		//			}
		//		}

		return x;
	}

	@Override
	public float getEyeHeight()
	{
		return 1.9f;
	}
}