/*
 * PacketHandler.java (PacketHandler)
 * Package minersbasic.api.network
 *
 * Project: Miners Basic
 *
 * Author: _Bedrock_Miner_ (minerbedrock@gmail.com)
 *
 * Last Modified: 04.04.2015
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * This class is used to send Packets ({@link IMessage}) between server and clients.
 * <p>
 * <strong>Usage:</strong><br>
 * <strong>Creation:</strong><br>
 * {@code PacketHandler handler = new PacketHandler(String channelid)}<br>
 * <em>OR</em><br>
 * {@code PacketHandler handler = MinersbasicAPI.createPacketHandler(String channelid)}
 * <p>
 * <strong>Registering a packet:</strong><br>
 * See: {@linkplain #registerPacket(Class, AbstractMessageHandler, Side)}<br>
 * See: {@linkplain #registerBidiPacket(Class, MessageHandler.Bidirectional)}
 * <p>
 * <strong>Sending packets:</strong><br>
 * See: {@linkplain #sendTo(IMessage, EntityPlayerMP)}<br>
 * See: {@linkplain #sendToAll(IMessage)}<br>
 * See: {@linkplain #sendToAllAround(IMessage, NetworkRegistry.TargetPoint)}<br>
 * See: {@linkplain #sendToAllAround(IMessage, int, double, double, double, double)}<br>
 * See: {@linkplain #sendToAllAround(IMessage, Entity, double)}<br>
 * See: {@linkplain #sendToDimension(IMessage, int)}<br>
 * See: {@linkplain #sendToServer(IMessage)}<br>
 *
 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
 */
public final class PacketHandler
{

	/** The ID for the next packet registration. */

	private byte nextPacketID = 0;
	/** The internal network wrapper. */
	private SimpleNetworkWrapper wrapper;
	/** The channelid. */
	private String channelid;

	/**
	 * Instantiates a new packet handler with the given channelid and reserves the channel.
	 *
	 * @param channelid
	 *            the channelid. This is mostly the modid.
	 */
	public PacketHandler(String channelid)
	{
		this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(channelid);
		this.channelid = channelid;
	}

	/**
	 * Register an IMessage packet with it's corresponding message handler.
	 *
	 * @param packetClass
	 *            the packet's class that should be registered.
	 * @param messageHandler
	 *            the message handler for this packet type.
	 * @param target
	 *            The side to which this packet can be sent.
	 * @return <code>true</code>, if successful
	 */
	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public boolean registerPacket(Class<? extends IMessage> packetClass, AbstractMessageHandler messageHandler, Side target)
	{
		if (this.nextPacketID == -1)
			throw new IllegalStateException("Too many packets registered for channel " + this.channelid);

		this.wrapper.registerMessage(messageHandler, packetClass, this.nextPacketID, target);
		if (ConfigurationHandler.debugMessages)
		{
			LogHelper.info(String.format("Registered packet class %s with handler class %s for the channel %s. Send direction: to %s. The discriminator is %s.", packetClass.getSimpleName(), messageHandler.getClass().getSimpleName(), this.channelid, target.name().toLowerCase(), this.nextPacketID));
		}
		this.nextPacketID++;
		return true;
	}

	/**
	 * Register an IMessage packet with it's corresponding bidirectional message handler.
	 *
	 * @param packetClass
	 *            the packet's class that should be registered.
	 * @param messageHandler
	 *            the message handler for this packet type.
	 * @return <code>true</code>, if successful
	 */
	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public boolean registerBidiPacket(Class<? extends IMessage> packetClass, MessageHandler.Bidirectional messageHandler)
	{
		if (this.nextPacketID == -1)
			throw new IllegalStateException("Too many packets registered for channel " + this.channelid);

		this.wrapper.registerMessage(messageHandler, packetClass, this.nextPacketID, Side.CLIENT);
		this.wrapper.registerMessage(messageHandler, packetClass, this.nextPacketID, Side.SERVER);
		if (ConfigurationHandler.debugMessages)
		{
			LogHelper.info(String.format("Registered bidirectional packet class %s with handler class %s for the channel %s. The discriminator is %s.", packetClass.getSimpleName(), messageHandler.getClass().getSimpleName(), this.channelid, this.nextPacketID));
		}
		this.nextPacketID++;
		return true;
	}

	/**
	 * Sends the given packet to every client.
	 *
	 * @param message
	 *            the packet to send.
	 */
	public void sendToAll(IMessage message)
	{
		this.wrapper.sendToAll(message);
	}

	/**
	 * Sends the given packet to the given player.
	 *
	 * @param message
	 *            the packet to send.
	 * @param player
	 *            the player to send the packet to.
	 */
	public void sendTo(IMessage message, EntityPlayerMP player)
	{
		if (player.connection != null)
			this.wrapper.sendTo(message, player);
	}

	/**
	 * Sends the given packet to all players around the given target point.
	 *
	 * @param message
	 *            the packet to send.
	 * @param point
	 *            the target point.
	 */
	public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point)
	{
		this.wrapper.sendToAllAround(message, point);
	}

	/**
	 * Sends the given packet to all players within the radius around the given coordinates.
	 *
	 * @param message
	 *            the packet to send.
	 * @param dimension
	 *            the dimension.
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 * @param range
	 *            the radius.
	 */
	public void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range)
	{
		this.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}

	/**
	 * Sends the given packet to all players within the radius around the given entity.
	 *
	 * @param message
	 *            the packet to send.
	 * @param entity
	 *            the entity.
	 * @param range
	 *            the radius.
	 */
	public void sendToAllAround(IMessage message, Entity entity, double range)
	{
		this.sendToAllAround(message, entity.world.provider.getDimension(), entity.posX, entity.posY, entity.posZ, range);
	}

	/**
	 * Sends the given packet to every player in the given dimension.
	 *
	 * @param message
	 *            the packet to send.
	 * @param dimensionId
	 *            the dimension to send the packet to.
	 */
	public void sendToDimension(IMessage message, int dimensionId)
	{
		this.wrapper.sendToDimension(message, dimensionId);
	}

	/**
	 * Sends the given packet to the server.
	 *
	 * @param message
	 *            the packet to send.
	 */
	public void sendToServer(IMessage message)
	{
		this.wrapper.sendToServer(message);
	}
}