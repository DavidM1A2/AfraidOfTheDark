package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.play.server.SEntityVelocityPacket

class PushSpellEffect : AOTDSpellEffect("push", ModResearches.CLOAK_OF_AGILITY) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.floatProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("strength"))
                .withSetter(this::setStrength)
                .withGetter(this::getStrength)
                .withDefaultValue(8f)
                .build()
        )
    }

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val exactPosition = state.position
        val world = state.world
        // Divide by 10 to make it roughly the number of blocks to move
        val strength = getStrength(instance) / 10.0
        val entityHit = state.entity
        createParticlesAt(2, 6, exactPosition, world.dimension(), ModParticles.FLY)
        if (entityHit != null) {
            val pushDirection = state.direction.scale(strength)
            entityHit.push(pushDirection.x, pushDirection.y, pushDirection.z)
            if (entityHit.deltaMovement.y >= 0) {
                entityHit.fallDistance = 0f
            }
            if (entityHit is ServerPlayerEntity) {
                entityHit.connection.send(SEntityVelocityPacket(entityHit))
            }
        }
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return getStrength(instance) * 0.2
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