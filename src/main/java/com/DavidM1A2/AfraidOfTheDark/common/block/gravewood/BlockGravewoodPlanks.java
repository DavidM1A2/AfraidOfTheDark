package com.DavidM1A2.afraidofthedark.common.block.gravewood;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDBlock;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Class representing gravewood planks
 */
public class BlockGravewoodPlanks extends AOTDBlock
{
	/**
	 * Constructor for gravewood planks sets the planks properties
	 */
	public BlockGravewoodPlanks()
	{
		super("gravewood_planks", Material.WOOD);
		this.setHardness(2.0f);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(Constants.AOTD_CREATIVE_TAB);
	}
}
