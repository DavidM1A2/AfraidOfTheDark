package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellFreezeData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.block.Blocks
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import kotlin.math.max

/**
 * Spell effect that causes water to freeze and creates ice
 *
 * @constructor initializes properties
 */
class FreezeSpellEffect : AOTDDurationSpellEffect(ResourceLocation(Constants.MOD_ID, "freeze"), ModResearches.ELEMENTAL_MAGIC, 1, 1, 60) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val world: World = state.world
        val blockPos = state.blockPosition
        val entity = state.entity

        // If the entity hit is living freeze it in place
        if (entity != null) {
            // We can only freeze a living entity
            if (entity is LivingEntity) {
                // If we hit a player, freeze their position and direction
                if (entity is PlayerEntity) {
                    val freezeData = entity.getSpellFreezeData()
                    freezeData.freezeTicks = max(freezeData.freezeTicks, getDuration(instance) * 20)
                    freezeData.freezePosition = Vector3d(entity.x, entity.y, entity.z)
                    freezeData.freezeYaw = entity.yRot
                    freezeData.freezePitch = entity.xRot
                } else {
                    entity.addEffect(EffectInstance(Effects.MOVEMENT_SLOWDOWN, getDuration(instance) * 20, 99))
                }
                createParticlesAround(5, 10, entity.position(), entity.level.dimension(), ModParticles.FREEZE, 1.0)
            }
        } else {
            val hitBlock = world.getBlockState(blockPos)
            if (hitBlock.block == Blocks.WATER || hitBlock.isAir) {
                world.setBlockAndUpdate(blockPos, Blocks.PACKED_ICE.defaultBlockState())
                createParticlesAround(5, 10, state.position, world.dimension(), ModParticles.FREEZE, 1.0)
            }
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        // Freezing a block costs 3
        val baseCost = 3.0
        // Each second of duration costs 3.0, but the first 1 seconds are free
        val durationCost = ((getDuration(instance) - 1) * 3.0).coerceAtLeast(0.0)
        return baseCost + durationCost * durationCost
    }
}