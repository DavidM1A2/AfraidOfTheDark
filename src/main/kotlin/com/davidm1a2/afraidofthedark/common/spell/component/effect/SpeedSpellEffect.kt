package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.ResourceLocation
import kotlin.math.abs

class SpeedSpellEffect : AOTDDurationSpellEffect(ResourceLocation(Constants.MOD_ID, "speed"), null, 1, 20) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("multiplier"))
                .withSetter(this::setMultiplier)
                .withGetter(this::getMultiplier)
                .withDefaultValue(1)
                .withMinValue(-10)
                .withMaxValue(10)
                .build()
        )
    }

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val exactPosition = state.position
        val entityHit = state.entity

        val multiplier = getMultiplier(instance)
        val effectType = if (multiplier >= 0) Effects.MOVEMENT_SPEED else Effects.MOVEMENT_SLOWDOWN
        val effectAmplifier = if (multiplier == 0) 0 else abs(multiplier) - 1
        val effect = EffectInstance(effectType, getDuration(instance), effectAmplifier)

        if (entityHit is LivingEntity) {
            createParticlesAt(1, 3, exactPosition, entityHit.level.dimension(), ModParticles.SPELL_HIT)
            entityHit.addEffect(effect)
        } else {
            val world = state.world
            val aoePotion = AreaEffectCloudEntity(world, exactPosition.x, exactPosition.y, exactPosition.z)
            aoePotion.addEffect(effect)
            aoePotion.owner = state.casterEntity as? LivingEntity
            aoePotion.setRadiusPerTick(0f)
            aoePotion.duration = getDuration(instance)
            world.addFreshEntity(aoePotion)
            createParticlesAt(2, 6, exactPosition, world.dimension(), ModParticles.SPELL_HIT)
        }
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return abs(getMultiplier(instance)) * 4 + getDuration(instance) * 0.1
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