package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellCharmData
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particles.ParticleTypes
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.ceil

/**
 * Effect that forces players to walk towards you or animals to mate
 *
 * @constructor adds the editable prop
 */
class CharmSpellEffect : AOTDDurationSpellEffect("charm", ModResearches.ADVANCED_MAGIC, 1.0, 1.0, 60.0) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val entity = state.entity
        val spellOwner = state.casterEntity
        // If we hit an entity that is an animal set them in love
        if (entity is AnimalEntity) {
            entity.setInLove(spellOwner as? PlayerEntity)
        } else if (entity is PlayerEntity && spellOwner != null) {
            // Grab the player's charm data
            val spellCharmData = entity.getSpellCharmData()
            // Charm them for the "charm duration"
            spellCharmData.charmTicks = ceil(getDuration(instance) * 20).toInt()

            // Set the charming entity
            spellCharmData.charmingEntityId = spellOwner.uuid
            val random = ThreadLocalRandom.current()
            val width = entity.bbWidth.toDouble()
            val height = entity.bbHeight.toDouble()

            // Spawn 4 random heart particles
            for (i in 0..3) {
                state.world.sendParticles(
                    ParticleTypes.HEART,
                    // The position will be somewhere inside the player's hitbox
                    state.position.x + random.nextFloat() * width * 2.0f - width,
                    state.position.y + random.nextFloat() * height,
                    state.position.z + random.nextFloat() * width * 2.0f - width,
                    // Spawn one particle
                    1,
                    // Randomize velocity
                    random.nextGaussian() * 0.02,
                    random.nextGaussian() * 0.02,
                    random.nextGaussian() * 0.02,
                    0.02
                )
            }
        } else {
            createFizzleParticleAt(state)
        }
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        // Charming an entity costs 3
        val baseCost = 3
        // Each second of duration costs 2.0, but the first 1 seconds are free
        val durationCost = ((getDuration(instance) - 1) * 2.0).coerceAtLeast(0.0)
        return baseCost + durationCost * durationCost
    }
}