package com.DavidM1A2.afraidofthedark.common.block;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDBlock;
import net.minecraft.block.material.Material;

/**
 * Class that represents a meteor block
 */
public class BlockMeteor extends AOTDBlock
{
	/**
	 * Constructor initializes the block's properties
	 */
	public BlockMeteor()
	{
		super("meteor", Material.ROCK);
		this.setHardness(10.0f);
		this.setResistance(50.0f);
		this.setHarvestLevel("pickaxe", 2);
	}
}
