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
 * Class representing the background music in the nightmare before killing enaria
 *
 * @constructor sets the sound event to play and sound category
 */
class NightmareMusicSound : PlayerFollowingSound(ModSounds.NIGHTMARE_MUSIC, SoundCategory.AMBIENT) {
    init {
        // This sound loops
        repeat = true
        volume = 0.6f
    }

    /**
     * CreateAccessor is called to create the sound once it's ready to be played. We test if the player has the right
     * research and just stop the music if they don't
     *
     * @param handler The sound handler
     * @return What the super method returns
     */
    override fun createAccessor(handler: SoundHandler): SoundEventAccessor? {
        val entityPlayer = Minecraft.getInstance().player!!
        if (entityPlayer.getResearch().isResearched(ModResearches.ENARIA)) {
            donePlaying = true
        }
        return super.createAccessor(handler)
    }

    /**
     * Ensure that this only players in the nightmare when the player is not dead
     */
    override fun tick() {
        super.tick()

        val entityPlayer = Minecraft.getInstance().player!!
        // Stop playing the sound if the player is 1) dead 2) not in the nightmare
        if (!entityPlayer.isAlive || entityPlayer.dimension.modType != ModDimensions.NIGHTMARE) {
            donePlaying = true
        }
    }

    /**
     * Gets the repeat delay for the sound at 0 seconds to loop
     *
     * @return The delay between sound plays
     */
    override fun getRepeatDelay(): Int {
        // Wait 0 seconds
        return 0
    }
}