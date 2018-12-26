/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.entities.ICanTakeSilverDamage;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModSounds;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAnimation;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityWerewolf extends EntityMob implements IMCAnimatedEntity, ICanTakeSilverDamage
{
	protected AnimationHandler animHandler = new AnimationHandlerWerewolf(this);
	// setup movespeed, agroRange, and followRange
	private static final double moveSpeed = .43D;
	private static final double agroRange = 16.0D;
	private static final double followRange = 32.0D;
	private boolean attacksAnyone = false;

	public EntityWerewolf(final World world)
	{
		// Set the model size
		super(world);
		this.setSize(1.8F, 1.6F);
		this.setCanAttackAnyone(false);
		this.experienceValue = 10;
	}

	@Override
	protected void initEntityAI()
	{
		// AI wanderer and watcher
		EntityAIWander wander = new EntityAIWander(this, EntityWerewolf.moveSpeed * 2);
		EntityAIWatchClosest watchClosest = new EntityAIWatchClosest(this, EntityPlayer.class, (float) EntityWerewolf.agroRange);
		CustomWerewolfTargetLocator targetLocator = new CustomWerewolfTargetLocator(this, EntityPlayer.class, 10, true);

		// Add various AI tasks
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(7, wander);
		this.tasks.addTask(8, watchClosest);
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		// Use custom werewolf target locator
		this.targetTasks.addTask(2, targetLocator);
	}

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return animHandler;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		this.attacksAnyone = nbttagcompound.getBoolean("attacksAnyone");
		super.readEntityFromNBT(nbttagcompound);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		nbttagcompound.setBoolean("attacksAnyone", attacksAnyone);
		super.writeEntityToNBT(nbttagcompound);
	}

	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);

		if (cause instanceof EntityDamageSource)
		{
			if (cause.getEntity() instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) cause.getEntity();

				if (!world.isRemote)
				{
					if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.SlayingOfTheWolves))
					{
						entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.SlayingOfTheWolves, true);
					}
				}

				if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.SlayingOfTheWolves))
				{
					boolean needToAddBlood = false;
					for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
						if (itemStack != null)
							if (itemStack.getItem() == Items.GLASS_BOTTLE)
							{
								itemStack.setCount(itemStack.getCount() - 1);
								needToAddBlood = true;
								break;
							}
					if (needToAddBlood)
						entityPlayer.inventory.addItemStackToInventory(new ItemStack(ModItems.werewolfBlood, 1));
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
		if (this.world.isRemote)
			if (this.motionX > 0.05 || this.motionZ > 0.05 || this.motionX < -0.05 || this.motionZ < -0.05)
				if (!animHandler.isAnimationActive("Bite") && !animHandler.isAnimationActive("Run"))
					animHandler.activateAnimation("Run", 0);

		super.moveEntityWithHeading(strafe, forward);
	}

	// Apply entity attributes
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(EntityWerewolf.followRange);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EntityWerewolf.moveSpeed);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(20.0D);
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected SoundEvent getDeathSound()
	{
		return ModSounds.werewolfDeath;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected SoundEvent getHurtSound()
	{
		return ModSounds.werewolfHurt;
	}

	// Get werewolf's volume
	@Override
	protected float getSoundVolume()
	{
		return 0.5F;
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
		return super.attackEntityFrom(DamageSource.GENERIC, 1);
	}

	@Override
	public boolean attackEntityAsMob(final Entity entity)
	{
		AfraidOfTheDark.instance.getPacketHandler().sendToAllAround(new SyncAnimation("Bite", this, "Bite"), new TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 15));

		boolean x = super.attackEntityAsMob(entity);

		if (entity instanceof EntityPlayer)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) entity;

			if (entityPlayer.getHealth() != 0)
			{
				if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.WerewolfExamination))
				{
					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.WerewolfExamination, true);
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

	public boolean canAttackAnyone()
	{
		return this.attacksAnyone;
	}

	public void setCanAttackAnyone(boolean attacksAnyone)
	{
		this.attacksAnyone = attacksAnyone;
	}

}
