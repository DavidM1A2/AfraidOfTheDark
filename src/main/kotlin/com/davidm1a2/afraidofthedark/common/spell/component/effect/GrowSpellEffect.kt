package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.block.IGrowable

/**
 * Spell effect that causes growable blocks to grow
 */
class GrowSpellEffect : AOTDSpellEffect("grow", ModResearches.SPELLMASON) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("strength"))
                .withSetter(this::setStrength)
                .withGetter(this::getStrength)
                .withDefaultValue(1)
                .withMinValue(1)
                .withMaxValue(10)
                .build()
        )
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val world = state.world
        var position = state.blockPosition
        var blockState = world.getBlockState(position)

        // If we hit a block that crops might be on check the block above and below and to if we can grow on that instead
        if (blockState.block !is IGrowable) {
            position = position.above()
            blockState = world.getBlockState(position)
            if (blockState.block !is IGrowable) {
                position = position.below(2)
                blockState = world.getBlockState(position)
            }
        }

        // Grob the block at the current position if it's a type 'IGrowable'
        for (ignored in 0 until getStrength(instance)) {
            if (blockState.block is IGrowable) {
                createParticlesAround(8, 16, state.position, world.dimension(), ModParticles.GROW, 2.0)
                (blockState.block as IGrowable).performBonemeal(world, world.random, position, blockState)
                blockState = world.getBlockState(position)
            } else {
                break
            }
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return getStrength(instance) * 3.0
    }

    fun setStrength(instance: SpellComponentInstance<*>, strength: Int) {
        instance.data.putInt(NBT_STRENGTH, strength)
    }

    fun getStrength(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_STRENGTH)
    }

    companion object {
        // NBT constants
        private const val NBT_STRENGTH = "strength"
    }
}