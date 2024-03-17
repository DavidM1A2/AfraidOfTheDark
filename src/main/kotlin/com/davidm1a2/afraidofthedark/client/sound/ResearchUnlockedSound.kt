package com.davidm1a2.afraidofthedark.client.sound

import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import net.minecraft.sounds.SoundSource

/**
 * Class representing the research unlocked sound
 *
 * @constructor sets the sound event to play and sound category
 */
class ResearchUnlockedSound : PlayerFollowingSound(ModSounds.RESEARCH_UNLOCKED, SoundSource.PLAYERS) {
    init {
        volume = 1.0f
        pitch = 1.0f
    }
}