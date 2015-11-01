/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;
import com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest.VoidChestTeleporter;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
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
		this.setCreativeTab(null);
		this.setLightLevel(1.0f);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
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
				if (world.provider.getDimensionId() == Constants.VoidChestWorld.VOID_CHEST_WORLD_ID)
				{
					if (AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.VoidChest))
					{
						Utility.sendPlayerToDimension(entityPlayer, 0, false, VoidChestTeleporter.class);
					}
				}
				else if (world.provider.getDimensionId() == 0)
				{
					if (AOTDPlayerData.get(entityPlayer).canResearch(ResearchTypes.VoidChest))
					{
						AOTDPlayerData.get(entityPlayer).unlockResearch(ResearchTypes.VoidChest, true);
						AOTDPlayerData.get(entityPlayer).unlockResearch(ResearchTypes.EldritchDecoration, true);
					}
					if (AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.VoidChest))
					{
						Utility.sendPlayerToVoidChest(entityPlayer, this.validatePlayerLocationVoidChest(AOTDPlayerData.get(entityPlayer).getPlayerLocationVoidChest(), entityPlayer));
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

	private int validatePlayerLocationVoidChest(int locationX, EntityPlayer entityPlayer)
	{
		if (locationX == 0)
		{
			if (!entityPlayer.worldObj.isRemote)
			{
				MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "/save-all");
			}

			ISaveHandler iSaveHandler = MinecraftServer.getServer().worldServers[0].getSaveHandler();
			if (iSaveHandler instanceof SaveHandler)
			{
				SaveHandler saveHandler = (SaveHandler) iSaveHandler;
				int furthestOutPlayer = 0;
				File playersDirectory = new File(saveHandler.getWorldDirectory(), "playerdata");
				for (String username : saveHandler.getAvailablePlayerDat())
				{
					File playerData = new File(playersDirectory, username + ".dat");

					if (playerData.exists() && playerData.isFile())
					{
						NBTTagCompound playerDataCompound;
						try
						{
							playerDataCompound = CompressedStreamTools.readCompressed(new FileInputStream(playerData));
							furthestOutPlayer = Math.max(furthestOutPlayer, AOTDPlayerData.getPlayerLocationVoidChestOffline(playerDataCompound));
						}
						catch (Exception e)
						{
							LogHelper.info("Error reading player data for username " + username);
						}
					}
				}

				AOTDPlayerData.get(entityPlayer).setPlayerLocationVoidChest(furthestOutPlayer + 1);
			}
		}
		return AOTDPlayerData.get(entityPlayer).getPlayerLocationVoidChest();
	}
}
