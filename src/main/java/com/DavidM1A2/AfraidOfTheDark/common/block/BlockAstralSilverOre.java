/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAstralSilverOre extends AOTDBlock
{
	public BlockAstralSilverOre()
	{
		super(Material.ROCK);
		this.setUnlocalizedName("astral_silver_ore");
		this.setRegistryName("astral_silver_ore");
		this.setHardness(10.0F);
		this.setResistance(50.0F);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityPlayer, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity, ItemStack stack)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.AstralSilver))
		{
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.AstralSilver, true);
		}
		super.harvestBlock(world, entityPlayer, blockPos, iBlockState, tileEntity, stack);
	}
}
