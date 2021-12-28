package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.capabilities.getWardedBlockMap
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.ChunkPos

class WardSpellEffect : AOTDDurationSpellEffect(ResourceLocation(Constants.MOD_ID, "ward"), null, 0, 0) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("strength"))
                .withSetter(this::setStrength)
                .withGetter(this::getStrength)
                .withMinValue(1)
                .withMaxValue(MAX_STRENGTH)
                .withDefaultValue(1)
                .build()
        )
    }

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val entityHit = state.entity
        val world = state.world
        if (entityHit is LivingEntity) {
            createParticlesAt(2, 6, state.position, world.dimension(), ModParticles.SPELL_HIT)
            entityHit.addEffect(EffectInstance(Effects.DAMAGE_RESISTANCE, getDuration(instance), getStrength(instance) - 1))
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
                wardedBlockMap.wardBlock(blockPosition, getStrength(instance).toByte())
                wardedBlockMap.sync(world, chunkPos, blockPos = blockPosition)
            }
        }
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return getStrength(instance) * 7.0 + getDuration(instance) * 0.1
    }

    fun setStrength(instance: SpellComponentInstance<*>, amount: Int) {
        instance.data.putInt(NBT_STRENGTH, amount)
    }

    fun getStrength(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_STRENGTH)
    }

    companion object {
        // NBT constants for strength
        private const val NBT_STRENGTH = "strength"

        const val MAX_STRENGTH = 10
    }
}