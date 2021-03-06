package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellCharmData
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellFreezeData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec3d

/**
 * The cleanse spell effect clears your spell effects
 */
class CleanseSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "cleanse")) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val entity = state.getEntity()
        if (entity != null) {
            // Extinguish the entity
            entity.extinguish()

            // Clear potion effects
            if (entity is LivingEntity) {
                entity.clearActivePotions()
            }

            // Unfreeze and uncharm the player
            if (entity is PlayerEntity) {
                val freezeData = entity.getSpellFreezeData()
                freezeData.freezeTicks = 0
                freezeData.sync(entity)

                val charmData = entity.getSpellCharmData()
                charmData.charmTicks = 0
                freezeData.sync(entity)
            }

            if (reducedParticles) {
                createParticlesAt(0, 1, Vec3d(entity.posX, entity.posY, entity.posZ), entity.dimension, ModParticles.HEAL)
            } else {
                createParticlesAt(2, 4, Vec3d(entity.posX, entity.posY, entity.posZ), entity.dimension, ModParticles.HEAL)
            }
        }
    }

    /**
     * Gets the cost of the effect
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the effect
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 25.0
    }
}