package com.DavidM1A2.afraidofthedark.common.block.core;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Base class for any simple blocks used by Afraid of the dark
 */
public abstract class AOTDBlock extends Block
{
	/**
	 * Constructor requires a material parameter that defines some block properties
	 *
	 * @param baseName The name of the block to be used by the game registry
	 * @param material The material of this block
	 */
	public AOTDBlock(String baseName, Material material)
	{
		super(material);
		this.setUnlocalizedName(Constants.MOD_ID + ":" + baseName);
		this.setRegistryName(Constants.MOD_ID + ":" + baseName);
		if (this.displayInCreative())
			this.setCreativeTab(Constants.AOTD_CREATIVE_TAB);
	}

	/**
	 * @return True if this block should be displayed in creative mode, false otherwise
	 */
	protected boolean displayInCreative()
	{
		return true;
	}
}
