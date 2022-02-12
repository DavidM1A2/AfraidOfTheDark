package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.capabilities.getWardedBlockMap
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.helper.WardStrength
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.ChunkPos

class WardSpellEffect : AOTDDurationSpellEffect(ResourceLocation(Constants.MOD_ID, "ward"), ModResearches.SPELLMASON, 0, 5) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.enumProperty<WardStrength>()
                .withBaseName(getUnlocalizedPropertyBaseName("strength"))
                .withSetter(this::setStrength)
                .withGetter(this::getStrength)
                .withDefaultValue(WardStrength.MEDIUM)
                .build()
        )
    }

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val entityHit = state.entity
        val world = state.world
        if (entityHit is LivingEntity) {
            createParticlesAt(2, 6, state.position, world.dimension(), ModParticles.SPELL_HIT)
            entityHit.addEffect(EffectInstance(Effects.DAMAGE_RESISTANCE, getDuration(instance) * 20, getStrength(instance).ordinal))
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
        // Base cost is 7 vitae
        val baseCost = 7.0
        // 7 cost per strength/second
        val strengthCost = (getStrength(instance).ordinal + 1) * 7.0
        // Each second of duration costs 0.5, but the first 5 seconds are free
        val durationCost = 1.0 + ((getDuration(instance) - 5.0) * 0.5).coerceAtLeast(0.0)
        return baseCost + strengthCost * durationCost
    }

    fun setStrength(instance: SpellComponentInstance<*>, strength: WardStrength) {
        instance.data.putInt(NBT_STRENGTH, strength.ordinal)
    }

    fun getStrength(instance: SpellComponentInstance<*>): WardStrength {
        return WardStrength.from(instance.data.getInt(NBT_STRENGTH))
    }

    companion object {
        // NBT constants for strength
        private const val NBT_STRENGTH = "strength"
    }
}