/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.debug;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.utility.UnsupportedLocationException;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

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
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		//if (!world.isRemote)
		{
			int adjustedX = entityPlayer.getPosition().getX() - Constants.AOTDSchematics.treeBranchyType2.getWidth() / 2;
			int adjustedZ = entityPlayer.getPosition().getZ() - Constants.AOTDSchematics.treeBranchyType2.getHeight() / 2;

			try
			{
				int y = Utility.getPlaceToSpawnLowest(world, adjustedX + Constants.AOTDSchematics.treeBranchyType2.getWidth() / 2 + 2, adjustedZ + Constants.AOTDSchematics.treeBranchyType2.getHeight() / 2 - 7, 5, 5);
				entityPlayer.addChatMessage(new ChatComponentText("Placed... may take a few seconds to load."));
				SchematicGenerator.generateSchematic(Constants.AOTDSchematics.treeLargeDonut, world, adjustedX, y - 3, adjustedZ);
			}
			catch (UnsupportedLocationException e)
			{
				entityPlayer.addChatMessage(new ChatComponentText("Can't place that here"));
			}

		}
		return super.onItemRightClick(itemStack, world, entityPlayer);
	}
}
