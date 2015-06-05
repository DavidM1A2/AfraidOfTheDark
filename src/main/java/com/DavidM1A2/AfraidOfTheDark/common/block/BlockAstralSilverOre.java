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

import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
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
		if (LoadResearchData.canResearch(entityPlayer, ResearchTypes.AstralSilver))
		{
			LoadResearchData.unlockResearchSynced(entityPlayer, ResearchTypes.AstralSilver, Side.SERVER, true);
			LoadResearchData.unlockResearchSynced(entityPlayer, ResearchTypes.AstralSilverSword, Side.SERVER, true);
		}
		super.harvestBlock(world, entityPlayer, blockPos, iBlockState, tileEntity);
	}
}