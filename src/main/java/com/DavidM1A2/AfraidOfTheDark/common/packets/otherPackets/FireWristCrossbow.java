package com.DavidM1A2.afraidofthedark.common.packets.otherPackets;

import com.DavidM1A2.afraidofthedark.common.constants.ModSounds;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityBolt;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import com.DavidM1A2.afraidofthedark.common.utility.AOTDBoltType;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Packet that tells the server that the client is ready to fire a wrist crossbow bolt
 */
public class FireWristCrossbow implements IMessage
{
	// The bolt to fire
	private AOTDBoltType selectedBolt;

	/**
	 * Default constructor is required but not used
	 */
	public FireWristCrossbow()
	{
		this.selectedBolt = AOTDBoltType.WOODEN;
	}

	/**
	 * Overloaded constructor takes the bolt as an argument
	 *
	 * @param selectedBolt The bolt to fire
	 */
	public FireWristCrossbow(AOTDBoltType selectedBolt)
	{
		this.selectedBolt = selectedBolt;
	}

	/**
	 * Reads the bolt type from the buffer
	 *
	 * @param buf The buffer to read from
	 */
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.selectedBolt = AOTDBoltType.values()[buf.readInt()];
	}

	/**
	 * Writes the bolt type into the buffer
	 *
	 * @param buf The buffer to write to
	 */
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.selectedBolt.ordinal());
	}

	/**
	 * Handler class for wrist fire crossbow messages sent from the client
	 */
	public static class Handler extends MessageHandler.Server<FireWristCrossbow>
	{
		/**
		 * Called when the server receives the packet
		 *
		 * @param entityPlayer The player that sent the message
		 * @param msg the message received
		 * @param ctx The message's context
		 */
		@Override
		public void handleServerMessage(EntityPlayer entityPlayer, FireWristCrossbow msg, MessageContext ctx)
		{
			// Only fire a bolt if the player is in creative or has the right bolt item
			if (entityPlayer.capabilities.isCreativeMode || entityPlayer.inventory.clearMatchingItems(msg.selectedBolt.getItem(), -1, 1, null) == 1)
			{
				World world = entityPlayer.world;
				// Play a fire sound effect
				world.playSound(null, entityPlayer.getPosition(), ModSounds.CROSSBOW_FIRE, SoundCategory.PLAYERS, 0.5f, world.rand.nextFloat() * 0.4f + 0.8f);
				// Instantiate bolt!
				EntityBolt bolt = msg.selectedBolt.createEntity(world, entityPlayer);
				// Push the bolt slightly forward so it does not collide with the player
				bolt.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0f, 3f, 0f);
				bolt.posX = bolt.posX + bolt.motionX;
				bolt.posY = bolt.posY + bolt.motionY;
				bolt.posZ = bolt.posZ + bolt.motionZ;
				world.spawnEntity(bolt);
			}
		}
	}
}
