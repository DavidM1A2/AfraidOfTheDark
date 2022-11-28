package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.capabilities.getWardedBlockMap
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.particle.ShieldParticleData
import com.davidm1a2.afraidofthedark.common.particle.WardParticleData
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.Direction
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.vector.Vector3d
import java.time.Duration
import kotlin.math.ceil

class WardSpellEffect : AOTDDurationSpellEffect("ward", ModResearches.APPRENTICE_ASCENDED, 0.0, FREE_DURATION, Duration.ofMinutes(20).seconds.toDouble()) {
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
            val durationTicks = ceil(getDuration(instance) * 20).toInt()
            entityHit.addEffect(EffectInstance(Effects.DAMAGE_RESISTANCE, durationTicks, (getStrength(instance) - 1).coerceAtMost(3)))
            val particles = List(4) { ShieldParticleData(entityHit.id, it * 90f, entityHit.bbWidth * 1.2f, durationTicks) }
            createParticlesAt(
                state, ParticlePacket.builder()
                    .particles(particles)
                    .position(entityHit.position())
                    .build()
            )
        } else if (entityHit == null) {
            val blockPosition = state.blockPosition
            val blockHit = world.getBlockState(blockPosition)
            // Don't ward air
            if (!blockHit.isAir) {
                val chunkPos = ChunkPos(blockPosition.x shr 4, blockPosition.z shr 4)
                val chunk = world.getChunk(chunkPos.x, chunkPos.z)
                val wardedBlockMap = chunk.getWardedBlockMap()
                wardedBlockMap.wardBlock(blockPosition, getStrength(instance))
                wardedBlockMap.sync(world, chunkPos, blockPos = blockPosition)
                val particlePositions = Direction.values().map {
                    Vector3d(blockPosition.x + 0.5, blockPosition.y + 0.5, blockPosition.z + 0.5)
                        .add(it.stepX * 0.505, it.stepY * 0.505, it.stepZ * 0.505)
                }
                val particles = Direction.values().map { WardParticleData(it) }
                createParticlesAt(
                    state, ParticlePacket.builder()
                        .particles(particles)
                        .positions(particlePositions)
                        .build()
                )
            } else {
                createFizzleParticleAt(state)
            }
        } else {
            createFizzleParticleAt(state)
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

        private const val FREE_DURATION = 3.0
    }
}