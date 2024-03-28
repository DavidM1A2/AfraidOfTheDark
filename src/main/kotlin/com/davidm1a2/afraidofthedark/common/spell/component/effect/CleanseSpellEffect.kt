package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellCharmData
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellFreezeData
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.particle.CleanseParticleData
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.ProcResult
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import kotlin.math.max
import kotlin.random.Random

/**
 * The cleanse spell effect clears your spell effects
 */
class CleanseSpellEffect : AOTDSpellEffect("cleanse", ModResearches.ADVANCED_MAGIC) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.booleanProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("beneficial_potion_effects"))
                .withDefaultValue(true)
                .withSetter(this::setBeneficialPotionEffects)
                .withGetter(this::getBeneficialPotionEffects)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.booleanProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("neutral_potion_effects"))
                .withDefaultValue(true)
                .withSetter(this::setNeutralPotionEffects)
                .withGetter(this::getNeutralPotionEffects)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.booleanProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("harmful_potion_effects"))
                .withDefaultValue(true)
                .withSetter(this::setHarmfulPotionEffects)
                .withGetter(this::getHarmfulPotionEffects)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.booleanProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("spell_effects"))
                .withDefaultValue(true)
                .withSetter(this::setSpellEffects)
                .withGetter(this::getSpellEffects)
                .build()
        )
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>): ProcResult {
        val entity = state.entity
        if (entity != null) {
            // Clear potion effects
            if (entity is LivingEntity) {
                val effectTypesToClear = mutableSetOf<MobEffectCategory>()
                if (getBeneficialPotionEffects(instance)) {
                    effectTypesToClear.add(MobEffectCategory.BENEFICIAL)
                }
                if (getNeutralPotionEffects(instance)) {
                    effectTypesToClear.add(MobEffectCategory.NEUTRAL)
                }
                if (getHarmfulPotionEffects(instance)) {
                    effectTypesToClear.add(MobEffectCategory.HARMFUL)
                }

                entity.activeEffects.map { it.effect }.forEach {
                    if (it.category in effectTypesToClear) {
                        entity.removeEffect(it)
                    }
                }
            }

            // Unfreeze and uncharm the player
            if (entity is Player && getSpellEffects(instance)) {
                val freezeData = entity.getSpellFreezeData()
                freezeData.freezeTicks = 0
                freezeData.sync(entity)

                val charmData = entity.getSpellCharmData()
                charmData.charmTicks = 0
            }

            // Show cleanse particles spinning around the hit entity
            val startOffset = Random.nextFloat() * 360
            val radius = max(0.1f, entity.bbWidth / 2 * 1.5f)
            val particles = listOf(
                CleanseParticleData(entity.id, startOffset + 0f, radius),
                CleanseParticleData(entity.id, startOffset + 90f, radius),
                CleanseParticleData(entity.id, startOffset + 180f, radius),
                CleanseParticleData(entity.id, startOffset + 270f, radius)
            )

            createParticlesAt(
                state, ParticlePacket.builder()
                    .particles(particles)
                    .position(Vec3(entity.x, entity.y, entity.z))
                    .speed(Vec3.ZERO)
                    .build()
            )
        } else {
            return ProcResult.failure()
        }
        return ProcResult.success()
    }

    /**
     * Gets the cost of the effect
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the effect
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        val beneficialEffectCost = if (getBeneficialPotionEffects(instance)) 5.0 else 0.0
        val neutralEffectCost = if (getNeutralPotionEffects(instance)) 5.0 else 0.0
        val harmfulEffectCost = if (getHarmfulPotionEffects(instance)) 5.0 else 0.0
        val spellEffectCost = if (getSpellEffects(instance)) 10.0 else 0.0
        return spellEffectCost + beneficialEffectCost + neutralEffectCost + harmfulEffectCost
    }

    fun setBeneficialPotionEffects(instance: SpellComponentInstance<*>, value: Boolean) {
        instance.data.putBoolean(NBT_BENEFICIAL_POTION_EFFECTS, value)
    }

    fun getBeneficialPotionEffects(instance: SpellComponentInstance<*>): Boolean {
        return instance.data.getBoolean(NBT_BENEFICIAL_POTION_EFFECTS)
    }

    fun setNeutralPotionEffects(instance: SpellComponentInstance<*>, value: Boolean) {
        instance.data.putBoolean(NBT_NEUTRAL_POTION_EFFECTS, value)
    }

    fun getNeutralPotionEffects(instance: SpellComponentInstance<*>): Boolean {
        return instance.data.getBoolean(NBT_NEUTRAL_POTION_EFFECTS)
    }

    fun setHarmfulPotionEffects(instance: SpellComponentInstance<*>, value: Boolean) {
        instance.data.putBoolean(NBT_HARMFUL_POTION_EFFECTS, value)
    }

    fun getHarmfulPotionEffects(instance: SpellComponentInstance<*>): Boolean {
        return instance.data.getBoolean(NBT_HARMFUL_POTION_EFFECTS)
    }

    fun setSpellEffects(instance: SpellComponentInstance<*>, value: Boolean) {
        instance.data.putBoolean(NBT_SPELL_EFFECTS, value)
    }

    fun getSpellEffects(instance: SpellComponentInstance<*>): Boolean {
        return instance.data.getBoolean(NBT_SPELL_EFFECTS)
    }

    companion object {
        private const val NBT_BENEFICIAL_POTION_EFFECTS = "beneficial_potion_effects"
        private const val NBT_NEUTRAL_POTION_EFFECTS = "neutral_potion_effects"
        private const val NBT_HARMFUL_POTION_EFFECTS = "harmful_potion_effects"
        private const val NBT_SPELL_EFFECTS = "spell_effects"
    }
}