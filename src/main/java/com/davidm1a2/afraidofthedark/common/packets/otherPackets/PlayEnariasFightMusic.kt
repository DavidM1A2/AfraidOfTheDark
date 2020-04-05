package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.client.sound.EnariaFightMusic
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityEnaria
import com.davidm1a2.afraidofthedark.common.packets.EntitySyncBase
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.ISound
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * Class used to tell the player to play the enaria fight music
 *
 * @property playMusic True if the music should play, false if it should stop playing
 */
class PlayEnariasFightMusic : EntitySyncBase {
    private var playMusic: Boolean

    /**
     * Required default constructor for all packets
     */
    constructor() : super() {
        playMusic = false
    }

    /**
     * Primary constructor used to initializes this packet with the enaria entity and a boolean
     *
     * @param entity The enaria entity to sync
     * @param playMusic True if we're starting the music, false if we're stopping the music
     */
    constructor(entity: EntityEnaria, playMusic: Boolean) : super(entity) {
        this.playMusic = playMusic
    }

    /**
     * Deserializes the playMusic boolean
     *
     * @param buf The buffer to read from
     */
    override fun fromBytes(buf: ByteBuf) {
        super.fromBytes(buf)
        this.playMusic = buf.readBoolean()
    }

    /**
     * Serializes the playMusic boolean
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf) {
        super.toBytes(buf)
        buf.writeBoolean(playMusic)
    }

    /**
     * Handler handles PlayEnariasFightMusic packets on the client side. This packet tells the client to play
     * the Enaria fight music
     */
    class Handler : MessageHandler.Client<PlayEnariasFightMusic>() {
        private var currentMusicInstance: ISound? = null

        /**
         * When the client gets the message update the player's watched meteor capability
         *
         * @param player the player reference (the player who received the packet)
         * @param msg    the message received
         * @param ctx    The context the message was sent through
         */
        override fun handleClientMessage(player: EntityPlayer, msg: PlayEnariasFightMusic, ctx: MessageContext) {
            val soundHandler = Minecraft.getMinecraft().soundHandler
            val musicIsPlaying = currentMusicInstance != null && soundHandler.isSoundPlaying(currentMusicInstance!!)

            if (musicIsPlaying) {
                // If we shouldn't play the music ignore cancel it
                if (!msg.playMusic) {
                    soundHandler.stopSound(currentMusicInstance!!)
                    currentMusicInstance = null
                }
            } else {
                // If we should play music and it isn't yet start it
                if (msg.playMusic) {
                    currentMusicInstance = EnariaFightMusic(msg.entityID)
                    soundHandler.playSound(currentMusicInstance!!)
                }
            }
        }
    }
}