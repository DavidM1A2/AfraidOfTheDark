package com.davidm1a2.afraidofthedark.client.sound

import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import net.minecraft.client.Minecraft
import net.minecraft.sounds.SoundSource

/**
 * Class representing the enaria fight music when in the forbidden city
 *
 * @constructor sets the sound event to play and sound category
 * @property enariaId The ID of the Enaria entity
 */
class EnariaFightMusicSound(private val enariaId: Int) : PlayerFollowingSound(ModSounds.ENARIA_FIGHT_MUSIC, SoundSource.PLAYERS) {
    init {
        // This sound loops
        looping = true
        delay = 0
    }

    /**
     * Ensure that this only players in the nightmare when the player is not dead
     */
    override fun tick() {
        super.tick()

        val enaria = Minecraft.getInstance().player!!.level.getEntity(enariaId)
        // If the enaria entity doesn't exist kill the sound
        if (enaria == null) {
            stop()
        }
    }
}