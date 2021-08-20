package com.davidm1a2.afraidofthedark.client.sound

import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import net.minecraft.client.Minecraft
import net.minecraft.util.SoundCategory

/**
 * Class representing the eerie echo that plays when entering the nightamre realm
 *
 * @constructor sets the sound event to play and category
 */
class EerieEchoSound : PlayerFollowingSound(ModSounds.EERIE_ECHOS, SoundCategory.AMBIENT) {
    init {
        looping = false
        volume = 1.0f
        delay = 0
    }

    override fun canPlaySound(): Boolean {
        val entityPlayer = Minecraft.getInstance().player!!
        // Stop playing the sound if the player is 1) dead 2) not in the nightmare
        return entityPlayer.isAlive && entityPlayer.level.dimension() == ModDimensions.NIGHTMARE_WORLD
    }
}