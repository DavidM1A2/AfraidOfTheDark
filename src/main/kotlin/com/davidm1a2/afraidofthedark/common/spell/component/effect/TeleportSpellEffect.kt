package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.ProcResult
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.Level

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
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>): ProcResult {
        val world: Level = state.world
        val spellCaster = state.casterEntity
        if (spellCaster != null) {
            val position = state.position
            // Create particles at the pre- and post-teleport position
            createParticlesAt(
                state, ParticlePacket.builder()
                    .particle(ModParticles.ENDER)
                    .position(spellCaster.position())
                    .iterations(4)
                    .build()
            )
            // Play sound at the pre- and post-teleport position
            world.playSound(
                null,
                position.x,
                position.y,
                position.z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS,
                2.5f,
                1.0f
            )

            spellCaster.teleportTo(position.x, position.y, position.z)

            createParticlesAt(
                state, ParticlePacket.builder()
                    .particle(ModParticles.ENDER)
                    .position(position)
                    .iterations(4)
                    .build()
            )
            world.playSound(
                null,
                position.x,
                position.y,
                position.z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS,
                2.5f,
                1.0f
            )
        } else {
            return ProcResult.failure()
        }
        return ProcResult.success()
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 30.0
    }
}