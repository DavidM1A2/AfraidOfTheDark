/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.entities.ICanTakeSilverDamage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;

public class EntityEnaria extends EntityMob implements IMCAnimatedEntity, IBossDisplayData, ICanTakeSilverDamage
{
	protected AnimationHandler animHandler = new AnimationHandlerEnaria(this);

	private static final double MOVE_SPEED = 0.6D;
	private static final double AGRO_RANGE = 16.0D;
	private static final double FOLLOW_RANGE = 32.0D;
	private static final double MAX_HEALTH = 40.0D;
	private static final double ATTACK_DAMAGE = 4.0D;
	private static final double KNOCKBACK_RESISTANCE = 0.5D;
	public static final String IS_VALID = "isValid";
	public static final String LAST_HIT = "lastHit";
	private static final int MAX_DAMAGE_IN_1_HIT = 10;
	private final EnariaAttacks enariaAttacks;

	public EntityEnaria(World world)
	{
		super(world);
		this.setSize(0.8F, 1.8F);

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
				this.killCommand();
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
						// 1/2 chance to teleport if taking silver damage
						if (this.rand.nextInt(2) == 0)
						{
							this.enariaAttacks.randomTeleport();
						}
					}
				}

				return super.attackEntityFrom(damageSource, damage);
			}
			else
			{
				// 1/4 chance to tele if taking non-silver, player damage
				if (this.rand.nextInt(4) == 0)
				{
					this.enariaAttacks.randomTeleport();
				}
			}
		}
		else
		{
			// 1/10 chance to tele if taking non-player damage
			if (this.rand.nextInt(10) == 0)
			{
				this.enariaAttacks.randomTeleport();
			}
		}

		return super.attackEntityFrom(DamageSource.generic, 1);
	}

	/**
	 * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
	 */
	@Override
	public void mountEntity(Entity entity)
	{
		this.ridingEntity = null;
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