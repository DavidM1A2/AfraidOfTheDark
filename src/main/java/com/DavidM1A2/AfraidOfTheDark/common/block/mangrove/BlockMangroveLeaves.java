/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.mangrove;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDLeaves;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMangroveLeaves extends AOTDLeaves
{
	/*
	 * Define mangrove leaves block
	 */
	public BlockMangroveLeaves()
	{
		super();
		this.setUnlocalizedName("mangroveLeaves");
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.mangroveSapling);
	}

	// Leaf render color
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(final IBlockState state)
	{
		return 0x409c4e;
	}

	// What color to multiply these leaves by
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass)
	{
		return 0x409c4e;
	}
}