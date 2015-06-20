/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class BlockSunstoneOre extends AOTDBlock
{
	public BlockSunstoneOre(final Material material)
	{
		super(material);
		this.setUnlocalizedName("sunstoneOre");
		this.setLightLevel(1.0f);
		this.setHardness(10.0F);
		this.setResistance(50.0F);
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 *
	 * @param fortune
	 *            the level of the Fortune enchantment on the player's tool
	 */
	@Override
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune)
	{
		return ModItems.sunstoneFragment;
	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityPlayer, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity)
	{
		if (LoadResearchData.canResearch(entityPlayer, ResearchTypes.Igneous))
		{
			LoadResearchData.unlockResearchSynced(entityPlayer, ResearchTypes.Igneous, Side.SERVER, true);
		}
		super.harvestBlock(world, entityPlayer, blockPos, iBlockState, tileEntity);
	}
}
