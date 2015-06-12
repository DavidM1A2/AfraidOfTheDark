package com.DavidM1A2.AfraidOfTheDark.common.debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.item.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

public class ItemWorldGenTest extends AOTDItem
{
	public ItemWorldGenTest()
	{
		super();
		this.setUnlocalizedName("worldGenTest");
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (!world.isRemote)
		{
			int adjustedX = entityPlayer.getPosition().getX() - Constants.AOTDSchematics.treeBranchyType2.getWidth() / 2;
			int adjustedZ = entityPlayer.getPosition().getZ() - Constants.AOTDSchematics.treeBranchyType2.getHeight() / 2;
			SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeBranchyType2, world, adjustedX, Utility.getPlaceToSpawnLowest(world, adjustedX + 2, adjustedZ - 7, 5, 5) - 3, adjustedZ, null, 0);
		}
		return super.onItemRightClick(itemStack, world, entityPlayer);
	}
}
