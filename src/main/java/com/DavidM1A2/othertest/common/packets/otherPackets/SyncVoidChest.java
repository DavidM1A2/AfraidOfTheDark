package com.DavidM1A2.afraidofthedark.common.packets.otherPackets;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.packets.EntitySyncBase;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import com.DavidM1A2.afraidofthedark.common.tileEntity.TileEntityVoidChest;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Packet that tells other players in the area that a void chest has been opened
 */
public class SyncVoidChest extends EntitySyncBase
{
    // The chest's x, y, and z position
    private int chestX;
    private int chestY;
    private int chestZ;

    /**
     * Default constructor is required but not used
     */
    public SyncVoidChest()
    {
        super();
        this.chestX = 0;
        this.chestY = 0;
        this.chestZ = 0;
    }

    /**
     * Primary constructor initializes fields
     *
     * @param chestX The X position of this chest
     * @param chestY The Y position of this chest
     * @param chestZ The Z position of this chest
     * @param player The player that opened the chest
     */
    public SyncVoidChest(int chestX, int chestY, int chestZ, EntityPlayer player)
    {
        super(player);
        this.chestX = chestX;
        this.chestY = chestY;
        this.chestZ = chestZ;
    }

    /**
     * Deserializes the UUID and chest x,y,z
     *
     * @param buf The buffer to read from
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        this.chestX = buf.readInt();
        this.chestY = buf.readInt();
        this.chestZ = buf.readInt();
    }

    /**
     * Serializes the UUID and chest x,y,z
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(this.chestX);
        buf.writeInt(this.chestY);
        buf.writeInt(this.chestZ);
    }

    /**
     * Handler class handles SyncVoidChest packets on the client side
     */
    public static class Handler extends MessageHandler.Client<SyncVoidChest>
    {
        /**
         * Called to handle the packet on the client side
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    The context the message was sent from
         */
        @Override
        public void handleClientMessage(EntityPlayer player, SyncVoidChest msg, MessageContext ctx)
        {
            // The player that opened the chest
            EntityPlayer chestOpener = player.world.getPlayerEntityByUUID(msg.entityUUID);
            if (chestOpener != null)
            {
                // Grab the void chest tile entity
                TileEntity chestTileEntity = player.world.getTileEntity(new BlockPos(msg.chestX, msg.chestY, msg.chestZ));
                if (chestTileEntity != null)
                {
                    // Ensure the tile entity is valid
                    if (chestTileEntity instanceof TileEntityVoidChest)
                    {
                        // Open the tile entity
                        TileEntityVoidChest voidChest = (TileEntityVoidChest) chestTileEntity;
                        voidChest.openChest(chestOpener);
                    }
                    else
                    {
                        AfraidOfTheDark.INSTANCE.getLogger().warn("Attempted to sync a void chest opening on a non void chest tile entity!");
                    }
                }
                else
                {
                    AfraidOfTheDark.INSTANCE.getLogger().warn("Attempted to find an invalid void chest!");
                }
            }
            else
            {
                AfraidOfTheDark.INSTANCE.getLogger().warn("Attempted to update user of a void chest opening from a null player!");
            }
        }
    }
}
