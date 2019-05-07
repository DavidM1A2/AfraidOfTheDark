package com.DavidM1A2.afraidofthedark.common.block;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDBlock;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerVoidChestData;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Class that defines a void chest portal block
 */
public class BlockVoidChestPortal extends AOTDBlock
{
	/**
	 * Class constructor sets item properties
	 */
	public BlockVoidChestPortal()
	{
		super("void_chest_portal", Material.PORTAL);
		// This block gives off light
		this.setLightLevel(1.0f);
		this.setResistance(100.0f);
	}

	/**
	 * Called when the entity walks into the block
	 *
	 * @param world The world the block is in
	 * @param blockPos The position of the block
	 * @param iBlockState The block state of the block
	 * @param entity The entity that walked into the block
	 */
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState iBlockState, Entity entity)
	{
		// Server side processing only
		if (!world.isRemote)
		{
			// Test if the entity is a player
			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				// Grab the player's research and void chest data
				IAOTDPlayerResearch playerResearch = entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
				IAOTDPlayerVoidChestData playerVoidChestData = entityPlayer.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null);
				// If the player is in the void chest send them to their stored dimension
				if (world.provider.getDimension() == ModDimensions.VOID_CHEST.getId())
				{
					// Send the player to their previously stored dimension
					entityPlayer.changeDimension(playerVoidChestData.getPreTeleportDimensionID(), ModDimensions.NOOP_TELEPORTER);
				}
				// If it's any other dimension go to the void chest dimension
				else
				{
					// If we can research the research research it
					if (playerResearch.canResearch(ModResearches.VOID_CHEST))
					{
						playerResearch.setResearch(ModResearches.VOID_CHEST, true);
						playerResearch.setResearch(ModResearches.ELDRITCH_DECORATION, true);
						playerResearch.sync(entityPlayer, true);
					}

					// If the player has the void chest research then move the player
					if (playerResearch.isResearched(ModResearches.VOID_CHEST))
					{
						// Make sure no friends index is set since the portal can only send to the player's dimension
						playerVoidChestData.setFriendsIndex(-1);
						entityPlayer.changeDimension(ModDimensions.VOID_CHEST.getId(), ModDimensions.NOOP_TELEPORTER);
					}
				}
			}
		}
	}

	/**
	 * @return This block doesn't show in creative, it's special
	 */
	@Override
	protected boolean displayInCreative()
	{
		return false;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 *
	 * @param random The RNG to use
	 * @return The number of blocks dropped, will always be 0 since you can't harvest this block
	 */
	public int quantityDropped(Random random)
	{
		return 0;
	}

	/**
	 * Called to get the collision/hitbox of this block. We can walk through this block so return the null AABB
	 *
	 * @param blockState The block state of this block
	 * @param worldIn The world the block is in
	 * @param pos The position the block is at
	 * @return The hitbox/bounding box of this block
	 */
	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}

	/**
	 * This block is not opaque since we can see through it
	 *
	 * @param state The block state
	 * @return False since the block is not opaque
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	/**
	 * @return The layer that this block is in, it's translucent since we can see through it
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
}
