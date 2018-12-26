package com.DavidM1A2.AfraidOfTheDark.common.block;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlockTileEntity;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityEnariaSpawner;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityGhastlyEnariaSpawner;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEnariaSpawner extends AOTDBlockTileEntity
{
	public BlockEnariaSpawner()
	{
		super(Material.ROCK);
		this.setUnlocalizedName("enariaSpawner");
		this.setRegistryName("enariaSpawner");
		this.setHardness(10.0F);
		this.setResistance(50.0F);
		this.setHarvestLevel("pickaxe", 3);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		if (world.provider.getDimension() == AOTDDimensions.Nightmare.getWorldID())
			return new TileEntityGhastlyEnariaSpawner();
		else
			return new TileEntityEnariaSpawner();
	}

	@Override
	protected boolean displayInCreative()
	{
		return false;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}
}
