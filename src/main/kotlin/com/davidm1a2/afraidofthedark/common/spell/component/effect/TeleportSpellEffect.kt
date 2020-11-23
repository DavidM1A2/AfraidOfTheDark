package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.world.World

/**
 * Teleports the spell owner to the hit location
 */
class TeleportSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "teleport")) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val world: World = state.world
        val spellCaster = state.getCasterEntity()
        if (spellCaster != null) {
            val position = state.position
            // Create particles at the pre and post teleport position
            // Play sound at the pre and post teleport position
            createParticlesAt(1, 3, position, spellCaster.dimension)
            world.playSound(
                null,
                position.x,
                position.y,
                position.z,
                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS,
                2.5f,
                1.0f
            )

            spellCaster.setPositionAndUpdate(position.x, position.y, position.z)

            createParticlesAt(1, 3, position, spellCaster.dimension)
            world.playSound(
                null,
                position.x,
                position.y,
                position.z,
                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS,
                2.5f,
                1.0f
            )
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 3.0
    }
}