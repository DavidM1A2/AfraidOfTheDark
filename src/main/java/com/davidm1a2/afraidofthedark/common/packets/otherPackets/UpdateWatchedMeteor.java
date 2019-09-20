package com.davidm1a2.afraidofthedark.common.packets.otherPackets;

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries;
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import com.davidm1a2.afraidofthedark.common.registry.meteor.MeteorEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Packet that can be sent from client -> server to tell the server to make a meteor for us, and server -> client
 * to tell the client what meteor lat/long/drop_angle/type they are watching
 */
public class UpdateWatchedMeteor implements IMessage
{
    // The meteor entry that the user saw
    private MeteorEntry meteorEntry;
    // The 3 fields that are used to
    private int dropAngle;
    private int latitude;
    private int longitude;

    /**
     * Default constructor is required but not used
     */
    public UpdateWatchedMeteor()
    {
        this.meteorEntry = null;
        this.dropAngle = 0;
        this.latitude = 0;
        this.longitude = 0;
    }

    /**
     * First constructor which will be used by the client to tell the server what meteor was selected
     * in the telescope GUI
     *
     * @param meteorEntry The meteor being watched
     */
    public UpdateWatchedMeteor(MeteorEntry meteorEntry)
    {
        this.meteorEntry = meteorEntry;
        this.dropAngle = 0;
        this.latitude = 0;
        this.longitude = 0;
    }

    /**
     * Second constructor which wil lbe used by the server to tell the client what the properties of the
     * watched meteor are
     *
     * @param meteorEntry The meteor being watched
     * @param dropAngle   The angle the meteor dropped at
     * @param latitude    The latitude the meteor was at
     * @param longitude   The longitude the meteor was at
     */
    public UpdateWatchedMeteor(MeteorEntry meteorEntry, int dropAngle, int latitude, int longitude)
    {
        this.meteorEntry = meteorEntry;
        this.dropAngle = dropAngle;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Converts the byte buffer to a structured format
     *
     * @param buf The buffer to read from
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        String meteorEntryString = ByteBufUtils.readUTF8String(buf);
        this.meteorEntry = StringUtils.equals(meteorEntryString, "none") ? null : ModRegistries.METEORS.getValue(new ResourceLocation(meteorEntryString));
        this.dropAngle = buf.readInt();
        this.latitude = buf.readInt();
        this.longitude = buf.readInt();
    }

    /**
     * Converts the structured format into a byte buffer
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.meteorEntry == null ? "none" : this.meteorEntry.getRegistryName().toString());
        buf.writeInt(this.dropAngle);
        buf.writeInt(this.latitude);
        buf.writeInt(this.longitude);
    }

    /**
     * Handler handles UpdateWatchedMeteor packets on both sides. Client updates capabilities and server
     * updates capabilities and updates the client too
     */
    public static class Handler extends MessageHandler.Bidirectional<UpdateWatchedMeteor>
    {
        /**
         * When the client gets the message update the player's watched meteor capability
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    The context the message was sent through
         */
        @Override
        public void handleClientMessage(EntityPlayer player, UpdateWatchedMeteor msg, MessageContext ctx)
        {
            // Update the player's watched meteor
            player.getCapability(ModCapabilities.PLAYER_BASICS, null).setWatchedMeteor(msg.meteorEntry, msg.dropAngle, msg.latitude, msg.longitude);
        }

        /**
         * When the server gets the message randomly generate 3 meteor fields and send that data
         * back to the client
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    The context the message was sent through
         */
        @Override
        public void handleServerMessage(EntityPlayer player, UpdateWatchedMeteor msg, MessageContext ctx)
        {
            // Randomize the meteor drop angle, latitude, and longitude
            MeteorEntry watchedMeteor = msg.meteorEntry;
            int dropAngle = player.getRNG().nextInt(45) + 5;
            int latitude = player.getRNG().nextInt(50) + 5;
            int longitude = player.getRNG().nextInt(130) + 5;
            // Tell the player about the meteor estimated values
            player.sendMessage(new TextComponentTranslation("aotd.falling_meteor.info.header", new TextComponentTranslation(watchedMeteor.getUnLocalizedName())));
            player.sendMessage(new TextComponentTranslation("aotd.falling_meteor.info.data", dropAngle, latitude, longitude));
            player.sendMessage(new TextComponentTranslation("aotd.falling_meteor.info.help"));
            // Update the player's watched meteor and send them values
            IAOTDPlayerBasics playerBasics = player.getCapability(ModCapabilities.PLAYER_BASICS, null);
            playerBasics.setWatchedMeteor(watchedMeteor, dropAngle, latitude, longitude);
            playerBasics.syncWatchedMeteor(player);
        }
    }
}
