package com.davidm1a2.afraidofthedark.common.packets.packetHandler

import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * The abstract base class for message handlers ([MessageHandler]). This class is intended to be invisible to your own classes, use the
 * subclasses instead.
 *
 * @param <T> The packet type that can be handled
 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
 */
abstract class AbstractMessageHandler<T : IMessage> : IMessageHandler<T, IMessage>
{
    /**
     * Handles a packet on client side. Note that this occurs after decoding has completed.
     *
     * @param player the player reference (the player who received the packet)
     * @param msg    the message received
     * @param ctx    the message context object. This contains additional information about the packet.
     */
    @SideOnly(Side.CLIENT)
    abstract fun handleClientMessage(player: EntityPlayer, msg: T, ctx: MessageContext)

    /**
     * Handles a packet on server side. Note that this occurs after decoding has completed.
     *
     * @param player the player reference (the player who sent the packet)
     * @param msg    the message received
     * @param ctx    the message context object. This contains additional information about the packet.
     */
    abstract fun handleServerMessage(player: EntityPlayer, msg: T, ctx: MessageContext)

    /**
     * Runs the handleClientSide method for the given message. Used to avoid crashes due to @SideOnly on Minecraft.class
     *
     * @param message the message
     */
    @SideOnly(Side.CLIENT)
    private fun runHandleClient(message: T, ctx: MessageContext)
    {
        Minecraft.getMinecraft().addScheduledTask { handleClientMessage(if (ctx.side.isClient) Minecraft.getMinecraft().player else ctx.serverHandler.player, message, ctx) }
    }

    /**
     * Runs the handleServerSide method for the given message.
     *
     * @param message the message
     */
    private fun runHandleServer(message: T, ctx: MessageContext)
    {
        val player = ctx.serverHandler.player
        player.world.minecraftServer!!.addScheduledTask { handleServerMessage(player, message, ctx) }
    }

    /**
     * Processes a received packet and calls the corresponding handler method.
     *
     * @param message the message
     * @param ctx     the context
     */
    override fun onMessage(message: T, ctx: MessageContext): IMessage?
    {
        if (ctx.side.isClient)
        {
            runHandleClient(message, ctx)
        }
        else
        {
            runHandleServer(message, ctx)
        }
        return null
    }
}