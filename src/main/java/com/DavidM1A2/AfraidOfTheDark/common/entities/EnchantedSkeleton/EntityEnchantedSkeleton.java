/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemBladeOfExhumation;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAnimation;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityEnchantedSkeleton extends EntityMob implements IMCAnimatedEntity
{
	protected AnimationHandler animHandler = new AnimationHandlerEnchantedSkeleton(this);
	// setup MOVE_SPEED, AGRO_RANGE, and FOLLOW_RANGE
	private static final double MOVE_SPEED = .25D;
	private static final double AGRO_RANGE = 16.0D;
	private static final double FOLLOW_RANGE = 32.0D;
	private static final double MAX_HEALTH = 10.0D;
	private static final double ATTACK_DAMAGE = 4.0D;
	private static final double KNOCKBACK_RESISTANCE = 0.5D;
	private boolean hasPlayedStartAnimation = false;

	public EntityEnchantedSkeleton(final World world)
	{
		// Set the model size
		super(world);
		this.setSize(0.8F, 2.0F);
		this.experienceValue = 4;

		// Add various AI tasks
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWander(this, EntityEnchantedSkeleton.MOVE_SPEED));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, (float) EntityEnchantedSkeleton.AGRO_RANGE));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Override
	public void onEntityUpdate()
	{
		if (!this.worldObj.isRemote)
		{
			if (!hasPlayedStartAnimation)
			{
				this.animHandler.activateAnimation("Spawn", 0);
				AfraidOfTheDark.instance.getPacketHandler().sendToAllAround(new SyncAnimation("Spawn", this.getEntityId()), new TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 50));
				this.hasPlayedStartAnimation = true;
			}
		}
		super.onEntityUpdate();
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource damageSource)
	{
		super.onDeath(damageSource);
		if (damageSource.getSourceOfDamage() instanceof EntityPlayer)
		{
			EntityPlayer killer = (EntityPlayer) damageSource.getSourceOfDamage();
			if (killer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.BladeOfExhumation))
			{
				if (killer.getCurrentEquippedItem() != null)
				{
					if (killer.getCurrentEquippedItem().getItem() instanceof ItemBladeOfExhumation)
					{
						this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY + 1, this.posZ, new ItemStack(ModItems.enchantedSkeletonBone, 1, 0)));
					}
				}
			}
			else if (killer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.BladeOfExhumation))
			{
				if (!killer.worldObj.isRemote)
				{
					killer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.BladeOfExhumation, true);
				}
				if (killer.getCurrentEquippedItem() != null)
				{
					if (killer.getCurrentEquippedItem().getItem() instanceof ItemBladeOfExhumation)
					{
						this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY + 1, this.posZ, new ItemStack(ModItems.enchantedSkeletonBone, 1, 0)));
					}
				}
			}
		}
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		this.dropItem(this.getDropItem(), 3);
	}

	@Override
	protected Item getDropItem()
	{
		return ModItems.enchantedSkeletonBone;
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
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(EntityEnchantedSkeleton.MAX_HEALTH);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(EntityEnchantedSkeleton.FOLLOW_RANGE);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(EntityEnchantedSkeleton.KNOCKBACK_RESISTANCE);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(EntityEnchantedSkeleton.MOVE_SPEED);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(EntityEnchantedSkeleton.ATTACK_DAMAGE);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		tagCompound.setBoolean("hasPlayedStartAnimation", hasPlayedStartAnimation);
		super.writeEntityToNBT(tagCompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		this.hasPlayedStartAnimation = tagCompund.getBoolean("hasPlayedStartAnimation");
		super.readEntityFromNBT(tagCompund);
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.skeleton.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.skeleton.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.skeleton.death";
	}

	@Override
	protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_)
	{
		this.playSound("mob.skeleton.step", 0.15F, 1.0F);
	}

	@Override
	public boolean attackEntityAsMob(final Entity entity)
	{
		if (entity instanceof EntityPlayer)
		{
			((EntityPlayer) entity).addPotionEffect(new PotionEffect(2, 80, 0, false, true));
			((EntityPlayer) entity).addPotionEffect(new PotionEffect(18, 80, 0, false, true));
		}

		if (!animHandler.isAnimationActive("Attack") && !animHandler.isAnimationActive("Spawn"))
		{
			animHandler.activateAnimation("Attack", 0);
			AfraidOfTheDark.instance.getPacketHandler().sendToAllAround(new SyncAnimation("Attack", this.getEntityId()), new TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 15));
		}

		return super.attackEntityAsMob(entity);
	}

	@Override
	public float getEyeHeight()
	{
		return 1.9f;
	}
}