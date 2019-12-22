package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry.ParticleTypes
import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry.spawnParticle
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * Custom packet used to make the client create a particle effect
 *
 * @property particle The particle to tell the client to spawn
 * @property positions A list of positions to spawn the particle at
 * @property speeds A list of speeds to spawn the particle at
 */
class SyncParticle : IMessage
{
    private lateinit var particle: ParticleTypes
    private lateinit var positions: List<Vec3d>
    private lateinit var speeds: List<Vec3d>

    /**
     * Default constructor is required but not used (for reflection)
     */
    constructor()

    /**
     * Overloaded constructor takes a particle to display, as well as a list of speeds/positions to display it at
     *
     * @param particle  The particle to spawn
     * @param positions The positions to spawn it in at
     * @param speeds    The speeds to spawn them in with
     */
    constructor(particle: ParticleTypes, positions: List<Vec3d>, speeds: List<Vec3d>)
    {
        this.particle = particle
        this.positions = positions
        this.speeds = speeds
    }

    /**
     * Reads the particle data from the buffer
     *
     * @param buf The buffer to read from
     */
    override fun fromBytes(buf: ByteBuf)
    {
        // Read the particle, then the number of particles to spawn, then the position/speed of each particle
        particle = ParticleTypes.values()[buf.readInt()]

        val numPositions = buf.readInt()
        val mutablePositions = mutableListOf<Vec3d>()
        val mutableSpeeds = mutableListOf<Vec3d>()
        for (i in 0 until numPositions)
        {
            mutablePositions.add(Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()))
            mutableSpeeds.add(Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()))
        }

        positions = mutablePositions.toList()
        speeds = mutableSpeeds.toList()
    }

    /**
     * Writes the particle data into the buffer
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf)
    {
        // Write the particle index first, then the number of particles to spawn, then each position and speed
        buf.writeInt(particle.ordinal)
        buf.writeInt(positions.size)
        for (i in positions.indices)
        {
            buf.writeDouble(positions[i].x)
            buf.writeDouble(positions[i].y)
            buf.writeDouble(positions[i].z)
            buf.writeDouble(speeds[i].x)
            buf.writeDouble(speeds[i].y)
            buf.writeDouble(speeds[i].z)
        }
    }

    /**
     * Handler class used to handle the packet on the client side
     */
    class Handler : MessageHandler.Client<SyncParticle>()
    {
        /**
         * Called to handle the packet on the client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    The context the message was sent from
         */
        override fun handleClientMessage(player: EntityPlayer, msg: SyncParticle, ctx: MessageContext)
        {
            // Grab the list of positions and speeds
            val positions = msg.positions
            val speeds = msg.speeds

            // Go over each position and speed and spawn a particle for it
            for (i in positions.indices)
            {
                val position = positions[i]
                val speed = speeds[i]
                spawnParticle(msg.particle, player.world, position.x, position.y, position.z, speed.x, speed.y, speed.z)
            }
        }
    }
}