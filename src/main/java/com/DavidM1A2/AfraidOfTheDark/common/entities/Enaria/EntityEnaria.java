/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.entities.ICanTakeSilverDamage;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class EntityEnaria extends EntityMob implements IMCAnimatedEntity, IBossDisplayData, ICanTakeSilverDamage
{
	protected AnimationHandler animHandler = new AnimationHandlerEnaria(this);

	private static final double MOVE_SPEED = 0.6D;
	private static final double AGRO_RANGE = 16.0D;
	private static final double FOLLOW_RANGE = 32.0D;
	private static final double MAX_HEALTH = 400.0D;
	private static final double ATTACK_DAMAGE = 12.0D;
	private static final double KNOCKBACK_RESISTANCE = 0.5D;
	private static final double RESEARCH_UNLOCK_RANGE = 50D;
	public static final String IS_VALID = "isValid";
	public static final String LAST_HIT = "lastHit";
	private static final int MAX_DAMAGE_IN_1_HIT = 10;
	private final EnariaAttacks enariaAttacks;

	public EntityEnaria(World world)
	{
		super(world);
		this.setSize(0.8F, 1.8F);
		this.experienceValue = 50;
		this.isImmuneToFire = true;

		this.setCustomNameTag("Enaria");

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackEnaria(this));
		this.tasks.addTask(3, new EntityAIFollowTarget(this, 8.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 20.0F));

		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));

		enariaAttacks = new EnariaAttacks(this, this.rand);
	}

	@Override
	public void onLivingUpdate()
	{
		if (this.ticksExisted == 1)
		{
			if (!this.getEntityData().getBoolean(IS_VALID))
			{
				this.onKillCommand();
			}
		}
		if (this.worldObj.isRemote)
		{
			BossStatus.setBossStatus(this, true);
		}
		super.onLivingUpdate();
	}

	// Apply entity attributes
	@Override
	protected void applyEntityAttributes()
	{
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(EntityEnaria.MAX_HEALTH);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(EntityEnaria.FOLLOW_RANGE);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(EntityEnaria.KNOCKBACK_RESISTANCE);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(EntityEnaria.MOVE_SPEED);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(EntityEnaria.ATTACK_DAMAGE);
		}
	}

	// Only take damage from silver weapons
	@Override
	public boolean attackEntityFrom(final DamageSource damageSource, float damage)
	{
		long timeBetweenHits = System.currentTimeMillis() - this.getEntityData().getLong(LAST_HIT);
		this.getEntityData().setLong(LAST_HIT, System.currentTimeMillis());

		// /kill the entity
		if (damage == Float.MAX_VALUE)
		{
			return super.attackEntityFrom(damageSource, damage);
		}
		// Cant nuke enaria
		else if (damage > MAX_DAMAGE_IN_1_HIT)
		{
			damage = MAX_DAMAGE_IN_1_HIT;
		}

		if (damageSource instanceof EntityDamageSource)
		{
			if (((EntityDamageSource) damageSource).damageType.equals("silverDamage"))
			{
				if (timeBetweenHits < 1000)
				{
					if (timeBetweenHits > 800)
					{
						damage = damage * 0.75f;
					}
					else if (timeBetweenHits > 500)
					{
						damage = damage * 0.35f;
					}
					else
					{
						damage = 1.0f;
						// 1/70 chance to teleport if taking silver damage
						if (this.rand.nextInt(40) == 0)
						{
							this.enariaAttacks.randomTeleport();
						}
					}
				}

				return super.attackEntityFrom(damageSource, damage);
			}
			else
			{
				// 1/60 chance to tele if taking non-silver, player damage
				if (this.rand.nextInt(60) == 0)
				{
					this.enariaAttacks.randomTeleport();
				}
			}
		}
		else
		{
			// 1/100 chance to tele if taking non-player damage
			if (this.rand.nextInt(100) == 0)
			{
				this.enariaAttacks.randomTeleport();
			}
		}

		return super.attackEntityFrom(DamageSource.generic, 1);
	}

	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);

		if (cause instanceof EntityDamageSource)
		{
			if (cause.getEntity() instanceof EntityPlayer)
			{
				for (Object object : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(RESEARCH_UNLOCK_RANGE, RESEARCH_UNLOCK_RANGE, RESEARCH_UNLOCK_RANGE)))
				{
					EntityPlayer entityPlayer = (EntityPlayer) object;

					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setHasBeatenEnaria(true);
					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).syncHasBeatenEnaria();

					if (!worldObj.isRemote)
					{
						if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.Enaria))
						{
							entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.Enaria, true);
						}
					}
				}
			}
		}
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText(EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + super.getDisplayName().getUnformattedTextForChat());
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float strafe, float forward)
	{
		if (this.motionX > 0.05 || this.motionZ > 0.05 || this.motionX < -0.05 || this.motionZ < -0.05)
		{
			if (!this.animHandler.isAnimationActive("spell") && !animHandler.isAnimationActive("autoattack") && !animHandler.isAnimationActive("armthrow") && !animHandler.isAnimationActive("walk"))
			{
				animHandler.activateAnimation("walk", 0);
			}
		}
		super.moveEntityWithHeading(strafe, forward);
	}

	/**
	 * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
	 */
	@Override
	public void mountEntity(Entity entity)
	{
		this.ridingEntity = null;
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return animHandler;
	}

	public EnariaAttacks getEnariaAttacks()
	{
		return this.enariaAttacks;
	}
}
