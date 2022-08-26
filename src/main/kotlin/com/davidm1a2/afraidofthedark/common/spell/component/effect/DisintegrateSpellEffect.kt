package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class DisintegrateSpellEffect : AOTDSpellEffect("disintegrate", ModResearches.MAGIC_MASTERY) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.floatProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("strength"))
                .withSetter(this::setStrength)
                .withGetter(this::getStrength)
                .withDefaultValue(1f)
                .withMinValue(1f)
                .withMaxValue(1000f)
                .build()
        )
    }

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val exactPosition = state.position
        val world = state.world
        val strength = getStrength(instance)
        val entityHit = state.entity
        createParticlesAt(2, 6, exactPosition, world.dimension(), ModParticles.DISINTEGRATE)
        if (entityHit != null) {
            entityHit.hurt(ModDamageSources.getSpellDamage(state), strength)
        } else if (canBlockBeDestroyed(world, state.blockPosition)) {
            world.setBlockAndUpdate(state.blockPosition, Blocks.AIR.defaultBlockState())
        }
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return getStrength(instance) * 2.0 - 1
    }

    /**
     * Tests if a given block can be broken with a disintegrate spell
     *
     * @param world    The world the block is in
     * @param blockPos The pos the block is at
     * @return True if the block can be destroyed, false otherwise
     */
    private fun canBlockBeDestroyed(world: World, blockPos: BlockPos): Boolean {
        val blockState = world.getBlockState(blockPos)
        return blockState.getDestroySpeed(world, blockPos) != -1f
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