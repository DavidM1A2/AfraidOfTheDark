/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGravewoodLeaves extends BlockLeavesBase
{
	/*
	 * Define gravewood leaves block
	 */
	public BlockGravewoodLeaves()
	{
		super();
		this.setBlockName("gravewoodLeaves");
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderColor(int p_149741_1_)
	{
		return ColorizerFoliage.getFoliageColorPine();
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (Minecraft.getMinecraft().isFancyGraphicsEnabled())
		{
			return icons[1];
		}
		else
		{
			return icons[0];
		}
	}

	// This function is used by default MC but not here. Just replace gravewood
	// with the name of your wood.
	@Override
	public String[] func_150125_e()
	{
		return new String[]
		{ "gravewood" };
	}

	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given coordinates. Args: blockAccess, x,
	 * y, z, side
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int side)
	{
		Block block = iBlockAccess.getBlock(x, y, z);
		// In fast mode we only render visible faces
		if (!block.isOpaqueCube() && !Minecraft.getMinecraft().isFancyGraphicsEnabled())
		{
			return true;
		}
		// In fancy we render all faces
		if (Minecraft.getMinecraft().isFancyGraphicsEnabled())
		{
			return true;
		}
		// If it is none of the above don't render it.
		else
		{
			return false;
		}
	}
}
