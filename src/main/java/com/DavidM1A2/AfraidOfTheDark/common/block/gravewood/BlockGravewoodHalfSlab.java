package com.DavidM1A2.afraidofthedark.common.block.gravewood;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDSlab;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;

/**
 * Class representing a gravewood half slab
 */
public class BlockGravewoodHalfSlab extends AOTDSlab
{
	/**
	 * Constructor sets the name and material
	 */
	public BlockGravewoodHalfSlab()
	{
		super("gravewood_half_slab", Material.WOOD);
	}

	/**
	 * @return It's a half slab, so not double
	 */
	@Override
	public boolean isDouble()
	{
		return false;
	}

	/**
	 * @return The opposite is a double slab since this one is a half slab
	 */
	@Override
	public BlockSlab getOpposite()
	{
		return (BlockSlab) ModBlocks.GRAVEWOOD_DOUBLE_SLAB;
	}
}
