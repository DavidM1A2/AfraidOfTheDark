package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;
import com.DavidM1A2.AfraidOfTheDark.common.utility.WorldGenerationUtility;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Dig extends Effect
{
	@Override
	public int getCost()
	{
		return 14;
	}

	@Override
	public void performEffect(BlockPos location, World world, double radius)
	{
		int blockRadius = (int) Math.floor(radius) / 2;
		if (blockRadius < 0)
			blockRadius = 0;
		for (int x = -blockRadius; x < blockRadius + 1; x++)
			for (int y = -blockRadius; y < blockRadius + 1; y++)
				for (int z = -blockRadius; z < blockRadius + 1; z++)
					if (world.getTileEntity(location.add(x, y, z)) == null)
						this.destroyBlock(world, location.add(x, y, z), true);
		VitaeUtils.vitaeReleasedFX(world, location, radius, (int) (radius * 5));
	}

	@Override
	public void performEffect(Entity entity)
	{
		if (entity.worldObj.destroyBlock(entity.getPosition().down(), true))
			VitaeUtils.vitaeReleasedFX(entity.worldObj, entity.getPosition().down(), .2, 1);
	}

	/**
	 * Sets a block to air, but also plays the sound and particles and can spawn drops
	 */
	public boolean destroyBlock(World world, BlockPos pos, boolean dropBlock)
	{
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (block.getMaterial() == Material.air)
		{
			return false;
		}
		else
		{
			world.playAuxSFX(2001, pos, Block.getStateId(iblockstate));

			if (dropBlock)
			{
				block.dropBlockAsItem(world, pos, iblockstate, 0);
			}

			return WorldGenerationUtility.setBlockStateFast(world, pos, Blocks.air.getDefaultState(), 3);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	public Effects getType()
	{
		return Effects.Dig;
	}

}
