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
import net.minecraft.util.math.vector.Vector3d
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
                .withBaseName(getUnlocalizedPropertyBaseName("duration"))
                .withSetter(this::setDuration)
                .withGetter(this::getDuration)
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
                    freezeData.freezeTicks = getDuration(instance)
                    freezeData.freezePosition = Vector3d(entity.x, entity.y, entity.z)
                    freezeData.freezeYaw = entity.yRot
                    freezeData.freezePitch = entity.xRot
                } else {
                    entity.addEffect(EffectInstance(Effects.MOVEMENT_SLOWDOWN, getDuration(instance), 99))
                }
                createParticlesAround(5, 10, entity.position(), entity.level.dimension(), ModParticles.FREEZE, 1.0)
            }
        } else {
            val hitBlock = world.getBlockState(blockPos)
            if (hitBlock.block == Blocks.WATER) {
                world.setBlockAndUpdate(blockPos, Blocks.ICE.defaultBlockState())
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
        val freezeDuration = getDuration(instance) / 20.0
        return 25.0 + freezeDuration * freezeDuration * 5.0
    }

    fun setDuration(instance: SpellComponentInstance<*>, duration: Int) {
        instance.data.putInt(NBT_DURATION, duration)
    }

    fun getDuration(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_DURATION)
    }

    companion object {
        // NBT constants for freeze duration
        private const val NBT_DURATION = "duration"
    }
}