package com.davidm1a2.afraidofthedark.client.sound

import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import net.minecraft.util.SoundCategory

/**
 * Class representing the enaria fight music when in the forbidden city
 *
 * @constructor sets the sound event to play and sound category
 * @property enariaId The ID of the Enaria entity
 */
class EnariaFightMusicSound(private val enariaId: Int) : PlayerFollowingSound(ModSounds.ENARIA_FIGHT_MUSIC, SoundCategory.PLAYERS) {
    init {
        // This sound loops
        looping = true
        delay = 0
    }
}