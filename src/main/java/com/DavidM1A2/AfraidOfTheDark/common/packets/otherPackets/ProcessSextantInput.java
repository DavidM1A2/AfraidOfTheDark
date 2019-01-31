package com.DavidM1A2.afraidofthedark.common.packets.otherPackets;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import com.DavidM1A2.afraidofthedark.common.registry.meteor.MeteorEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Random;

/**
 * Packet sent from client to server to tell the server to validate sextant information and tell us where a meteor landed
 */
public class ProcessSextantInput implements IMessage
{
	// The 3 fields that we are tracking
	private int dropAngle;
	private int latitude;
	private int longitude;

	/**
	 * Default constructor is required but not used
	 */
	public ProcessSextantInput()
	{
		this.dropAngle = -1;
		this.latitude = -1;
		this.longitude = -1;
	}

	/**
	 * Overloaded constructor that initializes fields
	 *
	 * @param dropAngle The angle the meteor dropped at
	 * @param latitude The latitude the meteor was dropping to
	 * @param longitude The longitude the meteor was dropping to
	 */
	public ProcessSextantInput(int dropAngle, int latitude, int longitude)
	{
		this.dropAngle = dropAngle;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Converts from raw bytes to structured data
	 *
	 * @param buf The buffer to read from
	 */
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.dropAngle = buf.readInt();
		this.latitude = buf.readInt();
		this.longitude = buf.readInt();
	}

	/**
	 * Converts from structured data to raw bytes
	 *
	 * @param buf The buffer to write to
	 */
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.dropAngle);
		buf.writeInt(this.latitude);
		buf.writeInt(this.longitude);
	}

	/**
	 * Handler handles ProcessSextantInput packets on the server side
	 */
	public static class Handler extends MessageHandler.Server<ProcessSextantInput>
	{
		// The maximum distance a meteor can fall away from the player
		private static final int MAX_METEOR_DISTANCE = 500;
		// The chance accuracy of the sextant used, the actual meteor will be within this many blocks of the coordinates
		private static final int ACCURACY = 8;

		/**
		 * Handles the message on the server side
		 *
		 * @param player the player reference (the player who sent the packet)
		 * @param msg the message received
		 * @param ctx The context that the message was sent through
		 */
		@Override
		public void handleServerMessage(EntityPlayer player, ProcessSextantInput msg, MessageContext ctx)
		{
			// First validate that the player entered the correct values into the sextant
			IAOTDPlayerBasics playerBasics = player.getCapability(ModCapabilities.PLAYER_BASICS, null);
			if (playerBasics.getWatchedMeteorDropAngle() == msg.dropAngle &&
					playerBasics.getWatchedMeteorLatitude() == msg.latitude &&
					playerBasics.getWatchedMeteorLongitude() == msg.longitude)
			{
				MeteorEntry meteorEntry = playerBasics.getWatchedMeteor();
				Random random = player.getRNG();

				// The meteor can drop from 15 - 500 blocks away from the player in both directions
				int xLocOfDrop = player.getPosition().getX() +
						(random.nextBoolean() ? -1 : 1) * (random.nextInt(MAX_METEOR_DISTANCE - 15) + 15);
				int zLocOfDrop = player.getPosition().getZ() +
						(random.nextBoolean() ? -1 : 1) * (random.nextInt(MAX_METEOR_DISTANCE - 15) + 15);

				// Drop the meteor
				this.dropMeteor(player, player.world, meteorEntry, xLocOfDrop, zLocOfDrop);
				// Print out a message telling the player where the meteor dropped +/- 8 blocks
				xLocOfDrop = xLocOfDrop + (random.nextBoolean() ? -1 : 1) * random.nextInt(ACCURACY + 1);
				zLocOfDrop = zLocOfDrop + (random.nextBoolean() ? -1 : 1) * random.nextInt(ACCURACY + 1);
				player.sendMessage(new TextComponentString("Based off of this information the meteor fell around " + xLocOfDrop + ", " + zLocOfDrop));

				// Clear the player's watched meteors so that the same meteor can't be used twice
				playerBasics.setWatchedMeteor(null, -1, -1, -1);
				playerBasics.syncWatchedMeteor(player);
			}
			else
				// The values aren't correct so show an error
				player.sendMessage(new TextComponentString("The values entered do not make sense. I should try to re-enter the meteor's data or observe a new meteor."));
		}

		private void dropMeteor(EntityPlayer entityPlayer, World world, MeteorEntry meteorEntry, int xPos, int zPos)
		{

		}
	}
}
