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
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAOTDBarrier extends AOTDBlock
{
	public BlockAOTDBarrier()
	{
		super(Material.barrier);
		this.setBlockUnbreakable();
		this.setResistance(6000001.0F);
		this.disableStats();
		this.translucent = true;
		this.setUnlocalizedName("aOTDBarrier");
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType()
	{
		return -1;
	}

	/**
	 * Called when the block is destroyed by an explosion. Useful for allowing the block to take into account tile entities, state, etc. when
	 * exploded, before it is removed.
	 *
	 * @param world
	 *            The current world
	 * @param pos
	 *            Block position in world
	 * @param Explosion
	 *            The explosion instance affecting the block
	 */
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		return;
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, BlockPos blockPos, Entity entity)
	{
		return false;
	}

	@Override
	public boolean canDropFromExplosion(Explosion explosion)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	/**
	 * Returns the default ambient occlusion value based on block opacity
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public float getAmbientOcclusionLightValue()
	{
		return 1.0F;
	}

	/**
	 * Spawns this Block's drops into the World as EntityItems.
	 * 
	 * @param chance
	 *            The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
	 * @param fortune
	 *            The player's fortune level
	 */
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{
	}
}
