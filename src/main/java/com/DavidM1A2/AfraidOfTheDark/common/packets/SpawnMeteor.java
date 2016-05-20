/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDMeteorTypes;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SpawnMeteor implements IMessage
{
	private BlockPos thePosition;
	private int radius;
	private int height;
	private int index;

	public SpawnMeteor()
	{
		this.thePosition = null;
		this.radius = 0;
		this.height = 0;
		this.index = -1;
	}

	public SpawnMeteor(final BlockPos thePosition, final int radius, final int height, final int index)
	{
		this.thePosition = thePosition;
		this.radius = radius;
		this.height = height;
		this.index = index;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.thePosition = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.radius = buf.readInt();
		this.height = buf.readInt();
		this.index = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeInt(this.thePosition.getX());
		buf.writeInt(this.thePosition.getY());
		buf.writeInt(this.thePosition.getZ());
		buf.writeInt(this.radius);
		buf.writeInt(this.height);
		buf.writeInt(this.index);
	}

	// when we receive a packet we set HasStartedAOTD
	public static class Handler extends MessageHandler.Server<SpawnMeteor>
	{
		@Override
		public IMessage handleServerMessage(final EntityPlayer entityPlayer, final SpawnMeteor msg, MessageContext ctx)
		{
			MinecraftServer.getServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					if (ConfigurationHandler.debugMessages)
					{
						LogHelper.info("Player has requested to place a meteor at " + msg.thePosition.toString());
					}

					AOTDMeteorTypes typeToSpawn = AOTDMeteorTypes.typeFromIndex(msg.index);

					if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.AstronomyII))
					{
						SpawnMeteor.create(entityPlayer.worldObj, msg.thePosition, msg.radius, msg.height, false, true, typeToSpawn);
					}
					else if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.AstronomyI.getPrevious()) && typeToSpawn == AOTDMeteorTypes.silver)
					{
						SpawnMeteor.create(entityPlayer.worldObj, msg.thePosition, msg.radius, msg.height, false, true, typeToSpawn);
					}
				}
			});
			return null;
		}
	}

	// Meteor creation code

	private static final List<Block> replaceableBlocks = new ArrayList<Block>()
	{
		{
			add(ModBlocks.astralSilverOre);
			add(ModBlocks.starMetalOre);
			add(ModBlocks.sunstoneOre);
			add(ModBlocks.meteor);
			add(Blocks.dirt);
			add(Blocks.grass);
			add(Blocks.leaves);
			add(Blocks.leaves2);
			add(Blocks.sand);
			add(Blocks.log);
			add(Blocks.log2);
			add(Blocks.vine);
			add(Blocks.deadbush);
			add(Blocks.double_plant);
			add(Blocks.ice);
			add(Blocks.air);
			add(Blocks.stone);
			add(Blocks.gravel);
			add(Blocks.sandstone);
			add(Blocks.snow);
			add(Blocks.snow_layer);
		}
	};

	private static void create(final World world, final BlockPos location, final int radius, final int height, final boolean hollow, final boolean isSphere, final AOTDMeteorTypes type)
	{
		int cx = location.getX();
		int cy = makeSureChunkIsGenerated(world, location);
		int cz = location.getZ();
		for (int x = cx - radius; x <= cx + radius; x++)
		{
			for (int z = cz - radius; z <= cz + radius; z++)
			{
				for (int y = (isSphere ? cy - radius : cy); y < (isSphere ? cy + radius : cy + height); y++)
				{
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (isSphere ? (cy - y) * (cy - y) : 0);
					if (dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1)))
					{
						Block current = world.getBlockState(new BlockPos(x, y, z)).getBlock();
						if (replaceableBlocks.contains(current) || current.getMaterial() == Material.water || current.getMaterial() == Material.lava)
						{
							world.setBlockState(new BlockPos(x, y, z), ModBlocks.meteor.getDefaultState());
						}
					}
				}
			}
		}

		SpawnMeteor.createCore(world, new BlockPos(cx, cy, cz), MathHelper.ceiling_double_int(radius / 2.5), MathHelper.ceiling_double_int(height / 2.5), hollow, isSphere, type);
	}

	private static void createCore(World world, BlockPos location, int radius, int height, boolean hollow, boolean isSphere, AOTDMeteorTypes type)
	{
		Block toPlace = (type == AOTDMeteorTypes.silver) ? ModBlocks.astralSilverOre : (type == AOTDMeteorTypes.sunstone) ? ModBlocks.sunstoneOre : ModBlocks.starMetalOre;
		int cx = location.getX();
		int cy = location.getY();
		int cz = location.getZ();
		for (int x = cx - radius; x <= cx + radius; x++)
		{
			for (int z = cz - radius; z <= cz + radius; z++)
			{
				for (int y = (isSphere ? cy - radius : cy); y < (isSphere ? cy + radius : cy + height); y++)
				{
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (isSphere ? (cy - y) * (cy - y) : 0);
					if (dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1)))
					{
						Block current = world.getBlockState(new BlockPos(x, y, z)).getBlock();
						if (replaceableBlocks.contains(current) || current.getMaterial() == Material.water || current.getMaterial() == Material.lava)
						{
							world.setBlockState(new BlockPos(x, y, z), toPlace.getDefaultState());
						}
					}
				}
			}
		}
	}

	private static int makeSureChunkIsGenerated(World world, BlockPos location)
	{
		if (!world.getChunkProvider().chunkExists(location.getX(), location.getZ()))
		{
			world.getChunkProvider().provideChunk(location.getX(), location.getZ());
		}

		while (world.getBlockState(location).getBlock() instanceof BlockAir || world.getBlockState(location).getBlock().getMaterial() == Material.water)
		{
			location = new BlockPos(location.getX(), location.getY() - 1, location.getZ());
		}
		return location.getY();
	}
}
