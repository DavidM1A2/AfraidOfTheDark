package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.OverworldHeightSavedData;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItem;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.WorldHeightMapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * Item that allows for modding debug, does nothing else
 */
public class ItemDebug extends AOTDItem
{
	/**
	 * Constructor sets up item properties
	 */
	public ItemDebug()
	{
		super("debug");
		this.setMaxStackSize(1);
	}

	///
	/// Code below here is not documented due to its temporary nature used for testing
	///


	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if (!worldIn.isRemote)
		{
			OverworldHeightSavedData x = OverworldHeightSavedData.get(worldIn);
			if (x != null)
			{
				BlockPos position = playerIn.getPosition();
				if (x.heightKnown(new ChunkPos(position.getX() >> 4, position.getZ() >> 4)))
				{
					playerIn.sendMessage(new TextComponentString("Low height is: " + x.getLowestHeight(new ChunkPos(position.getX() >> 4, position.getZ() >> 4))));
					playerIn.sendMessage(new TextComponentString("High height is: " + x.getHighestHeight(new ChunkPos(position.getX() >> 4, position.getZ() >> 4))));
				}
				else
				{
					playerIn.sendMessage(new TextComponentString("Height unknown..."));
				}
			}
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
