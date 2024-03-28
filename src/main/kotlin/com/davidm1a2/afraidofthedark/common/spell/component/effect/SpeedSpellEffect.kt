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
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import java.time.Duration
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.random.Random

class SpeedSpellEffect : AOTDDurationSpellEffect("speed", ModResearches.CLOAK_OF_AGILITY, 1.0, 10.0, Duration.ofMinutes(20).seconds.toDouble()) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("multiplier"))
                .withSetter(this::setMultiplier)
                .withGetter(this::getMultiplier)
                .withDefaultValue(1)
                .withMinValue(-6)
                .withMaxValue(10)
                .build()
        )
    }

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>): ProcResult {
        val exactPosition = state.position
        val entityHit = state.entity

        val multiplier = getMultiplier(instance)
        if (multiplier == 0) {
            return ProcResult.failure()
        }

        val duration = ceil(getDuration(instance) * 20).toInt()
        val effectType = if (multiplier >= 0) MobEffects.MOVEMENT_SPEED else MobEffects.MOVEMENT_SLOWDOWN
        val effectAmplifier = abs(multiplier) - 1
        val effect = MobEffectInstance(effectType, duration, effectAmplifier)

        if (entityHit is LivingEntity) {
            entityHit.addEffect(effect)
            val particleSpeeds = List(5) {
                Vec3.directionFromRotation(entityHit.xRot, entityHit.yRot)
                    .reverse()
                    // Remove the y component
                    .let { Vec3(it.x, (Random.nextDouble() * entityHit.bbHeight / 2 + entityHit.bbHeight / 2) * 0.25, it.z) }
                    // Normalize the direction
                    .normalize()
                    // Set the y component to upwards, perturb the x and z components
                    .let {
                        Vec3(
                            it.x * 0.2 + (Random.nextDouble() - 0.5) * 0.2,
                            (Random.nextDouble() * entityHit.bbHeight / 2 + entityHit.bbHeight / 2) * 0.25,
                            it.z * 0.2 + (Random.nextDouble() - 0.5) * 0.2
                        )
                    }
            }
            createParticlesAt(
                state, ParticlePacket.builder()
                    .particle(ModParticles.DUST_CLOUD)
                    .position(entityHit.position())
                    .speeds(particleSpeeds)
                    .build()
            )
        } else {
            val world = state.world
            val aoePotion = AreaEffectCloud(world, exactPosition.x, exactPosition.y, exactPosition.z)
            aoePotion.addEffect(effect)
            aoePotion.owner = state.casterEntity as? LivingEntity
            aoePotion.radiusPerTick = 0f
            aoePotion.duration = duration
            world.addFreshEntity(aoePotion)
            createParticlesAt(
                state, ParticlePacket.builder()
                    .particle(ModParticles.DUST_CLOUD)
                    .position(state.position)
                    .speed(
                        Vec3(Random.nextDouble() - 0.5, 0.0, Random.nextDouble() - 0.5)
                            .normalize()
                            .scale(0.2)
                            .add(0.0, Random.nextDouble() * 0.3 + 0.3, 0.0)
                    )
                    .build()
            )
        }
        return ProcResult.success()
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        // Each second of speed costs 0.25
        val durationCost = getDuration(instance) * 0.25
        // Each level of speed costs 1.0 per second
        val speedCostMultiplier = abs(getMultiplier(instance)) * 1.0
        return speedCostMultiplier * durationCost
    }

    fun setMultiplier(instance: SpellComponentInstance<*>, amount: Int) {
        instance.data.putInt(NBT_MULTIPLIER, amount)
    }

    fun getMultiplier(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_MULTIPLIER)
    }

    companion object {
        // NBT constants for speed multiplier
        private const val NBT_MULTIPLIER = "multiplier"
    }
}