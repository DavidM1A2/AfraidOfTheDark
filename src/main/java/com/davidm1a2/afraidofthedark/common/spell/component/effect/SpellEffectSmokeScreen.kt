package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.SyncParticle
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint
import java.util.*

/**
 * Creates a smoke screen at a given effect location
 *
 * @constructor adds the editable prop
 */
class SpellEffectSmokeScreen : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "smoke_screen")) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withName("Smoke Density")
                .withDescription("The number of particles present in the smoke screen.")
                .withSetter { instance, newValue -> instance.data.setInteger(NBT_SMOKE_DENSITY, newValue) }
                .withGetter { it.data.getInteger(NBT_SMOKE_DENSITY) }
                .withDefaultValue(10)
                .withMinValue(1)
                .build()
        )
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val position = state.position
        val positions: MutableList<Vec3d> = ArrayList()

        // Create smokeDensity random smoke particles
        for (i in 0 until getSmokeDensity(instance)) {
            positions.add(position.addVector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5))
        }

        AfraidOfTheDark.INSTANCE.packetHandler.sendToAllAround(
            SyncParticle(
                AOTDParticleRegistry.ParticleTypes.SMOKE_SCREEN_ID,
                positions,
                Collections.nCopies(positions.size, Vec3d.ZERO)
            ),
            TargetPoint(state.world.provider.dimension, position.x, position.y, position.z, 100.0)
        )
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return getSmokeDensity(instance) / 5.0
    }

    /**
     * The amount of smoke density this effect gives
     *
     * @param instance The instance of the smoke screen spell
     * @return amount of smoke density this effect gives
     */
    fun getSmokeDensity(instance: SpellComponentInstance<SpellEffect>): Int {
        return instance.data.getInteger(NBT_SMOKE_DENSITY)
    }

    companion object {
        // NBT constants for smoke density
        private const val NBT_SMOKE_DENSITY = "smoke_density"
    }
}