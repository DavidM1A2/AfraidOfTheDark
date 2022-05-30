package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import java.time.Duration
import kotlin.math.abs

class SpeedSpellEffect : AOTDDurationSpellEffect("speed", ModResearches.CLOAK_OF_AGILITY, 1, 10, Duration.ofMinutes(20).seconds.toInt()) {
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

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val exactPosition = state.position
        val entityHit = state.entity

        val multiplier = getMultiplier(instance)
        if (multiplier == 0) {
            return
        }

        val duration = getDuration(instance) * 20
        val effectType = if (multiplier >= 0) Effects.MOVEMENT_SPEED else Effects.MOVEMENT_SLOWDOWN
        val effectAmplifier = abs(multiplier) - 1
        val effect = EffectInstance(effectType, duration, effectAmplifier)

        if (entityHit is LivingEntity) {
            createParticlesAt(1, 3, exactPosition, entityHit.level.dimension(), ModParticles.SPELL_HIT)
            entityHit.addEffect(effect)
        } else {
            val world = state.world
            val aoePotion = AreaEffectCloudEntity(world, exactPosition.x, exactPosition.y, exactPosition.z)
            aoePotion.addEffect(effect)
            aoePotion.owner = state.casterEntity as? LivingEntity
            aoePotion.setRadiusPerTick(0f)
            aoePotion.duration = duration
            world.addFreshEntity(aoePotion)
            createParticlesAt(2, 6, exactPosition, world.dimension(), ModParticles.SPELL_HIT)
        }
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