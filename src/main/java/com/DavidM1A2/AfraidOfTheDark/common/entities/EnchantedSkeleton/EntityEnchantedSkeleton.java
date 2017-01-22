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
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
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
	}

	@Override
	protected void initEntityAI()
	{
		// Add various AI tasks
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
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
				AfraidOfTheDark.instance.getPacketHandler().sendToAllAround(new SyncAnimation("Spawn", this), new TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 50));
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
				if (killer.getHeldItemMainhand() != null)
				{
					if (killer.getHeldItemMainhand().getItem() instanceof ItemBladeOfExhumation)
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
				if (killer.getHeldItemMainhand() != null)
				{
					if (killer.getHeldItemMainhand().getItem() instanceof ItemBladeOfExhumation)
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
		if (this.worldObj.isRemote)
		{
			if (!animHandler.isAnimationActive("Spawn") && !animHandler.isAnimationActive("Attack"))
			{
				if (this.motionX > 0.05 || this.motionZ > 0.05 || this.motionX < -0.05 || this.motionZ < -0.05)
				{
					if (!animHandler.isAnimationActive("Walk"))
					{
						animHandler.activateAnimation("Walk", 0);
					}
				}
				else
				{
					if (!animHandler.isAnimationActive("Idle"))
					{
						animHandler.activateAnimation("Idle", 0);
					}
				}
			}
		}
		super.moveEntityWithHeading(strafe, forward);
	}

	// Apply entity attributes
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EntityEnchantedSkeleton.MAX_HEALTH);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(EntityEnchantedSkeleton.FOLLOW_RANGE);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(EntityEnchantedSkeleton.KNOCKBACK_RESISTANCE);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EntityEnchantedSkeleton.MOVE_SPEED);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(EntityEnchantedSkeleton.ATTACK_DAMAGE);
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
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected SoundEvent getHurtSound()
	{
		return SoundEvents.ENTITY_SKELETON_HURT;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_SKELETON_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_)
	{
		this.playSound(SoundEvents.ENTITY_SKELETON_STEP, 0.15F, 1.0F);
	}

	@Override
	public boolean attackEntityAsMob(final Entity entity)
	{
		if (entity instanceof EntityPlayer)
		{
			((EntityPlayer) entity).addPotionEffect(new PotionEffect(Potion.getPotionById(2), 80, 0, false, true));
			((EntityPlayer) entity).addPotionEffect(new PotionEffect(Potion.getPotionById(18), 80, 0, false, true));
		}
		AfraidOfTheDark.instance.getPacketHandler().sendToAllAround(new SyncAnimation("Attack", this, "Attack", "Spawn"), new TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 15));

		return super.attackEntityAsMob(entity);
	}

	@Override
	public float getEyeHeight()
	{
		return 1.9f;
	}
}