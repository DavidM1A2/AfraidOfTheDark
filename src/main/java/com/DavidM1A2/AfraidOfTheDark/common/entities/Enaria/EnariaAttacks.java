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

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncParticleFX;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDParticleFXTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EnariaAttacks
{
	private final EntityEnaria enaria;
	private final Random random;
	private static final int POTION_POISON_RANGE = 20;
	private static final int DARKNESS_RANGE = 100;
	private static final int TELEPORT_RANGE = 20;
	private static final double KNOCKBACK_POWER = 5;
	private static final int MAX_KNOCKBACK_RANGE = 10;
	private static final int TELEPORT_PLAYER_RANGE = 80;
	private static final int BASIC_RANGE = 20;
	private static final int NUMBER_OF_PARTICLES_PER_ATTACK = 30;
	private static final int NUMBER_OF_PARTICLES_PER_TELEPORT = 30;
	private static final int RAY_TELEPORT_RANGE = 120;
	private static final int SHUFFLE_INVENTORY_RANGE = 30;
	private static final int SIGHT_RANGE = 130;
	private final PotionEffect[] possibleEffects;

	public EnariaAttacks(EntityEnaria enaria, Random random)
	{
		this.enaria = enaria;
		this.random = random;

		possibleEffects = new PotionEffect[]
		{
				// Slowness
				new PotionEffect(Potion.moveSlowdown.getId(), 300, 0, false, true),
				// Mining fatigue
				new PotionEffect(Potion.digSlowdown.getId(), 300, 1, false, true),
				// Instant dmg
				new PotionEffect(Potion.harm.getId(), 1, 2, false, true),
				// Nausea
				new PotionEffect(Potion.confusion.getId(), 350, 0, false, true),
				// Blindness
				new PotionEffect(Potion.blindness.getId(), 100, 0, false, true),
				// Hunger
				new PotionEffect(Potion.hunger.getId(), 100, 10, false, true),
				// Weakness
				new PotionEffect(Potion.weakness.getId(), 100, 4, false, true),
				// Poison
				new PotionEffect(Potion.poison.getId(), 100, 3, false, true),
				// Wither
				new PotionEffect(Potion.wither.getId(), 100, 2, false, true) };
	}

	public void performBasicAttack()
	{
		for (Object object : this.enaria.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getEntityBoundingBox().expand(BASIC_RANGE, BASIC_RANGE, BASIC_RANGE)))
		{
			if (object instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) object;
				entityPlayer.attackEntityFrom(EntityDamageSource.causeMobDamage(this.enaria), (float) this.enaria.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
				this.performBasicAttackParticleEffectTo(entityPlayer);
			}
		}
	}

	private void performBasicAttackParticleEffectTo(EntityPlayer entityPlayer)
	{
		for (int i = 0; i < NUMBER_OF_PARTICLES_PER_ATTACK; i++)
		{
			int x = (int) Math.round(this.enaria.posX + (entityPlayer.posX - this.enaria.posX) * i / NUMBER_OF_PARTICLES_PER_ATTACK);
			int y = 1 + (int) Math.round(this.enaria.posY + (entityPlayer.posY - this.enaria.posY) * i / NUMBER_OF_PARTICLES_PER_ATTACK);
			int z = (int) Math.round(this.enaria.posZ + (entityPlayer.posZ - this.enaria.posZ) * i / NUMBER_OF_PARTICLES_PER_ATTACK);

			if (!entityPlayer.worldObj.isRemote)
			{
				AfraidOfTheDark.getPacketHandler().sendToAllAround(new SyncParticleFX(AOTDParticleFXTypes.EnariaBasicAttack, x, y, z), new TargetPoint(entityPlayer.dimension, x, y, z, 40));
			}
		}
	}

	public void randomTeleport()
	{
		if (!this.enaria.worldObj.isRemote)
		{
			for (int i = 0; i < NUMBER_OF_PARTICLES_PER_TELEPORT; i++)
			{
				AfraidOfTheDark.getPacketHandler().sendToAllAround(new SyncParticleFX(AOTDParticleFXTypes.EnariaTeleport, this.enaria.getPosition().getX() + Math.random(), this.enaria.getPosition().getY() + .7 + Math.random(), this.enaria.getPosition().getZ() + Math.random()), new TargetPoint(
						this.enaria.dimension, this.enaria.getPosition().getX() + Math.random(), this.enaria.getPosition().getY() + .7 + Math.random(), this.enaria.getPosition().getZ() + Math.random(), 40));
			}

			MovingObjectPosition hit = this.enaria.rayTrace(RAY_TELEPORT_RANGE, 1.0F);

			switch (hit.typeOfHit)
			{
				case BLOCK:
					BlockPos hitPos = new BlockPos(hit.hitVec);
					if (this.enaria.worldObj.isAirBlock(hitPos) && this.enaria.worldObj.isAirBlock(hitPos.up()))
					{
						teleportWithShockwave(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord);
					}
					else
					{
						hitPos = hitPos.offset(hit.sideHit);
						teleportWithShockwave(hitPos.getX() + 0.5, hitPos.getY(), hitPos.getZ() + 0.5);
					}
					break;
				case ENTITY:
					if (hit.entityHit != null)
					{
						teleportWithShockwave(hit.entityHit.posX, hit.entityHit.posY, hit.entityHit.posZ);
					}
					break;
				case MISS:
					break;
			}
		}
	}

	private void teleportWithShockwave(double x, double y, double z)
	{
		this.enaria.setPosition(x, y, z);
		this.enaria.addPotionEffect(new PotionEffect(14, 40, 0, false, false));

		List entityList = this.enaria.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getEntityBoundingBox().expand(MAX_KNOCKBACK_RANGE, MAX_KNOCKBACK_RANGE, MAX_KNOCKBACK_RANGE));
		for (Object entityObject : entityList)
		{
			if (entityObject instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entityObject;

				double motionX = this.enaria.getPosition().getX() - entityPlayer.getPosition().getX();
				double motionZ = this.enaria.getPosition().getZ() - entityPlayer.getPosition().getZ();

				double hypotenuse = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);

				entityPlayer.addVelocity(-motionX * KNOCKBACK_POWER * 0.6000000238418579D / hypotenuse, 0.1D, -motionZ * KNOCKBACK_POWER * 0.6000000238418579D / hypotenuse);
				entityPlayer.velocityChanged = true;
			}
		}
	}

	public void performRandomAttack()
	{
		switch (this.random.nextInt(7))
		{
			case 0:
			{
				this.attackSummonWerewolves();
				break;
			}
			case 1:
			{
				this.attackAOEPotion();
				break;
			}
			case 2:
			{
				this.attackShuffleInventory();
				break;
			}
			case 3:
			{
				this.attackSummonEnchantedSkeletons();
				break;
			}
			case 4:
			{
				this.teleportToPlayer();
				break;
			}
			case 5:
			{
				this.attackDarkness();
				break;
			}
			case 6:
			{
				this.attackSummonSplinterDrones();
				break;
			}
		}
	}

	private void attackDarkness()
	{
		for (Object object : this.enaria.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getEntityBoundingBox().expand(DARKNESS_RANGE, DARKNESS_RANGE, DARKNESS_RANGE)))
		{
			if (object instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) object;

				this.enaria.worldObj.playSoundAtEntity(entityPlayer, "mob.wither.hurt", 1.0f, 0.5f);

				// remove night vision
				if (entityPlayer.isPotionActive(Potion.nightVision.getId()))
				{
					entityPlayer.removePotionEffect(Potion.nightVision.getId());
				}
				// Add blindness
				entityPlayer.addPotionEffect(new PotionEffect(Potion.blindness.getId(), 260, 0, false, false));
			}
		}

		for (int i = this.enaria.getPosition().getX() - 5; i < this.enaria.getPosition().getX() + 5; i++)
		{
			for (int j = this.enaria.getPosition().getY() - 5; j < this.enaria.getPosition().getY() + 5; j++)
			{
				for (int k = this.enaria.getPosition().getZ() - 5; k < this.enaria.getPosition().getZ() + 5; k++)
				{
					BlockPos current = new BlockPos(i, j, k);
					if (this.enaria.worldObj.getBlockState(current).getBlock() instanceof BlockTorch)
					{
						this.enaria.worldObj.setBlockToAir(current);
					}
				}
			}
		}
	}

	public void attackShuffleInventory()
	{
		if (!this.enaria.worldObj.isRemote)
		{
			for (Object object : this.enaria.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getEntityBoundingBox().expand(SHUFFLE_INVENTORY_RANGE, SHUFFLE_INVENTORY_RANGE, SHUFFLE_INVENTORY_RANGE)))
			{
				if (object instanceof EntityPlayer)
				{
					EntityPlayer entityPlayer = (EntityPlayer) object;

					if (!entityPlayer.capabilities.isCreativeMode)
					{
						Collections.shuffle(Arrays.asList(entityPlayer.inventory.mainInventory));
					}
				}
			}
		}
	}

	public void attackSummonWerewolves()
	{
		if (!this.enaria.worldObj.isRemote)
		{
			int numberOfWWsSpawned = 0;
			int maxWWsSpawned = this.random.nextInt(3) + 2;

			for (EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				if (numberOfWWsSpawned < maxWWsSpawned)
				{
					BlockPos current = this.enaria.getPosition().offset(facing);
					IBlockState block = this.enaria.worldObj.getBlockState(current);
					if (block.getBlock() instanceof BlockAir)
					{
						EntityWerewolf werewolf = new EntityWerewolf(this.enaria.worldObj);
						werewolf.setPosition(current.getX(), current.getY(), current.getZ());
						werewolf.setCanAttackAnyone(true);
						this.enaria.worldObj.spawnEntityInWorld(werewolf);

						numberOfWWsSpawned = numberOfWWsSpawned + 1;
					}
				}
			}

			if (numberOfWWsSpawned == 0)
			{
				this.randomTeleport();
			}
		}
	}

	public void attackSummonSplinterDrones()
	{
		if (!this.enaria.worldObj.isRemote)
		{
			int numberOfSplintersSpawned = 0;
			int maxSplintersSpawned = this.random.nextInt(2) + 1;

			for (EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				if (numberOfSplintersSpawned < maxSplintersSpawned)
				{
					BlockPos current = this.enaria.getPosition().offset(facing).up();
					IBlockState block = this.enaria.worldObj.getBlockState(current);
					if (block.getBlock() instanceof BlockAir)
					{
						EntitySplinterDrone splinterDrone = new EntitySplinterDrone(this.enaria.worldObj);
						splinterDrone.setPosition(current.getX(), current.getY(), current.getZ());
						this.enaria.worldObj.spawnEntityInWorld(splinterDrone);

						numberOfSplintersSpawned = numberOfSplintersSpawned + 1;
					}
				}
			}

			if (numberOfSplintersSpawned == 0)
			{
				this.randomTeleport();
			}
		}
	}

	public void attackSummonEnchantedSkeletons()
	{
		if (!this.enaria.worldObj.isRemote)
		{
			int numberOfSkeletonsSpawned = 0;
			int maxSkeletonsSpawned = this.random.nextInt(6) + 4;

			for (EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				if (numberOfSkeletonsSpawned < maxSkeletonsSpawned)
				{
					BlockPos current = this.enaria.getPosition().offset(facing).up();
					IBlockState block = this.enaria.worldObj.getBlockState(current);
					if (block.getBlock() instanceof BlockAir)
					{
						EntitySplinterDrone splinterDrone = new EntitySplinterDrone(this.enaria.worldObj);
						splinterDrone.setPosition(current.getX(), current.getY(), current.getZ());
						this.enaria.worldObj.spawnEntityInWorld(splinterDrone);
						EntitySplinterDrone splinterDrone2 = new EntitySplinterDrone(this.enaria.worldObj);
						splinterDrone2.setPosition(current.getX(), current.getY(), current.getZ());
						this.enaria.worldObj.spawnEntityInWorld(splinterDrone2);

						numberOfSkeletonsSpawned = numberOfSkeletonsSpawned + 2;
					}
				}
			}

			if (numberOfSkeletonsSpawned == 0)
			{
				this.randomTeleport();
			}
		}
	}

	public void attackAOEPotion()
	{
		if (!this.enaria.worldObj.isRemote)
		{
			for (Object object : this.enaria.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getEntityBoundingBox().expand(POTION_POISON_RANGE, POTION_POISON_RANGE, POTION_POISON_RANGE)))
			{
				if (object instanceof EntityPlayer)
				{
					EntityPlayer entityPlayer = (EntityPlayer) object;

					if (!entityPlayer.capabilities.isCreativeMode)
					{
						Collections.shuffle(Arrays.asList(this.possibleEffects), this.enaria.getRNG());

						// 3 random potion effects
						entityPlayer.addPotionEffect(this.possibleEffects[0]);
						entityPlayer.addPotionEffect(this.possibleEffects[1]);
						entityPlayer.addPotionEffect(this.possibleEffects[2]);
					}
				}
			}
		}
	}

	private void teleportToPlayer()
	{
		List entityList = this.enaria.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getEntityBoundingBox().expand(TELEPORT_PLAYER_RANGE, TELEPORT_PLAYER_RANGE, TELEPORT_PLAYER_RANGE));
		for (Object entityObject : entityList)
		{
			if (entityObject instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entityObject;

				entityPlayer.setHealth(entityPlayer.getHealth() - 6.0f);

				this.enaria.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);

				this.enaria.addPotionEffect(new PotionEffect(Potion.resistance.getId(), 20, 99, false, false));

				entityPlayer.addPotionEffect(new PotionEffect(Potion.blindness.getId(), 30, 0, false, false));

				// Server side
				if (this.enaria.worldObj instanceof WorldServer)
				{
					Utility.createExplosionWithoutBlockDamageServer(this.enaria.worldObj, entityPlayer, this.enaria.posX, this.enaria.posY, this.enaria.posZ, 7, false, true);
				}
				else
				{
					Utility.createExplosionWithoutBlockDamageClient(this.enaria.worldObj, entityPlayer, this.enaria.posX, this.enaria.posY, this.enaria.posZ, 7, false, true);
				}

				return;
			}
		}
	}
}