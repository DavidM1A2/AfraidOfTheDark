package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.client.sound.EnariaFightMusicSound
import com.davidm1a2.afraidofthedark.common.network.packets.EntityPacketProcessor
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.ISound
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * Class used to tell the player to play the enaria fight music
 */
class PlayEnariasFightMusicPacketProcessor : EntityPacketProcessor<PlayEnariasFightMusicPacket>() {
    private var currentMusicInstance: ISound? = null

    override fun encode(msg: PlayEnariasFightMusicPacket, buf: PacketBuffer) {
        writeEntityData(msg, buf)
        buf.writeBoolean(msg.playMusic)
    }

    override fun decode(buf: PacketBuffer): PlayEnariasFightMusicPacket {
        val (uuid, id) = readEntityData(buf)
        return PlayEnariasFightMusicPacket(
            uuid,
            id,
            buf.readBoolean()
        )
    }

    override fun process(msg: PlayEnariasFightMusicPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_CLIENT) {
            val soundHandler = Minecraft.getInstance().soundManager
            val musicIsPlaying = currentMusicInstance?.let { soundHandler.isActive(it) } ?: false

            if (musicIsPlaying) {
                // If we shouldn't play the music ignore cancel it
                if (!msg.playMusic) {
                    soundHandler.stop(currentMusicInstance!!)
                    currentMusicInstance = null
                }
            } else {
                // If we should play music and it isn't yet start it
                if (msg.playMusic) {
                    currentMusicInstance = EnariaFightMusicSound(msg.entityID)
                    soundHandler.play(currentMusicInstance!!)
                }
            }
        }
    }
}