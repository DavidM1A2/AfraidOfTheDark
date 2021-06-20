package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentProperty
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.potion.Effect
import net.minecraft.potion.EffectInstance
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.registries.ForgeRegistries
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Spell effect that applies potion effects
 *
 * @constructor adds the editable props
 */
class PotionEffectSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "potion_effect")) {
    init {
        addEditableProperty(
            SpellComponentProperty(
                "Potion Type",
                "The type of potion effect to apply. Must be using the minecraft naming convention, like 'minecraft:speed'.",
                { instance, newValue ->
                    // Grab the potion associated with the text
                    val type = ForgeRegistries.POTIONS.getValue(ResourceLocation(newValue))
                    // If type is not null it's a valid potion type so we store it, otherwise throw an exception
                    if (type != null) {
                        instance.data.putString(NBT_POTION_TYPE, type.registryName.toString())
                    } else {
                        throw InvalidValueException("Invalid potion type $newValue, it was not found in the registry.")
                    }
                },
                { it.data.getString(NBT_POTION_TYPE) },
                { it.data.putString(NBT_POTION_TYPE, "speed") }
            )
        )
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withName("Potion Strength")
                .withDescription("The level of the potion to apply, ex. 4 means apply 'Potion Type' at level 4.")
                .withSetter { instance, newValue -> instance.data.putInt(NBT_POTION_STRENGTH, newValue - 1) }
                .withGetter { it.data.getInt(NBT_POTION_STRENGTH) + 1 }
                .withDefaultValue(1)
                .withMinValue(1)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withName("Potion Duration")
                .withDescription("The number of ticks the potion effect should run for.")
                .withSetter { instance, newValue -> instance.data.putInt(NBT_POTION_DURATION, newValue) }
                .withGetter { it.data.getInt(NBT_POTION_DURATION) }
                .withDefaultValue(20)
                .withMinValue(1)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.floatProperty()
                .withName("Potion Cloud Radius")
                .withDescription("The size of the potion cloud if the potion is delivered to a block and not an entity.")
                .withSetter { instance, newValue -> instance.data.putFloat(NBT_POTION_RADIUS, newValue) }
                .withGetter { it.data.getFloat(NBT_POTION_RADIUS) }
                .withDefaultValue(2f)
                .withMinValue(0f)
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
        val exactPosition = state.position
        val potionType = getPotionType(instance)
        val potionStrength = getPotionStrength(instance)
        val potionDuration = getPotionDuration(instance)

        // If we hit an entity just apply the potion effect
        val entityHit = state.getEntity()
        if (entityHit != null) {
            if (entityHit is LivingEntity) {
                createParticlesAt(1, 3, exactPosition, entityHit.dimension, ModParticles.SPELL_HIT)
                entityHit.addPotionEffect(EffectInstance(potionType, potionDuration, potionStrength))
            }
        } else {
            val world: World = state.world
            val aoePotion = AreaEffectCloudEntity(world, exactPosition.x, exactPosition.y, exactPosition.z)
            aoePotion.addEffect(EffectInstance(potionType, potionDuration, potionStrength))
            aoePotion.owner = state.getCasterEntity() as? LivingEntity
            aoePotion.radius = getPotionRadius(instance)
            aoePotion.setRadiusPerTick(0f)
            aoePotion.duration = potionDuration
            world.addEntity(aoePotion)
            createParticlesAt(4, 8, exactPosition, world.dimension.type, ModParticles.SPELL_HIT)
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 15 + getPotionDuration(instance) / 5.0 * getPotionStrength(instance) * max(
            sqrt(getPotionRadius(instance)),
            1.0f
        )
    }

    fun setPotionType(instance: SpellComponentInstance<SpellEffect>, effect: Effect) {
        instance.data.putString(NBT_POTION_TYPE, effect.registryName.toString())
    }

    /**
     * Gets the potion type for the given spell instance
     *
     * @param instance The instance of the potion type
     * @return The potion for the instance
     */
    fun getPotionType(instance: SpellComponentInstance<SpellEffect>): Effect {
        return ForgeRegistries.POTIONS.getValue(ResourceLocation(instance.data.getString(NBT_POTION_TYPE)))!!
    }

    fun setPotionStrength(instance: SpellComponentInstance<SpellEffect>, strength: Int) {
        instance.data.putInt(NBT_POTION_STRENGTH, strength)
    }

    /**
     * Gets the potion strength for the given spell instance
     *
     * @param instance The instance of the potion strength
     * @return The potion strength for the instance
     */
    fun getPotionStrength(instance: SpellComponentInstance<SpellEffect>): Int {
        return instance.data.getInt(NBT_POTION_STRENGTH)
    }

    fun setPotionDuration(instance: SpellComponentInstance<SpellEffect>, duration: Int) {
        instance.data.putInt(NBT_POTION_DURATION, duration)
    }

    /**
     * Gets the potion duration for the given spell instance
     *
     * @param instance The instance of the potion duration
     * @return The potion duration for the instance
     */
    fun getPotionDuration(instance: SpellComponentInstance<SpellEffect>): Int {
        return instance.data.getInt(NBT_POTION_DURATION)
    }

    /**
     * Gets the potion radius for the given spell instance
     *
     * @param instance The instance of the potion radius
     * @return The potion radius for the instance
     */
    fun getPotionRadius(instance: SpellComponentInstance<SpellEffect>): Float {
        return instance.data.getFloat(NBT_POTION_RADIUS)
    }

    companion object {
        // NBT constants spell potion stats
        private const val NBT_POTION_TYPE = "potion_type"
        private const val NBT_POTION_STRENGTH = "potion_strength"
        private const val NBT_POTION_DURATION = "potion_duration"
        private const val NBT_POTION_RADIUS = "potion_radius"
    }
}