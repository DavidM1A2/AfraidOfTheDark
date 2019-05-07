package com.DavidM1A2.afraidofthedark.common.packets.animationPackets;

import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.DavidM1A2.afraidofthedark.common.packets.EntitySyncBase;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Packed used to synchronize animations that are started server side and run client side
 */
public class SyncAnimation extends EntitySyncBase
{
    // The animation to play
    private String animationName = "";
    // Animations that will block this animation from playing, meaning they are of a higher priority
    private String[] higherPriorityAnims;

    /**
     * Required default constructor for all packets
     */
    public SyncAnimation()
    {
        super();
        this.animationName = StringUtils.EMPTY;
        this.higherPriorityAnims = ArrayUtils.EMPTY_STRING_ARRAY;
    }

    /**
     * Primary constructor used to initializes this packet with the entity to sync, animation to play, and higher priority animations
     *
     * @param animationName       The animation to play
     * @param entity              The entity to sync
     * @param higherPriorityAnims Optional argument of higher priority animations
     */
    public SyncAnimation(String animationName, Entity entity, String... higherPriorityAnims)
    {
        super(entity);
        this.animationName = animationName;
        this.higherPriorityAnims = higherPriorityAnims;
    }

    /**
     * De-serializes the byte buffer into data
     *
     * @param buf The buffer to read from
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        this.animationName = ByteBufUtils.readUTF8String(buf);
        this.higherPriorityAnims = new String[buf.readInt()];
        for (int i = 0; i < this.higherPriorityAnims.length; i++)
            this.higherPriorityAnims[i] = ByteBufUtils.readUTF8String(buf);
    }

    /**
     * Serializes the data into a byte buffer
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        ByteBufUtils.writeUTF8String(buf, this.animationName);
        buf.writeInt(this.higherPriorityAnims.length);
        for (String higherPriorityAnim : this.higherPriorityAnims)
            ByteBufUtils.writeUTF8String(buf, higherPriorityAnim);
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    public static class Handler extends MessageHandler.Client<SyncAnimation>
    {
        /**
         * Called whenever we get a sync animation packet from the server
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        @Override
        public void handleClientMessage(EntityPlayer player, SyncAnimation msg, MessageContext ctx)
        {
            // Grab the entity in the world by ID that the server wanted us to update
            Entity entity = player.world.getEntityByID(msg.entityID);
            // Ensure the entity is non-null and a MC animated entity
            if (entity instanceof IMCAnimatedEntity)
            {
                // Grab the animation handler
                AnimationHandler animationHandler = ((IMCAnimatedEntity) entity).getAnimationHandler();
                // Ensure no higher priority animations are active, and if so activate the animation
                if (Arrays.stream(msg.higherPriorityAnims).noneMatch(animationHandler::isAnimationActive))
                {
                    animationHandler.activateAnimation(msg.animationName, 0);
                }
            }
        }
    }
}
