package com.davidm1a2.afraidofthedark.common.packets.animationPackets

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.packets.EntityPacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * Packet used to synchronize animations that are started server side and run client side
 */
class AnimationPacketProcessor : EntityPacketProcessor<AnimationPacket>() {
    override fun encode(msg: AnimationPacket, buf: PacketBuffer) {
        writeEntityData(msg, buf)
        buf.writeString(msg.animationName)
        buf.writeInt(msg.higherPriorityAnims.size)
        msg.higherPriorityAnims.forEach { buf.writeString(it) }
    }

    override fun decode(buf: PacketBuffer): AnimationPacket {
        val (uuid, id) = readEntityData(buf)

        return AnimationPacket(
            uuid,
            id,
            buf.readString(500),
            Array(buf.readInt()) {
                buf.readString(500)
            }
        )
    }

    override fun process(msg: AnimationPacket, ctx: NetworkEvent.Context) {
        // Only process client side
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            // Grab the entity in the world by ID that the server wanted us to update
            val entity = Minecraft.getInstance().player.world.getEntityByID(msg.entityID)

            // Ensure the entity is non-null and a MC animated entity
            if (entity is IMCAnimatedModel) {
                // Grab the animation handler
                val animationHandler = entity.getAnimationHandler()

                // Ensure no higher priority animations are active, and if so activate the animation
                if (msg.higherPriorityAnims.none { animationHandler.isAnimationActive(it) }) {
                    animationHandler.playAnimation(msg.animationName)
                }
            }
        }
    }
}