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
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncParticleFX;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDParticleFXTypes;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EnariaAttacks
{
	private final EntityEnaria enaria;
	private final Random random;
	private static final int POTION_POISON_RANGE = 20;
	private static final int DARKNESS_RANGE = 30;
	private static final int TELEPORT_RANGE = 20;
	private static final double KNOCKBACK_POWER = 5;
	private static final int MAX_KNOCKBACK_RANGE = 10;
	private static final int BASIC_RANGE = 20;
	private static final int NUMBER_OF_PARTICLES_PER_ATTACK = 30;
	private static final int NUMBER_OF_PARTICLES_PER_TELEPORT = 30;
	private static final int TELEPORT_ATTEMPTS = 200;
	private final PotionEffect[] possibleEffects;

	public EnariaAttacks(EntityEnaria enaria, Random random)
	{
		this.enaria = enaria;
		this.random = random;

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

	public void performBasicAttack()
	{
		for (Object object : this.enaria.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getEntityBoundingBox().expand(BASIC_RANGE, BASIC_RANGE, BASIC_RANGE)))
		{
			if (object instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) object;
				entityPlayer.attackEntityFrom(EntityDamageSource.causeMobDamage(this.enaria), 10);
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

	public void performRandomAttack()
	{
		switch (this.random.nextInt(5))
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
			case 3:
			{
				this.attackShuffleInventory();
				break;
			}
			case 4:
			{
				this.attackDarkness();
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
				if (entityPlayer.isPotionActive(16))
				{
					entityPlayer.removePotionEffect(16);
				}
				// Add blindness
				entityPlayer.addPotionEffect(new PotionEffect(15, 260, 0, false, false));
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
			for (Object object : this.enaria.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getEntityBoundingBox().expand(POTION_POISON_RANGE, POTION_POISON_RANGE, POTION_POISON_RANGE)))
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
						Collections.shuffle(Arrays.asList(this.possibleEffects));

						// 3 random potion effects
						entityPlayer.addPotionEffect(this.possibleEffects[0]);
						entityPlayer.addPotionEffect(this.possibleEffects[1]);
						entityPlayer.addPotionEffect(this.possibleEffects[2]);
					}
				}
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

			int counter = TELEPORT_ATTEMPTS;
			while (counter > 0)
			{
				int x = (int) this.enaria.getPosition().getX();
				int y = (int) this.enaria.getPosition().getY();
				int z = (int) this.enaria.getPosition().getZ();

				x = (int) (x + (this.random.nextDouble() - 0.5) * TELEPORT_RANGE);
				y = y + (counter < TELEPORT_ATTEMPTS / 3 ? 0 : counter < TELEPORT_ATTEMPTS / 2 ? 1 : 2);
				z = (int) (z + (this.random.nextDouble() - 0.5) * TELEPORT_RANGE);

				BlockPos location = new BlockPos(x, y, z);
				if (this.enaria.worldObj.isAirBlock(location) && !this.enaria.worldObj.isAirBlock(location.add(0, -1, 0)) && this.enaria.worldObj.isAirBlock(location.add(0, 1, 0)))
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

					return;
				}

				counter = counter - 1;
			}
		}
	}
}
