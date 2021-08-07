package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.block.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d

/**
 * Effect that sets fire to the hit target
 *
 * @constructor adds the editable prop
 */
class BurnSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "burn")) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withName("Burn")
                .withDescription("The number of seconds to set fire to when hitting entities.")
                .withSetter { instance, newValue -> instance.data.putInt(NBT_BURN_DURATION, newValue) }
                .withGetter { it.data.getInt(NBT_BURN_DURATION) }
                .withDefaultValue(2)
                .withMinValue(1)
                .withMaxValue(60)
                .build()
        )
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        if (state.getEntity() != null) {
            val entity = state.getEntity()
            createParticlesAt(5, 10, Vector3d(entity!!.x, entity.y, entity.z), entity.level.dimension(), ModParticles.FIRE)
            entity.remainingFireTicks = entity.remainingFireTicks + getBurnDuration(instance)
        } else {
            val world = state.world
            val position = state.blockPosition
            if (world.isEmptyBlock(position.above())) {
                if (!world.isEmptyBlock(position)) {
                    if (reducedParticles) {
                        createParticlesAround(0, 1, state.position, world.dimension(), ModParticles.FIRE, 0.5)
                    } else {
                        createParticlesAround(2, 4, state.position, world.dimension(), ModParticles.FIRE, 0.5)
                    }
                    world.setBlockAndUpdate(position.above(), Blocks.FIRE.defaultBlockState())
                }
            }
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 10.0 + getBurnDuration(instance) * 5.0
    }

    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    fun getBurnDuration(instance: SpellComponentInstance<SpellEffect>): Int {
        return instance.data.getInt(NBT_BURN_DURATION)
    }

    companion object {
        // NBT constants for burn duration
        private const val NBT_BURN_DURATION = "burn_duration"
    }
}