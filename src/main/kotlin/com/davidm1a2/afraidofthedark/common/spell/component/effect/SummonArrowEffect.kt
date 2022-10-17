package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.particle.ArrowTrailParticleData
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.projectile.AbstractArrowEntity
import net.minecraft.entity.projectile.ArrowEntity
import kotlin.math.ceil

class SummonArrowEffect : AOTDSpellEffect("summon_arrow", ModResearches.WRIST_CROSSBOW) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.floatProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("speed"))
                .withSetter(this::setSpeed)
                .withGetter(this::getSpeed)
                .withDefaultValue(40f)
                .withMinValue(0f)
                // Turns out MC doesn't let things move faster than about 3.9 blocks/tick. This speed is hardcoded as part of the SEntityVelocityPacket :(
                .withMaxValue(75f)
                .build()
        )
    }

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        // Divide by 20 to convert speed per second to speed per tick
        val speed = getSpeed(instance) / 20f
        val world = state.world
        val position = state.position
        val direction = state.direction

        val arrowEntity = ArrowEntity(world, position.x, position.y, position.z)

        arrowEntity.owner = state.entity
        arrowEntity.shoot(direction.x, direction.y, direction.z, speed, 0f)
        arrowEntity.pickup = AbstractArrowEntity.PickupStatus.DISALLOWED
        world.addFreshEntity(arrowEntity)
        createParticlesAt(
            state, ParticlePacket.builder()
                .particles(List(ceil(speed * 5).toInt().coerceAtLeast(1)) { ArrowTrailParticleData(arrowEntity.id, it) })
                .position(position)
                .build()
        )
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 5.0
    }

    private fun setSpeed(instance: SpellComponentInstance<*>, speed: Float) {
        instance.data.putFloat(NBT_SPEED, speed)
    }

    private fun getSpeed(instance: SpellComponentInstance<*>): Float {
        return instance.data.getFloat(NBT_SPEED)
    }

    companion object {
        // NBT constants
        private const val NBT_SPEED = "speed"
    }
}