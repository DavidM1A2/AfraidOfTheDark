package com.DavidM1A2.afraidofthedark.common.packets.capabilityPackets;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellFreezeData;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This is a packet that is sent from a client to the server that updates the number of ticks the player will be frozen for
 */
public class SyncFreezeData implements IMessage
{
    // The number of freeze ticks remaining
    private int freezeTicks;
    // The position the player is frozen at
    private Vec3d position;
    // The direction the player was looking when being frozen
    private Vec2f direction;

    /**
     * Required default constructor that is not used
     */
    public SyncFreezeData()
    {
        this.freezeTicks = 0;
        this.position = null;
        this.direction = null;
    }

    /**
     * Constructor that initializes the field
     *
     * @param freezeTicks An integer containing the number of freeze ticks remaining
     * @param position    The position that the player was frozen at
     * @param direction   The direction that the player was looking in when frozen
     */
    public SyncFreezeData(int freezeTicks, Vec3d position, Vec2f direction)
    {
        this.freezeTicks = freezeTicks;
        this.position = position;
        this.direction = direction;
    }

    /**
     * Converts the byte buf into the boolean data
     *
     * @param buf The buffer to read
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.freezeTicks = buf.readInt();
        if (freezeTicks > 0)
        {
            this.position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
            this.direction = new Vec2f(buf.readFloat(), buf.readFloat());
        }
    }

    /**
     * Converts the boolean into a byte buf
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.freezeTicks);
        if (freezeTicks > 0)
        {
            buf.writeDouble(this.position.x);
            buf.writeDouble(this.position.y);
            buf.writeDouble(this.position.z);
            buf.writeFloat(this.direction.x);
            buf.writeFloat(this.direction.y);
        }
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    public static class Handler extends MessageHandler.Client<SyncFreezeData>
    {
        /**
         * Handles the packet on client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        @Override
        public void handleClientMessage(EntityPlayer player, SyncFreezeData msg, MessageContext ctx)
        {
            IAOTDPlayerSpellFreezeData freezeData = player.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA, null);
            freezeData.setFreezeTicks(msg.freezeTicks);
            freezeData.setFreezePosition(msg.position);
            freezeData.setFreezeDirection(msg.direction);
        }
    }
}
