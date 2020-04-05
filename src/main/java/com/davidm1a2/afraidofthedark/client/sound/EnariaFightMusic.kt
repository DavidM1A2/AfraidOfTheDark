package com.davidm1a2.afraidofthedark.client.sound

import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityEnaria
import net.minecraft.client.Minecraft
import net.minecraft.util.SoundCategory

/**
 * Class representing the enaria fight music when in the gnomish city
 *
 * @constructor sets the sound event to play and sound category
 * @property enariaId The ID of the Enaria entity
 */
class EnariaFightMusic(private val enariaId: Int) :
    PlayerFollowingSound(ModSounds.ENARIA_FIGHT_MUSIC, SoundCategory.PLAYERS) {
    init {
        // This sound loops
        repeat = true
        repeatDelay = 0
    }

    /**
     * Ensure that this only players in the nightmare when the player is not dead
     */
    override fun update() {
        super.update()

        val entityPlayer = Minecraft.getMinecraft().player!!
        val enaria = entityPlayer.world.getEntityByID(enariaId) as? EntityEnaria
        // If the enaria entity doesn't exist kill the sound
        if (enaria == null) {
            donePlaying = true
        } else {
            // Get the distance from the player to enaria, and fade based on distance
            val distance = entityPlayer.getDistance(enaria)
            volume = when {
                distance > MAX_MUSIC_DISTANCE -> 0f
                distance > MAX_MUSIC_VOLUME_DISTANCE -> (MAX_MUSIC_DISTANCE - distance) / MAX_MUSIC_VOLUME_DISTANCE * MAX_MUSIC_VOLUME
                else -> MAX_MUSIC_VOLUME
            }
        }
    }

    companion object {
        // The max distance (in # blocks) that you can hear music
        private const val MAX_MUSIC_DISTANCE = 50

        // The distance at which the music is full volume
        private const val MAX_MUSIC_VOLUME_DISTANCE = 30

        // The max music volume
        private const val MAX_MUSIC_VOLUME = 1.0f
    }
}