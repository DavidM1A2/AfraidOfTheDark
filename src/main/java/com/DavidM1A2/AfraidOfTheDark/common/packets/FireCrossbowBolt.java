/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDCrossbowBoltTypes;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FireCrossbowBolt implements IMessage
{
	private AOTDCrossbowBoltTypes boltType;

	public FireCrossbowBolt()
	{
		boltType = AOTDCrossbowBoltTypes.Iron;
	}

	public FireCrossbowBolt(AOTDCrossbowBoltTypes boltType)
	{
		this.boltType = boltType;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.boltType = AOTDCrossbowBoltTypes.getTypeFromID(buf.readInt());
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeInt(AOTDCrossbowBoltTypes.getIDFromType(this.boltType));
	}

	// When we get a packet we set the current item that the player is holding to the data we rececived
	public static class Handler extends MessageHandler.Server<FireCrossbowBolt>
	{
		@Override
		public IMessage handleServerMessage(final EntityPlayer entityPlayer, final FireCrossbowBolt msg, final MessageContext ctx)
		{
			MinecraftServer.getServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					World world = entityPlayer.worldObj;

					// Only fire a bolt if the player is in creative or has the right bolt item
					if (entityPlayer.capabilities.isCreativeMode || entityPlayer.inventory.consumeInventoryItem(msg.boltType.getMyBoltItem()))
					{
						world.spawnEntityInWorld(msg.boltType.createBolt(world, entityPlayer));
						world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, ((world.rand.nextFloat() * 0.4F) + 0.8F));
					}
				}
			});

			return null;
		}
	}
}