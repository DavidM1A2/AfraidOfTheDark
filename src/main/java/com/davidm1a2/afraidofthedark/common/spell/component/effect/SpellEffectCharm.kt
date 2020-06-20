package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellCharmData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Particles
import net.minecraft.util.ResourceLocation
import java.util.concurrent.ThreadLocalRandom

/**
 * Effect that forces players to walk towards you or animals to mate
 *
 * @constructor adds the editable prop
 */
class SpellEffectCharm : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "charm")) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withName("Charm Duration")
                .withDescription("The number of ticks to charm to when hitting players.")
                .withSetter { instance, newValue -> instance.data.setInt(NBT_CHARM_DURATION, newValue) }
                .withGetter { it.data.getInt(NBT_CHARM_DURATION) }
                .withDefaultValue(40)
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
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val entity = state.getEntity()
        val spellOwner = state.getCasterEntity()
        // If we hit an entity that is an animal set them in love
        if (entity is EntityAnimal) {
            entity.setInLove(spellOwner as? EntityPlayer)
        } else if (entity is EntityPlayer && spellOwner != null) {
            // Grab the player's charm data
            val spellCharmData = entity.getSpellCharmData()
            // Charm them for the "charm duration"
            spellCharmData.charmTicks = getCharmDuration(instance)

            // Set the charming entity
            spellCharmData.charmingEntityId = spellOwner.uniqueID
            val random = ThreadLocalRandom.current()
            val width = entity.width.toDouble()
            val height = entity.height.toDouble()

            // Spawn 4 random heart particles
            for (i in 0..3) {
                state.world.spawnParticle(
                    Particles.HEART,
                    // The position will be somewhere inside the player's hitbox
                    state.position.x + random.nextFloat() * width * 2.0f - width,
                    state.position.y + 0.5 + random.nextFloat() * height,
                    state.position.z + random.nextFloat() * width * 2.0f - width,
                    // Spawn one particle
                    1,
                    // Randomize velocity
                    random.nextGaussian() * 0.02,
                    random.nextGaussian() * 0.02,
                    random.nextGaussian() * 0.02,
                    0.02
                )
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
        return 10.0 + getCharmDuration(instance)
    }

    /**
     * The charm duration this effect gives
     *
     * @param instance The instance of the spell effect to grab the charm duration from
     * @return The duration of the charm in ticks
     */
    private fun getCharmDuration(instance: SpellComponentInstance<SpellEffect>): Int {
        return instance.data.getInt(NBT_CHARM_DURATION)
    }

    companion object {
        // NBT constants for charm duration
        private const val NBT_CHARM_DURATION = "charm_duration"
    }
}