package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellFreezeData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.block.Blocks
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

/**
 * Spell effect that causes water to freeze and creates ice
 *
 * @constructor initializes properties
 */
class FreezeSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "freeze")) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withName("Duration")
                .withDescription("The number of ticks the freeze will last against entities.")
                .withSetter { instance, newValue -> instance.data.putInt(NBT_FREEZE_DURATION, newValue) }
                .withGetter { it.data.getInt(NBT_FREEZE_DURATION) }
                .withDefaultValue(20)
                .withMinValue(1)
                .withMaxValue(1200)
                .build()
        )
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val world: World = state.world
        val blockPos = state.blockPosition
        val entity = state.getEntity()

        // If the entity hit is living freeze it in place
        if (entity != null) {
            // We can only freeze a living entity
            if (entity is LivingEntity) {
                // If we hit a player, freeze their position and direction
                if (entity is PlayerEntity) {
                    val freezeData = entity.getSpellFreezeData()
                    freezeData.freezeTicks = getFreezeDuration(instance)
                    freezeData.freezePosition = Vec3d(entity.posX, entity.posY, entity.posZ)
                    freezeData.freezeYaw = entity.rotationYaw
                    freezeData.freezePitch = entity.rotationPitch
                } else {
                    entity.addPotionEffect(EffectInstance(Effects.SLOWNESS, getFreezeDuration(instance), 99))
                }
                createParticlesAround(5, 10, entity.positionVector, entity.dimension, ModParticles.FREEZE, 1.0)
            }
        } else {
            val hitBlock = world.getBlockState(blockPos)
            if (hitBlock.block == Blocks.WATER) {
                world.setBlockState(blockPos, Blocks.ICE.defaultState)
                createParticlesAround(5, 10, state.position, world.dimension.type, ModParticles.FREEZE, 1.0)
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
        val freezeDuration = getFreezeDuration(instance)
        return 25.0 + freezeDuration * freezeDuration * 5.0
    }

    fun setFreezeDuration(instance: SpellComponentInstance<SpellEffect>, duration: Int) {
        instance.data.putInt(NBT_FREEZE_DURATION, duration)
    }

    /**
     * Gets the freeze duration in ticks
     *
     * @param instance The instance of the freeze effect
     * @return The freeze duration in ticks
     */
    fun getFreezeDuration(instance: SpellComponentInstance<SpellEffect>): Int {
        return instance.data.getInt(NBT_FREEZE_DURATION)
    }

    companion object {
        // NBT constants for freeze duration
        private const val NBT_FREEZE_DURATION = "freeze_duration"
    }
}