package com.davidm1a2.afraidofthedark.client.sound

import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.entity.enaria.EnariaEntity
import net.minecraft.client.Minecraft
import net.minecraft.util.SoundCategory

/**
 * Class representing the enaria fight music when in the gnomish city
 *
 * @constructor sets the sound event to play and sound category
 * @property enariaId The ID of the Enaria entity
 */
class EnariaFightMusicSound(private val enariaId: Int) :
    PlayerFollowingSound(ModSounds.ENARIA_FIGHT_MUSIC, SoundCategory.PLAYERS) {
    init {
        // This sound loops
        repeat = true
        repeatDelay = 0
    }

    /**
     * Ensure that this only players in the nightmare when the player is not dead
     */
    override fun tick() {
        super.tick()

        val enaria = Minecraft.getInstance().player!!.world.getEntityByID(enariaId) as? EnariaEntity
        // If the enaria entity doesn't exist kill the sound
        if (enaria == null) {
            donePlaying = true
        }
    }
}