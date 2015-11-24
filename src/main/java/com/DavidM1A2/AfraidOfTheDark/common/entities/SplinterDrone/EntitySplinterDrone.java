package com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAnimation;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntitySplinterDrone extends EntityFlying implements IMCAnimatedEntity, IMob
{
	protected AnimationHandler animHandler = new AnimationHandlerSplinterDrone(this);
	// setup MOVE_SPEED, AGRO_RANGE, and FOLLOW_RANGE
	private static final double MOVE_SPEED = 0.05D;
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
		this.isImmuneToFire = true;
		this.experienceValue = 7;

		this.moveHelper = new EntitySplinterDroneMoveHelper(this);

		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, (float) EntitySplinterDrone.AGRO_RANGE));
		this.tasks.addTask(4, new EntityAIHoverSplinterDrone(this));
		this.tasks.addTask(5, new EntityAILookIdleSplinterDrone(this));

		this.targetTasks.addTask(1, new EntityAIAttackSplinterDrone(this));
		this.targetTasks.addTask(2, new EntityAIFindEntityNearestPlayer(this));
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
		if (!animHandler.isAnimationActive("Activate") && !animHandler.isAnimationActive("Charge") && !animHandler.isAnimationActive("Idle"))
		{
			if (this.worldObj.isRemote)
			{
				animHandler.activateAnimation("Idle", 0);
			}
		}
		super.onEntityUpdate();
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
		{
			this.setDead();
		}
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
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(EntitySplinterDrone.MAX_HEALTH);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(EntitySplinterDrone.FOLLOW_RANGE);
		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(EntitySplinterDrone.KNOCKBACK_RESISTANCE);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(EntitySplinterDrone.MOVE_SPEED);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(EntitySplinterDrone.ATTACK_DAMAGE);
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

				if (!worldObj.isRemote)
				{
					if (AOTDPlayerData.get(entityPlayer).canResearch(ResearchTypes.GnomishCity))
					{
						AOTDPlayerData.get(entityPlayer).unlockResearch(ResearchTypes.GnomishCity, true);
						if (AOTDPlayerData.get(entityPlayer).getHasBeatenEnaria())
						{
							AOTDPlayerData.get(entityPlayer).unlockResearch(ResearchTypes.Enaria, true);
						}
					}
				}
			}
		}
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean unused, int number)
	{
		this.dropItem(ModItems.gnomishMetalIngot, rand.nextInt(5) + 3);
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