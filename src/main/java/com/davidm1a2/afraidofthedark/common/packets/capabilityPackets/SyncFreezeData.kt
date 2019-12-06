package com.davidm1a2.afraidofthedark.common.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * This is a packet that is sent from a client to the server that updates the number of ticks the player will be frozen for
 *
 * @property freezeTicks The number of freeze ticks remaining
 * @property position The position the player is frozen at
 * @property yaw The yaw the player was looking when being frozen
 * @property pitch The pitch the player was looking when being frozen
 */
class SyncFreezeData : IMessage
{
    private var freezeTicks: Int
    private lateinit var position: Vec3d
    private var yaw: Float
    private var pitch: Float

    /**
     * Required default constructor that is not used
     */
    constructor()
    {
        freezeTicks = 0
        yaw = 0f
        pitch = 0f
    }

    /**
     * Constructor that initializes the field
     *
     * @param freezeTicks An integer containing the number of freeze ticks remaining
     * @param position    The position that the player was frozen at
     * @param yaw         The yaw of the direction that the player was looking in when frozen
     * @param pitch       The pitch of the direction that the player was looking in when frozen
     */
    constructor(freezeTicks: Int, position: Vec3d, yaw: Float, pitch: Float)
    {
        this.freezeTicks = freezeTicks
        this.position = position
        this.yaw = yaw
        this.pitch = pitch
    }

    /**
     * Converts the byte buf into the boolean data
     *
     * @param buf The buffer to read
     */
    override fun fromBytes(buf: ByteBuf)
    {
        freezeTicks = buf.readInt()
        if (freezeTicks > 0)
        {
            position = Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())
            yaw = buf.readFloat()
            pitch = buf.readFloat()
        }
    }

    /**
     * Converts the boolean into a byte buf
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf)
    {
        buf.writeInt(freezeTicks)
        if (freezeTicks > 0)
        {
            buf.writeDouble(position.x)
            buf.writeDouble(position.y)
            buf.writeDouble(position.z)
            buf.writeFloat(yaw)
            buf.writeFloat(pitch)
        }
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    class Handler : MessageHandler.Client<SyncFreezeData>()
    {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleClientMessage(player: EntityPlayer, msg: SyncFreezeData, ctx: MessageContext)
        {
            val freezeData = player.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA, null)!!
            freezeData.freezeTicks = msg.freezeTicks
            freezeData.freezePosition = msg.position
            freezeData.setFreezeDirection(msg.yaw, msg.pitch)
        }
    }
}