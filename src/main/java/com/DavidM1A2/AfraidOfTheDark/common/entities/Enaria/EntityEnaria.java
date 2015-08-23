/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.entities.ICanTakeSilverDamage;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;

import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityEnaria extends EntityMob implements IMCAnimatedEntity, IBossDisplayData, IRangedAttackMob, ICanTakeSilverDamage
{
	protected AnimationHandler animHandler = new AnimationHandlerEnaria(this);

	private static final double MOVE_SPEED = 0.25D;
	private static final double AGRO_RANGE = 16.0D;
	private static final double FOLLOW_RANGE = 32.0D;
	private static final double MAX_HEALTH = 400.0D;
	private static final double ATTACK_DAMAGE = 4.0D;
	private static final double KNOCKBACK_RESISTANCE = 0.5D;
	public static final String IS_VALID = "isValid";
	public static final String LAST_HIT = "lastHit";
	private static final int MAX_DAMAGE_IN_1_HIT = 10;
	private static final int TELEPORT_RANGE = 20;
	private static final double KNOCKBACK_POWER = 30;
	private static final int MAX_KNOCKBACK_RANGE = 10;
	private static final int POTION_POISON_RANGE = 20;
	private Random random = new Random();
	private final PotionEffect[] possibleEffects;

	public EntityEnaria(World world)
	{
		super(world);
		this.setSize(0.8F, 1.8F);

		this.setCustomNameTag("Enaria");

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackEnaria(this));
		this.tasks.addTask(3, new EntityAIFollowTarget(this, EntityEnaria.MOVE_SPEED, 8.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 20.0F));

		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));

		possibleEffects = new PotionEffect[]
		{
				// Slowness
				new PotionEffect(2, 300, 0, false, true),
				// Mining fatigue
				new PotionEffect(4, 300, 1, false, true),
				// Instant dmg
				new PotionEffect(7, 1, 2, false, true),
				// Nausea
				new PotionEffect(9, 300, 0, false, true),
				// Blindness
				new PotionEffect(15, 100, 0, false, true),
				// Hunger
				new PotionEffect(17, 100, 10, false, true),
				// Weakness
				new PotionEffect(18, 100, 2, false, true),
				// Poison
				new PotionEffect(19, 100, 2, false, true),
				// Wither
				new PotionEffect(20, 100, 2, false, true) };
	}

	@Override
	public void onLivingUpdate()
	{
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
		if (damage > MAX_DAMAGE_IN_1_HIT)
		{
			damage = MAX_DAMAGE_IN_1_HIT;
		}

		long timeBetweenHits = System.currentTimeMillis() - this.getEntityData().getLong(LAST_HIT);

		if (timeBetweenHits < 3000)
		{
			if (timeBetweenHits > 2000)
			{
				damage = damage * 0.75f;
			}
			else if (timeBetweenHits > 1000)
			{
				damage = damage * 0.35f;
			}
			else
			{
				damage = 1.0f;
			}
		}

		if (damageSource instanceof EntityDamageSource)
		{
			if (((EntityDamageSource) damageSource).damageType.equals("silverDamage"))
			{
				return super.attackEntityFrom(damageSource, damage);
			}
		}

		this.getEntityData().setLong(LAST_HIT, System.currentTimeMillis());

		return super.attackEntityFrom(DamageSource.generic, 1);
	}

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return animHandler;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entityLivingBase, float damage)
	{
		if (!this.worldObj.isRemote)
		{
			switch (random.nextInt(3))
			{
				case 0:
				{
					this.randomTeleport();
					break;
				}
				case 1:
				{
					this.attackSummonWerewolves();
					break;
				}
				case 2:
				{
					this.attackAOEPotion();
					break;
				}
			}
		}
	}

	private void attackSummonWerewolves()
	{
		int numberOfWWsSpawned = 0;

		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			BlockPos current = this.getPosition().offset(facing);
			IBlockState block = this.worldObj.getBlockState(current);
			if (block.getBlock() instanceof BlockAir)
			{
				EntityWerewolf werewolf = new EntityWerewolf(this.worldObj);
				werewolf.setPosition(current.getX(), current.getY(), current.getZ());
				this.worldObj.spawnEntityInWorld(werewolf);

				numberOfWWsSpawned = numberOfWWsSpawned + 1;
			}
		}

		if (numberOfWWsSpawned == 0)
		{
			this.randomTeleport();
		}
	}

	private void attackAOEPotion()
	{
		for (Object object : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(POTION_POISON_RANGE, POTION_POISON_RANGE, POTION_POISON_RANGE)))
		{
			if (object instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) object;

				Collections.shuffle(Arrays.asList(this.possibleEffects));

				// 4 random potion effects
				entityPlayer.addPotionEffect(this.possibleEffects[0]);
				entityPlayer.addPotionEffect(this.possibleEffects[1]);
				entityPlayer.addPotionEffect(this.possibleEffects[2]);
				entityPlayer.addPotionEffect(this.possibleEffects[3]);
			}
		}
	}

	private void randomTeleport()
	{
		int counter = 200;
		while (counter > 0)
		{
			int x = (int) this.posX;
			int y = (int) this.posY;
			int z = (int) this.posZ;

			x = (int) (x + (random.nextDouble() - 0.5) * TELEPORT_RANGE);
			z = (int) (z + (random.nextDouble() - 0.5) * TELEPORT_RANGE);

			BlockPos newPosition = new BlockPos(x, y, z);

			if (this.worldObj.getBlockState(newPosition).getBlock() instanceof BlockAir)
			{
				this.setPosition(x, y, z);
				this.addPotionEffect(new PotionEffect(14, 40, 0, false, false));

				List entityList = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(MAX_KNOCKBACK_RANGE, MAX_KNOCKBACK_RANGE, MAX_KNOCKBACK_RANGE));
				for (Object entityObject : entityList)
				{
					if (entityObject instanceof EntityPlayer)
					{
						EntityPlayer entityPlayer = (EntityPlayer) entityObject;

						double motionX = this.getPosition().getX() - entityPlayer.getPosition().getX();
						double motionZ = this.getPosition().getZ() - entityPlayer.getPosition().getZ();

						double hypotenuse = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);

						entityPlayer.addVelocity(-motionX * KNOCKBACK_POWER * 0.6000000238418579D / hypotenuse, 0.1D, -motionZ * KNOCKBACK_POWER * 0.6000000238418579D / hypotenuse);
						entityPlayer.velocityChanged = true;
					}
				}

				break;
			}

			counter = counter - 1;
		}
	}
}
