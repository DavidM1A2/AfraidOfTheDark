package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellHitInfo;
import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Freeze extends Effect
{
	@Override
	public int getCost()
	{
		return 16;
	}

	@Override
	public void performEffect(SpellHitInfo hitInfo)
	{
		if (hitInfo.getEntityHit() == null)
			this.encaseLocationInIce(hitInfo.getWorld(), hitInfo.getLocation(), hitInfo.getRadius());
		else
		{
			if (hitInfo.getEntityHit() instanceof EntityLivingBase)
			{
				// Slowness
				((EntityLivingBase) hitInfo.getEntityHit()).addPotionEffect(new PotionEffect(Potion.getPotionById(2), 40, 3, false, false));
			}
			VitaeUtils.vitaeReleasedFX(hitInfo.getWorld(), hitInfo.getLocation(), 1, 5);
			Entity entity = hitInfo.getEntityHit();
			double radius = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX;
			radius = Math.max(radius, entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY);
			radius = Math.max(radius, entity.getEntityBoundingBox().maxZ - entity.getEntityBoundingBox().minZ);
			this.encaseLocationInIce(hitInfo.getWorld(), hitInfo.getLocation(), radius + 1);
		}
	}

	private void encaseLocationInIce(World world, BlockPos location, double radius)
	{
		int blockRadius = (int) Math.ceil(radius);
		double threshhold = 0.5;
		for (int x = -blockRadius; x < blockRadius + 1; x++)
			for (int y = -blockRadius; y < blockRadius + 1; y++)
				for (int z = -blockRadius; z < blockRadius + 1; z++)
				{
					BlockPos currentLoc = location.add(x, y, z);
					Block current = world.getBlockState(currentLoc).getBlock();
					double distance = Math.sqrt(location.distanceSqToCenter(currentLoc.getX(), currentLoc.getY(), currentLoc.getZ()));
					if (distance < radius + threshhold && distance > radius - threshhold)
					{
						if (current instanceof BlockAir)
						{
							world.setBlockState(currentLoc, Blocks.ICE.getDefaultState());
							VitaeUtils.vitaeReleasedFX(world, currentLoc, 1, 1);
						}
					}
				}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	public Effects getType()
	{
		return Effects.Freeze;
	}
}
