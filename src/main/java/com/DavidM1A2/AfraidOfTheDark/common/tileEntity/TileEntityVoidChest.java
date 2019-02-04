package com.DavidM1A2.afraidofthedark.common.tileEntity;

import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.DavidM1A2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity;
import net.minecraft.block.Block;

/**
 * Class that represents the void chest tile entity logic
 */
public class TileEntityVoidChest extends AOTDTickingTileEntity
{
	/**
	 * Constructor initializes the tile entity fields
	 */
	public TileEntityVoidChest()
	{
		super(ModBlocks.VOID_CHEST);
	}
}
