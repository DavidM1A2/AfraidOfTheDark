package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.fml.network.PacketDistributor
import java.util.Collections

/**
 * Creates a smoke screen at a given effect location
 *
 * @constructor adds the editable prop
 */
class SmokeScreenSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "smoke_screen"), ModResearches.POCKET_DIMENSION) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("density"))
                .withSetter(this::setDensity)
                .withGetter(this::getDensity)
                .withDefaultValue(6)
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
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val position = state.position
        val positions: MutableList<Vector3d> = ArrayList()

        // Create smokeDensity random smoke particles
        for (i in 0 until getDensity(instance)) {
            positions.add(position.add(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5))
        }

        AfraidOfTheDark.packetHandler.sendToAllAround(
            ParticlePacket(
                ModParticles.SMOKE_SCREEN,
                positions,
                Collections.nCopies(positions.size, Vector3d.ZERO)
            ),
            PacketDistributor.TargetPoint(position.x, position.y, position.z, 100.0, state.world.dimension())
        )
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        // 1 vitae just to use smoke screen
        val baseCost = 1.0
        // Each additional smoke particle adds 1.5 vitae
        return baseCost + getDensity(instance) * 1.5
    }

    fun setDensity(instance: SpellComponentInstance<*>, density: Int) {
        instance.data.putInt(NBT_DENSITY, density)
    }

    fun getDensity(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_DENSITY)
    }

    companion object {
        // NBT constants for smoke density
        private const val NBT_DENSITY = "density"
    }
}