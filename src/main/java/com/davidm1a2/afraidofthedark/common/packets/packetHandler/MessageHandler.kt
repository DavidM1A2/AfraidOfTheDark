package com.davidm1a2.afraidofthedark.common.packets.packetHandler

import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * The Class MessageHandler is a collection of message handlers.
 *
 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
 */
class MessageHandler
{
    /**
     * The client message handler can be extended to write handlers for packets sent to client side.
     *
     * @param <T> the packet type that can be handled
     * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
     */
    abstract class Client<T : IMessage> : AbstractMessageHandler<T>()
    {
        override fun handleServerMessage(player: EntityPlayer, msg: T, ctx: MessageContext)
        {
        }
    }

    /**
     * The server message handler can be extended to write handlers for packets sent to server side.
     *
     * @param <T> the packet type that can be handled
     * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
     */
    abstract class Server<T : IMessage> : AbstractMessageHandler<T>()
    {
        override fun handleClientMessage(player: EntityPlayer, msg: T, ctx: MessageContext)
        {
        }
    }

    /**
     * The bidirectional message handler can be extended to write handlers for packets sent to both server and client side.
     *
     * @param <T> the packet type that can be handled
     * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
     */
    abstract class Bidirectional<T : IMessage> : AbstractMessageHandler<T>()
}