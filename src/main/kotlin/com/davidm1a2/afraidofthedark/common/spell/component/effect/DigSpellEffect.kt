package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.ProcResult
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.core.BlockPos
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import java.time.Duration
import kotlin.math.abs
import kotlin.math.ceil

/**
 * Dig effect digs a block
 */
class DigSpellEffect : AOTDDurationSpellEffect("dig", ModResearches.APPRENTICE_ASCENDED, 0.0, FREE_DURATION, Duration.ofMinutes(20).seconds.toDouble()) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("speed"))
                .withSetter(this::setSpeed)
                .withGetter(this::getSpeed)
                .withDefaultValue(1)
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
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>): ProcResult {
        val world = state.world
        val entity = state.entity
        if (entity is LivingEntity) {
            val speed = getSpeed(instance)
            if (speed != 0) {
                val effectType = if (speed >= 0) MobEffects.DIG_SPEED else MobEffects.DIG_SLOWDOWN
                val effect = MobEffectInstance(effectType, ceil(getDuration(instance) * 20).toInt(), abs(speed) - 1)
                entity.addEffect(effect)
                createParticlesAt(
                    state, ParticlePacket.builder()
                        .particle(ModParticles.DIG)
                        .position(entity.position())
                        .iterations(8)
                        .build()
                )
            } else {
                return ProcResult.failure()
            }
        } else {
            // Digs the block at the position
            val position = state.blockPosition
            if (canBlockBeDestroyed(world, position)) {
                world.destroyBlock(position, true)
                createParticlesAt(
                    state, ParticlePacket.builder()
                        .particle(ModParticles.DIG)
                        .position(state.position)
                        .iterations(6)
                        .build()
                )
            } else {
                return ProcResult.failure()
            }
        }
        return ProcResult.success()
    }

    /**
     * Tests if a given block can be broken with a dig spell
     *
     * @param world    The world the block is in
     * @param blockPos The pos the block is at
     * @return True if the block can be destroyed, false otherwise
     */
    private fun canBlockBeDestroyed(world: Level, blockPos: BlockPos): Boolean {
        val blockState = world.getBlockState(blockPos)
        return blockState.getDestroySpeed(world, blockPos) != -1f && !blockState.isAir
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        val speed = getSpeed(instance)
        // Digging a block costs 1
        val baseCost = 1.0
        // Each second of duration costs 0.25, but the first 3 seconds are free
        val durationCost = ((getDuration(instance) - FREE_DURATION) * 0.25).coerceAtLeast(0.0)
        val speedCostMultiplier = abs(speed)
        return baseCost + speedCostMultiplier * durationCost
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

        private const val FREE_DURATION = 3.0
    }
}