package com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The abstract base class for message handlers ({@link MessageHandler}). This class is intended to be invisible to your own classes, use the
 * subclasses instead.
 * 
 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
 * @param <T>
 *            The packet type that can be handled
 */
abstract class AbstractMessageHandler<T extends IMessage> implements IMessageHandler<T, IMessage>
{

	/**
	 * Handles a packet on client side. Note that this occurs after decoding has completed.
	 *
	 * @param player
	 *            the player reference (the player who received the packet)
	 * @param msg
	 *            the message received
	 * @param ctx
	 *            the message context object. This contains additional information about the packet.
	 * @return can return a reply to the received packet. If no reply should be sent, just return <code>null</code>.
	 */
	@SideOnly(Side.CLIENT)
	public abstract IMessage handleClientMessage(final EntityPlayer player, final T msg, final MessageContext ctx);

	/**
	 * Handles a packet on server side. Note that this occurs after decoding has completed.
	 *
	 * @param player
	 *            the player reference (the player who sent the packet)
	 * @param msg
	 *            the message received
	 * @param ctx
	 *            the message context object. This contains additional information about the packet.
	 * @return can return a reply to the received packet. If no reply should be sent, just return <code>null</code>.
	 */
	public abstract IMessage handleServerMessage(final EntityPlayer player, final T msg, final MessageContext ctx);

	/**
	 * Runs the handleClientSide method for the given message. Used to avoid crashes due to @SideOnly on Minecraft.class
	 *
	 * @param message
	 *            the message
	 * @return the reply
	 */
	@SideOnly(Side.CLIENT)
	private IMessage runHandleClient(T message, MessageContext ctx)
	{
		return this.handleClientMessage(ctx.side.isClient() ? Minecraft.getMinecraft().player : ctx.getServerHandler().player, message, ctx);
	}

	/**
	 * Processes a received packet and calls the corresponding handler method.
	 * 
	 * @param message
	 *            the message
	 * @param ctx
	 *            the context
	 */
	@Override
	public final IMessage onMessage(T message, MessageContext ctx)
	{
		if (ctx.side.isClient())
		{
			return this.runHandleClient(message, ctx);
		}
		else
		{
			return this.handleServerMessage(ctx.getServerHandler().player, message, ctx);
		}
	}
}
