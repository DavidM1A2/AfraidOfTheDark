package com.davidm1a2.afraidofthedark.client.sound

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.SoundEventAccessor
import net.minecraft.client.audio.SoundHandler
import net.minecraft.util.SoundCategory

/**
 * Class representing the background music in the nightmare after killing enaria
 *
 * @constructor sets the sound event to play and sound category
 */
class NightmareChaseMusicSound : PlayerFollowingSound(ModSounds.NIGHTMARE_CHASE_MUSIC, SoundCategory.AMBIENT) {
    init {
        // This sound loops
        looping = true
        volume = 0.6f
        delay = 0
    }

    /**
     * CreateAccessor is called to create the sound once it's ready to be played. We test if the player has the right
     * research and just stop the music if they don't
     *
     * @param handler The sound handler
     * @return What the super method returns
     */
    override fun resolve(handler: SoundHandler): SoundEventAccessor? {
        val entityPlayer = Minecraft.getInstance().player!!
        if (!entityPlayer.getResearch().isResearched(ModResearches.ARCH_SORCERESS)) {
            stop()
        }
        return super.resolve(handler)
    }

    override fun canPlaySound(): Boolean {
        val entityPlayer = Minecraft.getInstance().player!!
        // Stop playing the sound if the player is 1) dead 2) not in the nightmare
        return entityPlayer.isAlive && entityPlayer.level.dimension() == ModDimensions.NIGHTMARE_WORLD
    }
}