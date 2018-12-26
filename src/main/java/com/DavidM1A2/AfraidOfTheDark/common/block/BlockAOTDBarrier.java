/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAOTDBarrier extends AOTDBlock
{
	public BlockAOTDBarrier()
	{
		super(Material.BARRIER);
		this.setBlockUnbreakable();
		this.setResistance(6000001.0F);
		this.disableStats();
		this.translucent = true;
		this.setUnlocalizedName("aotd_barrier");
		this.setRegistryName("aotd_barrier");
		this.setCreativeTab(null);
	}

	@Override
	protected boolean displayInCreative()
	{
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public void onBlockExploded(World world, net.minecraft.util.math.BlockPos pos, Explosion explosion)
	{
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, net.minecraft.util.math.BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	public boolean canDropFromExplosion(Explosion explosionIn)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state)
	{
		return 1.0F;
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{
	}
}
