/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockVoidChestPortal extends AOTDBlock
{
	private long lastTimeEntered = 0;
	private static final int TIME_INBETWEEN_MESSAGES = 3000;

	public BlockVoidChestPortal()
	{
		super(Material.rock);
		this.setUnlocalizedName("voidChestPortal");
		this.setLightLevel(1.0f);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
	}

	@Override
	protected boolean displayInCreative()
	{
		return false;
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
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return null;
	}

	/**
	 * Determines if this block is can be destroyed by the specified entities normal behavior.
	 *
	 * @param world
	 *            The current world
	 * @param pos
	 *            Block position in world
	 * @return True to allow the ender dragon to destroy this block
	 */
	public boolean canEntityDestroy(IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	/**
	 * Called When an Entity Collided with the Block
	 */
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState iBlockState, Entity entity)
	{
		if (!world.isRemote)
		{
			if (entity instanceof EntityPlayerMP)
			{
				EntityPlayerMP entityPlayer = (EntityPlayerMP) entity;
				if (world.provider.getDimensionId() == AOTDDimensions.VoidChest.getWorldID())
				{
					if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.VoidChest))
					{
						AOTDDimensions.VoidChest.fromDimensionTo(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerDimensionPreTeleport(), entityPlayer);
					}
				}
				else
				{
					if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.VoidChest))
					{
						entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.VoidChest, true);
						entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.EldritchDecoration, true);
					}
					if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.VoidChest))
					{
						AOTDDimensions.VoidChest.toDimension(entityPlayer);
					}
					else
					{
						if (System.currentTimeMillis() - this.lastTimeEntered > BlockVoidChestPortal.TIME_INBETWEEN_MESSAGES)
						{
							entityPlayer.addChatMessage(new ChatComponentText("This mysterious block could become more useful later. I should write down the location of this place."));
							this.lastTimeEntered = System.currentTimeMillis();
						}
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Item getItem(World worldIn, BlockPos pos)
	{
		return null;
	}
}
