package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.math.abs
import kotlin.random.Random

/**
 * Dig effect digs a block
 */
class DigSpellEffect : AOTDDurationSpellEffect(ResourceLocation(Constants.MOD_ID, "dig"), ModResearches.SPELLMASON, 0, 0) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("speed"))
                .withSetter(this::setSpeed)
                .withGetter(this::getSpeed)
                .withDefaultValue(0)
                .withMinValue(-10)
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
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val world = state.world
        val entity = state.getEntity()
        val min = if (reducedParticles) 0 else 5
        val max = if (reducedParticles) 1 else 10
        if (entity is LivingEntity) {
            createParticlesAt(min, max, state.position, world.dimension(), ModParticles.DIG)
            val speed = getSpeed(instance)
            if (speed != 0) {
                val effectType = if (speed >= 0) Effects.DIG_SPEED else Effects.DIG_SLOWDOWN
                val effect = EffectInstance(effectType, getDuration(instance), abs(speed) - 1)
                entity.addEffect(effect)
            }
        } else {
            // Digs the block at the position
            val position = state.blockPosition
            if (canBlockBeDestroyed(world, position)) {
                if (!reducedParticles) {
                    createParticlesAt(min, max, state.position, world.dimension(), ModParticles.DIG)
                } else if (Random.nextDouble() > 0.6) {
                    createParticlesAt(min, max, state.position, world.dimension(), ModParticles.DIG)
                }
                world.destroyBlock(position, true)
            }
        }
    }

    /**
     * Tests if a given block can be broken with a dig spell
     *
     * @param world    The world the block is in
     * @param blockPos The pos the block is at
     * @return True if the block can be destroyed, false otherwise
     */
    private fun canBlockBeDestroyed(world: World, blockPos: BlockPos): Boolean {
        val blockState = world.getBlockState(blockPos)
        return blockState.getDestroySpeed(world, blockPos) != -1f
    }

    /**
     * Gets the cost of the effect given the state of the effect
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of dig is 14
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        val speed = getSpeed(instance)
        return 10.0 + if (speed != 0) speed * 15 + getDuration(instance) * 0.1 else 0.0
    }

    fun setSpeed(instance: SpellComponentInstance<*>, amount: Int) {
        instance.data.putInt(NBT_SPEED, amount)
    }

    fun getSpeed(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_SPEED)
    }

    companion object {
        // NBT constants for dig speed
        private const val NBT_SPEED = "speed"
    }
}