package com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAnimation;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntitySplinterDrone extends EntityMob implements IMCAnimatedEntity
{
	protected AnimationHandler animHandler = new AnimationHandlerSplinterDrone(this);
	// setup MOVE_SPEED, AGRO_RANGE, and FOLLOW_RANGE
	private static final double MOVE_SPEED = 0D;
	private static final double AGRO_RANGE = 20.0D;
	private static final double FOLLOW_RANGE = 20.0D;
	private static final double MAX_HEALTH = 25.0D;
	private static final double ATTACK_DAMAGE = 2.0D;
	private static final double KNOCKBACK_RESISTANCE = 0.5D;
	private boolean hasPlayedStartAnimation = false;

	public EntitySplinterDrone(World par1World)
	{
		super(par1World);
		this.setSize(0.8F, 3.0F);

		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, (float) EntitySplinterDrone.AGRO_RANGE));
		this.tasks.addTask(4, new EntityAILookIdleSplinterDrone(this));

		this.targetTasks.addTask(1, new EntityAIAttackSplinterDrone(this));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Override
	public void onEntityUpdate()
	{
		if (!hasPlayedStartAnimation)
		{
			if (!this.worldObj.isRemote)
			{
				this.animHandler.activateAnimation("Activate", 0);
				AfraidOfTheDark.getPacketHandler().sendToAllAround(new SyncAnimation("Activate", this.getEntityId()), new TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 50));
				this.hasPlayedStartAnimation = true;
			}
		}
		super.onEntityUpdate();
	}

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return animHandler;
	}

	// Apply entity attributes
	@Override
	protected void applyEntityAttributes()
	{
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(EntitySplinterDrone.MAX_HEALTH);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(EntitySplinterDrone.FOLLOW_RANGE);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(EntitySplinterDrone.KNOCKBACK_RESISTANCE);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(EntitySplinterDrone.MOVE_SPEED);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(EntitySplinterDrone.ATTACK_DAMAGE);
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

	@Override
	public float getEyeHeight()
	{
		return 1.5f;
	}
}