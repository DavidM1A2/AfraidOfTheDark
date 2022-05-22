package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.world.World

/**
 * Teleports the spell owner to the hit location
 */
class TeleportSpellEffect : AOTDSpellEffect("teleport", ModResearches.POCKET_DIMENSION) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val world: World = state.world
        val spellCaster = state.casterEntity
        if (spellCaster != null) {
            val position = state.position
            // Create particles at the pre and post teleport position
            createParticlesAt(2, 4, position, spellCaster.level.dimension(), ModParticles.ENDER)
            // Play sound at the pre and post teleport position
            world.playSound(
                null,
                position.x,
                position.y,
                position.z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS,
                2.5f,
                1.0f
            )

            spellCaster.teleportTo(position.x, position.y, position.z)

            createParticlesAt(2, 4, position, spellCaster.level.dimension(), ModParticles.ENDER)
            world.playSound(
                null,
                position.x,
                position.y,
                position.z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS,
                2.5f,
                1.0f
            )
        }
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 30.0
    }
}