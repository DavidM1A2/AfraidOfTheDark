package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.capabilities.getWardedBlockMap
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
import net.minecraft.util.math.ChunkPos
import java.time.Duration

class WardSpellEffect : AOTDDurationSpellEffect("ward", ModResearches.APPRENTICE_ASCENDED, 0, FREE_DURATION, Duration.ofMinutes(20).seconds.toInt()) {
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

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val entityHit = state.entity
        val world = state.world
        if (entityHit is LivingEntity) {
            createParticlesAt(2, 6, state.position, world.dimension(), ModParticles.SPELL_HIT)
            entityHit.addEffect(EffectInstance(Effects.DAMAGE_RESISTANCE, getDuration(instance) * 20, (getStrength(instance) - 1).coerceAtMost(3)))
        }
        if (entityHit == null) {
            val blockPosition = state.blockPosition
            val blockHit = world.getBlockState(blockPosition)
            // Don't ward air
            if (!blockHit.isAir) {
                createParticlesAt(2, 6, state.position, world.dimension(), ModParticles.SPELL_HIT)
                val chunkPos = ChunkPos(blockPosition.x shr 4, blockPosition.z shr 4)
                val chunk = world.getChunk(chunkPos.x, chunkPos.z)
                val wardedBlockMap = chunk.getWardedBlockMap()
                wardedBlockMap.wardBlock(blockPosition, getStrength(instance))
                wardedBlockMap.sync(world, chunkPos, blockPos = blockPosition)
            }
        }
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        val baseCost = 9.0
        // 2^(n-1) cost multiplier, since each level of strength is 2x as good as the previous
        val strengthCostMultiplier = 1 shl (getStrength(instance) - 1)
        // Each second of duration costs 0.5, but the first 3 seconds are free
        val durationCost = 1.0 + ((getDuration(instance) - FREE_DURATION) * 0.5).coerceAtLeast(0.0)
        return baseCost + strengthCostMultiplier * durationCost
    }

    fun setStrength(instance: SpellComponentInstance<*>, strength: Int) {
        instance.data.putInt(NBT_STRENGTH, strength)
    }

    fun getStrength(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_STRENGTH)
    }

    companion object {
        // NBT constants for strength
        private const val NBT_STRENGTH = "strength"

        private const val FREE_DURATION = 3
    }
}