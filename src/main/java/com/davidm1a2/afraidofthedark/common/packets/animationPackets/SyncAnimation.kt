package com.davidm1a2.afraidofthedark.common.packets.animationPackets

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity
import com.davidm1a2.afraidofthedark.common.packets.EntitySyncBase
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler
import io.netty.buffer.ByteBuf
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * Packed used to synchronize animations that are started server side and run client side
 *
 * @property animationName The animation to play
 * @property higherPriorityAnims Animations that will block this animation from playing, meaning they are of a higher priority
 */
class SyncAnimation : EntitySyncBase
{
    private var animationName: String
    private var higherPriorityAnims: Array<String>

    /**
     * Required default constructor for all packets
     */
    constructor() : super()
    {
        animationName = ""
        higherPriorityAnims = arrayOf()
    }

    /**
     * Primary constructor used to initializes this packet with the entity to sync, animation to play, and higher priority animations
     *
     * @param animationName       The animation to play
     * @param entity              The entity to sync
     * @param higherPriorityAnims Optional argument of higher priority animations
     */
    constructor(animationName: String, entity: Entity, vararg higherPriorityAnims: String) : super(entity)
    {
        this.animationName = animationName
        this.higherPriorityAnims = arrayOf(*higherPriorityAnims)
    }

    /**
     * De-serializes the byte buffer into data
     *
     * @param buf The buffer to read from
     */
    override fun fromBytes(buf: ByteBuf)
    {
        super.fromBytes(buf)
        animationName = ByteBufUtils.readUTF8String(buf)
        higherPriorityAnims = Array(buf.readInt())
        {
            ByteBufUtils.readUTF8String(buf)
        }
    }

    /**
     * Serializes the data into a byte buffer
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf)
    {
        super.toBytes(buf)
        ByteBufUtils.writeUTF8String(buf, animationName)
        buf.writeInt(higherPriorityAnims.size)
        higherPriorityAnims.forEach { ByteBufUtils.writeUTF8String(buf, it) }
    }

    /**
     * Handler to perform actions upon getting a packet
     */
    class Handler : MessageHandler.Client<SyncAnimation>()
    {
        /**
         * Called whenever we get a sync animation packet from the server
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    the message context object. This contains additional information about the packet.
         */
        override fun handleClientMessage(player: EntityPlayer, msg: SyncAnimation, ctx: MessageContext)
        {
            // Grab the entity in the world by ID that the server wanted us to update
            val entity = player.world.getEntityByID(msg.entityID)

            // Ensure the entity is non-null and a MC animated entity
            if (entity is IMCAnimatedEntity)
            {
                // Grab the animation handler
                val animationHandler = (entity as IMCAnimatedEntity).animationHandler

                // Ensure no higher priority animations are active, and if so activate the animation
                if (msg.higherPriorityAnims.none { animationHandler.isAnimationActive(it) })
                {
                    animationHandler.activateAnimation(msg.animationName, 0f)
                }
            }
        }
    }
}