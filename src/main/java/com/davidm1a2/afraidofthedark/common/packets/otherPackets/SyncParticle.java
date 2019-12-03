package com.davidm1a2.afraidofthedark.common.packets.otherPackets;

import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry;
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom packet used to make the client create a particle effect
 */
public class SyncParticle implements IMessage
{
    // The particle to tell the client to spawn
    private AOTDParticleRegistry.ParticleTypes particle;
    // A list of positions to spawn the particle at
    private List<Vec3d> positions;
    private List<Vec3d> speeds;

    /**
     * Default constructor is required but not used (for reflection)
     */
    public SyncParticle()
    {
        this.particle = null;
        this.positions = null;
        this.speeds = null;
    }

    /**
     * Overloaded constructor takes a particle to display, as well as a list of speeds/positions to display it at
     *
     * @param particle  The particle to spawn
     * @param positions The positions to spawn it in at
     * @param speeds    The speeds to spawn them in with
     */
    public SyncParticle(AOTDParticleRegistry.ParticleTypes particle, List<Vec3d> positions, List<Vec3d> speeds)
    {
        this.particle = particle;
        this.positions = positions;
        this.speeds = speeds;
    }

    /**
     * Reads the particle data from the buffer
     *
     * @param buf The buffer to read from
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        // Read the particle, then the number of particles to spawn, then the position/speed of each particle
        this.particle = AOTDParticleRegistry.ParticleTypes.values()[buf.readInt()];
        int numPositions = buf.readInt();
        this.positions = new ArrayList<>();
        this.speeds = new ArrayList<>();
        for (int i = 0; i < numPositions; i++)
        {
            this.positions.add(new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
            this.speeds.add(new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
        }
    }

    /**
     * Writes the particle data into the buffer
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        // Write the particle index first, then the number of particles to spawn, then each position and speed
        buf.writeInt(this.particle.ordinal());
        buf.writeInt(this.positions.size());
        for (int i = 0; i < positions.size(); i++)
        {
            buf.writeDouble(positions.get(i).x);
            buf.writeDouble(positions.get(i).y);
            buf.writeDouble(positions.get(i).z);
            buf.writeDouble(speeds.get(i).x);
            buf.writeDouble(speeds.get(i).y);
            buf.writeDouble(speeds.get(i).z);
        }
    }

    /**
     * Handler class used to handle the packet on the client side
     */
    public static class Handler extends MessageHandler.Client<SyncParticle>
    {
        /**
         * Called to handle the packet on the client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    The context the message was sent from
         */
        @Override
        public void handleClientMessage(EntityPlayer player, SyncParticle msg, MessageContext ctx)
        {
            // Grab the list of positions and speeds
            List<Vec3d> positions = msg.positions;
            List<Vec3d> speeds = msg.speeds;
            // Go over each position and speed and spawn a particle for it
            for (int i = 0; i < positions.size(); i++)
            {
                Vec3d position = positions.get(i);
                Vec3d speed = speeds.get(i);
                AOTDParticleRegistry.INSTANCE.spawnParticle(msg.particle, player.world, position.x, position.y, position.z, speed.x, speed.y, speed.z);
            }
        }
    }
}
