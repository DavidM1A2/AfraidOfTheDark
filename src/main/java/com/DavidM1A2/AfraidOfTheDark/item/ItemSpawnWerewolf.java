package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSpawnWerewolf extends ItemBase
{
	@SideOnly(Side.CLIENT)
	private IIcon theIcon;
	protected String entityToSpawnName = "";
	protected String entityToSpawnNameFull = "";
	protected EntityLiving entityToSpawn = null;

	public ItemSpawnWerewolf(String entityToSpawnName)
	{
		super();
		this.setUnlocalizedName("spawnWerewolf");
		this.setHasSubtypes(false);
		this.maxStackSize = 64;
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
		setEntityToSpawnName(entityToSpawnName);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int blockX, int blockY, int blockZ, int blockOffset, float par8, float par9, float par10)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			Block block = world.getBlock(blockX, blockY, blockZ);
			blockX = blockX + Facing.offsetsXForSide[blockOffset];
			blockY = blockY + Facing.offsetsYForSide[blockOffset];
			blockZ = blockZ + Facing.offsetsZForSide[blockOffset];

			double d0 = 0.0D;

			if (blockOffset == 1 && block.getRenderType() == 11)
			{
				d0 = 0.5D;
			}

			Entity entity = spawnEntity(world, blockX + 0.5D, blockY + d0, blockZ + 0.5D);

			if (entity != null)
			{
				if (entity instanceof EntityLivingBase && itemStack.hasDisplayName())
				{
					((EntityLiving) entity).setCustomNameTag(itemStack.getDisplayName());
				}

				if (!entityPlayer.capabilities.isCreativeMode)
				{
					itemStack.stackSize = itemStack.stackSize - 1;
				}
			}

			return true;
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (world.isRemote)
		{
			return itemStack;
		}
		else
		{
			MovingObjectPosition movingObjectPosition = getMovingObjectPositionFromPlayer(world, entityPlayer, true);

			if (movingObjectPosition == null)
			{
				return itemStack;
			}
			else
			{
				if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				{
					int x = movingObjectPosition.blockX;
					int y = movingObjectPosition.blockY;
					int z = movingObjectPosition.blockZ;

					if (!world.canMineBlock(entityPlayer, x, y, z))
					{
						return itemStack;
					}

					if (!entityPlayer.canPlayerEdit(x, y, z, movingObjectPosition.sideHit, itemStack))
					{
						return itemStack;
					}

					if (world.getBlock(x, y, z) instanceof BlockLiquid)
					{
						Entity entity = spawnEntity(world, x, y, z);

						if (entity != null)
						{
							if (entity instanceof EntityLivingBase && itemStack.hasDisplayName())
							{
								((EntityLiving) entity).setCustomNameTag(itemStack.getDisplayName());
							}

							if (!entityPlayer.capabilities.isCreativeMode)
							{
								itemStack.stackSize = itemStack.stackSize - 1;
							}
						}
					}
				}
				return itemStack;
			}
		}
	}

	public void setEntityToSpawnName(String parEntityToSpawnName)
	{
		entityToSpawnName = parEntityToSpawnName;
		entityToSpawnNameFull = Refrence.MOD_ID + "." + entityToSpawnName;
	}

	public Entity spawnEntity(World world, double x, double y, double z)
	{
		if (!world.isRemote)
		{
			entityToSpawnNameFull = Refrence.MOD_ID + "." + entityToSpawnName;

			if (EntityList.stringToClassMapping.containsKey(entityToSpawnNameFull))
			{
				entityToSpawn = (EntityLiving) EntityList.createEntityByName(entityToSpawnNameFull, world);
				entityToSpawn.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360), 0.0F);
				world.spawnEntityInWorld(entityToSpawn);
				entityToSpawn.onSpawnWithEgg((IEntityLivingData) null);
				entityToSpawn.playLivingSound();
			}
		}

		return entityToSpawn;
	}
}
