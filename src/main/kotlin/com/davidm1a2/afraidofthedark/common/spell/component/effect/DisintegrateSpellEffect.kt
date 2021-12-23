package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.block.Blocks
import net.minecraft.util.ResourceLocation

class DisintegrateSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "disintegrate"), ModResearches.MAGIC_MASTERY) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.floatProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("strength"))
                .withSetter(this::setStrength)
                .withGetter(this::getStrength)
                .withDefaultValue(4f)
                .withMinValue(0f)
                .build()
        )
    }

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val exactPosition = state.position
        val world = state.world
        val strength = getStrength(instance)
        val entityHit = state.entity
        createParticlesAt(2, 6, exactPosition, world.dimension(), ModParticles.SPELL_HIT)
        if (entityHit != null) {
            entityHit.hurt(ModDamageSources.getSpellDamage(state.casterEntity), strength)
        } else {
            val blockPosition = state.blockPosition
            val blockHit = world.getBlockState(blockPosition)
            if (blockHit.harvestLevel <= strength && blockHit.getDestroySpeed(world, blockPosition) != -1f) {
                world.setBlockAndUpdate(blockPosition, Blocks.AIR.defaultBlockState())
            }
        }
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return getStrength(instance) * 2.0
    }

    fun setStrength(instance: SpellComponentInstance<*>, amount: Float) {
        instance.data.putFloat(NBT_STRENGTH, amount)
    }

    fun getStrength(instance: SpellComponentInstance<*>): Float {
        return instance.data.getFloat(NBT_STRENGTH)
    }

    companion object {
        // NBT constants for strength
        private const val NBT_STRENGTH = "strength"
    }
}