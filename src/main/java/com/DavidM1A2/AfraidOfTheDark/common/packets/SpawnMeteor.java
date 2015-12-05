/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.MeteorTypes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.CreateMeteor;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
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

					MeteorTypes typeToSpawn = MeteorTypes.typeFromIndex(msg.index);

					if (AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.AstronomyII))
					{
						CreateMeteor.create(entityPlayer.worldObj, msg.thePosition, msg.radius, msg.height, false, true, typeToSpawn);
					}
					else if (AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.AstronomyI.getPrevious()) && typeToSpawn == MeteorTypes.silver)
					{
						CreateMeteor.create(entityPlayer.worldObj, msg.thePosition, msg.radius, msg.height, false, true, typeToSpawn);
					}
				}
			});
			return null;
		}
	}
}
