package com.DavidM1A2.AfraidOfTheDark.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

public abstract class AOTDBlockTileEntity extends BlockContainer
{
	protected AOTDBlockTileEntity(Material material)
	{
		super(material);
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Refrence.MOD_ID.toLowerCase() + ":", this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// Format for a block is: tile.modid:blockname.name
	}

	// Get the unlocalized name
	protected String getUnwrappedUnlocalizedName(final String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
