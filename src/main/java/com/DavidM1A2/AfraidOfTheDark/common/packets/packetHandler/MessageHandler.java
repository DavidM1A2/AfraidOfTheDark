package com.DavidM1A2.afraidofthedark.common.packets.packetHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * The Class MessageHandler is a collection of message handlers.
 *
 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
 */
public class MessageHandler
{
	/**
	 * The client message handler can be extended to write handlers for packets sent to client side.
	 *
	 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
	 * @param <T>
	 *            the packet type that can be handled
	 */
	public static abstract class Client<T extends IMessage> extends AbstractMessageHandler<T>
	{
		@Override
		public final IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx)
		{
			return null;
		}
	}

	/**
	 * The server message handler can be extended to write handlers for packets sent to server side.
	 *
	 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
	 * @param <T>
	 *            the packet type that can be handled
	 */
	public static abstract class Server<T extends IMessage> extends AbstractMessageHandler<T>
	{
		@Override
		public final IMessage handleClientMessage(EntityPlayer player, T message, MessageContext ctx)
		{
			return null;
		}
	}

	/**
	 * The bidirectional message handler can be extended to write handlers for packets sent to both server and client side.
	 *
	 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
	 * @param <T>
	 *            the packet type that can be handled
	 */
	public static abstract class Bidirectional<T extends IMessage> extends AbstractMessageHandler<T>
	{
	}
}
