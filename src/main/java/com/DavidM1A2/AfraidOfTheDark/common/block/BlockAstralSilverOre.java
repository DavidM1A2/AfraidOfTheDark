/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class BlockAstralSilverOre extends AOTDBlock
{
	public BlockAstralSilverOre(final Material material)
	{
		super(material);
		this.setUnlocalizedName("astralSilverOre");
		this.setHardness(10.0F);
		this.setResistance(50.0F);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityPlayer, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity)
	{
		if (Research.canResearch(entityPlayer, ResearchTypes.AstralSilver))
		{
			Research.unlockResearchSynced(entityPlayer, ResearchTypes.AstralSilver, Side.SERVER, true);
		}
		super.harvestBlock(world, entityPlayer, blockPos, iBlockState, tileEntity);
	}
}
