package com.davidm1a2.afraidofthedark.client.sound

import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import net.minecraft.client.Minecraft
import net.minecraft.sounds.SoundSource

/**
 * Class representing the bell ringing sound in the nightmare
 *
 * @constructor sets the sound event to play and sound category
 */
class BellsRingingSound : PlayerFollowingSound(ModSounds.BELLS, SoundSource.AMBIENT) {
    init {
        // This sound loops
        looping = true
    }

    /**
     * Ensure that this only players in the nightmare when the player is not dead
     */
    override fun tick() {
        super.tick()

        val entityPlayer = Minecraft.getInstance().player!!
        if (!entityPlayer.isAlive || entityPlayer.level.dimension() != ModDimensions.NIGHTMARE_WORLD) {
            stop()
        }
    }

    /**
     * Gets the repeat delay for the sound, this will be somewhat random
     *
     * @return The delay between sound plays
     */
    override fun getDelay(): Int {
        // Wait a minute (60 seconds * 20 ticks / second)
        return 60 * 20
    }
}