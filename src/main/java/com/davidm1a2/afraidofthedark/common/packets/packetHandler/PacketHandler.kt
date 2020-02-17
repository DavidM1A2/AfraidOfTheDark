package com.davidm1a2.afraidofthedark.common.packets.packetHandler

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler.Bidirectional
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.relauncher.Side

/**
 * Miner's basic packet handler updated
 *
 * @constructor Instantiates a new packet handler with the given channelid and reserves the channel.
 * @param channelId the channelId. This is mostly the modid.
 * @property nextPacketID The ID for the next packet registration
 * @property wrapper The internal network wrapper.
 */
class PacketHandler(private val channelId: String)
{
    private var nextPacketID: Byte = 0
    private val wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(channelId)

    /**
     * Register an IMessage packet with it's corresponding message handler.
     *
     * @param packetClass    the packet's class that should be registered.
     * @param messageHandler the message handler for this packet type.
     * @param target         The side to which this packet can be sent.
     * @return `true`, if successful
     */
    fun <T : IMessage, V : AbstractMessageHandler<T>> registerPacket(packetClass: Class<T>, messageHandler: V, target: Side): Boolean
    {
        check(nextPacketID.toInt() != -1) { "Too many packets registered for channel $channelId" }

        wrapper.registerMessage(messageHandler, packetClass, nextPacketID.toInt(), target)

        if (AfraidOfTheDark.INSTANCE.configurationHandler.debugMessages)
        {
            AfraidOfTheDark.INSTANCE.logger.info(
                    "Registered packet class ${packetClass.simpleName} with handler class ${messageHandler.javaClass.simpleName} for the channel $channelId. Send direction: to ${target.name.toLowerCase()}. The discriminator is $nextPacketID."
            )
        }

        nextPacketID++
        return true
    }

    /**
     * Register an IMessage packet with it's corresponding bidirectional message handler.
     *
     * @param packetClass    the packet's class that should be registered.
     * @param messageHandler the message handler for this packet type.
     * @return `true`, if successful
     */
    fun <T : IMessage> registerBidiPacket(packetClass: Class<T>, messageHandler: Bidirectional<T>): Boolean
    {
        check(nextPacketID.toInt() != -1) { "Too many packets registered for channel $channelId" }

        wrapper.registerMessage(messageHandler, packetClass, nextPacketID.toInt(), Side.CLIENT)
        wrapper.registerMessage(messageHandler, packetClass, nextPacketID.toInt(), Side.SERVER)

        if (AfraidOfTheDark.INSTANCE.configurationHandler.debugMessages)
        {
            AfraidOfTheDark.INSTANCE.logger.info(
                    "Registered packet class ${packetClass.simpleName} with handler class ${messageHandler.javaClass.simpleName} for the channel $channelId. The discriminator is $nextPacketID."
            )
        }

        nextPacketID++
        return true
    }

    /**
     * Sends the given packet to every client.
     *
     * @param message the packet to send.
     */
    fun sendToAll(message: IMessage?)
    {
        wrapper.sendToAll(message)
    }

    /**
     * Sends the given packet to the given player.
     *
     * @param message the packet to send.
     * @param player  the player to send the packet to.
     */
    fun sendTo(message: IMessage, player: EntityPlayerMP)
    {
        if (player.connection != null)
        {
            wrapper.sendTo(message, player)
        }
    }

    /**
     * Sends the given packet to all players around the given target point.
     *
     * @param message the packet to send.
     * @param point   the target point.
     */
    fun sendToAllAround(message: IMessage, point: TargetPoint?)
    {
        wrapper.sendToAllAround(message, point)
    }

    /**
     * Sends the given packet to all players within the radius around the given coordinates.
     *
     * @param message   the packet to send.
     * @param dimension the dimension.
     * @param x         the x coordinate.
     * @param y         the y coordinate.
     * @param z         the z coordinate.
     * @param range     the radius.
     */
    fun sendToAllAround(message: IMessage, dimension: Int, x: Double, y: Double, z: Double, range: Double)
    {
        this.sendToAllAround(message, TargetPoint(dimension, x, y, z, range))
    }

    /**
     * Sends the given packet to all players within the radius around the given entity.
     *
     * @param message the packet to send.
     * @param entity  the entity.
     * @param range   the radius.
     */
    fun sendToAllAround(message: IMessage, entity: Entity, range: Double)
    {
        this.sendToAllAround(message, entity.world.provider.dimension, entity.posX, entity.posY, entity.posZ, range)
    }

    /**
     * Sends the given packet to every player in the given dimension.
     *
     * @param message     the packet to send.
     * @param dimensionId the dimension to send the packet to.
     */
    fun sendToDimension(message: IMessage, dimensionId: Int)
    {
        wrapper.sendToDimension(message, dimensionId)
    }

    /**
     * Sends the given packet to the server.
     *
     * @param message the packet to send.
     */
    fun sendToServer(message: IMessage)
    {
        wrapper.sendToServer(message)
    }
}