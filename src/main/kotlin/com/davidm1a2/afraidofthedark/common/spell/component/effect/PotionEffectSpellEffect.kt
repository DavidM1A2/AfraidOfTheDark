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
import net.minecraft.potion.Effects
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
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
            object : SpellComponentProperty<Effect>(
                getUnlocalizedPropertyBaseName("type"),
                this::setType,
                this::getType,
                Effects.MOVEMENT_SPEED
            ) {
                override fun convertTo(newValue: String): Effect {
                    // Grab the potion associated with the text
                    val type = ForgeRegistries.POTIONS.getValue(ResourceLocation(newValue))
                    // If type is not null it's a valid potion type so we store it, otherwise throw an exception
                    if (type != null) {
                        return type
                    } else {
                        throw InvalidValueException(TranslationTextComponent("property_error.afraidofthedark.potion.type", newValue))
                    }
                }

                override fun convertFrom(value: Effect): String {
                    return value.registryName.toString()
                }
            }
        )
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("strength"))
                .withSetter(this::setStrength)
                .withGetter(this::getStrength)
                .withDefaultValue(1)
                .withMinValue(1)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("duration"))
                .withSetter(this::setDuration)
                .withGetter(this::getDuration)
                .withDefaultValue(20)
                .withMinValue(1)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.floatProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("cloud_radius"))
                .withSetter(this::setRadius)
                .withGetter(this::getRadius)
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
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val exactPosition = state.position
        val potionType = getType(instance)
        val potionStrength = getStrength(instance)
        val potionDuration = getDuration(instance)

        // If we hit an entity just apply the potion effect
        val entityHit = state.getEntity()
        if (entityHit != null) {
            if (entityHit is LivingEntity) {
                createParticlesAt(1, 3, exactPosition, entityHit.level.dimension(), ModParticles.SPELL_HIT)
                entityHit.addEffect(EffectInstance(potionType, potionDuration, potionStrength))
            }
        } else {
            val world: World = state.world
            val aoePotion = AreaEffectCloudEntity(world, exactPosition.x, exactPosition.y, exactPosition.z)
            aoePotion.addEffect(EffectInstance(potionType, potionDuration, potionStrength))
            aoePotion.owner = state.getCasterEntity() as? LivingEntity
            aoePotion.radius = getRadius(instance)
            aoePotion.setRadiusPerTick(0f)
            aoePotion.duration = potionDuration
            world.addFreshEntity(aoePotion)
            createParticlesAt(2, 6, exactPosition, world.dimension(), ModParticles.SPELL_HIT)
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 15.0 + getDuration(instance) * getStrength(instance) * max(sqrt(getRadius(instance)), 1.0f)
    }

    fun setType(instance: SpellComponentInstance<*>, effect: Effect) {
        instance.data.putString(NBT_TYPE, effect.registryName.toString())
    }

    fun getType(instance: SpellComponentInstance<*>): Effect {
        return ForgeRegistries.POTIONS.getValue(ResourceLocation(instance.data.getString(NBT_TYPE)))!!
    }

    fun setStrength(instance: SpellComponentInstance<*>, strength: Int) {
        instance.data.putInt(NBT_STRENGTH, strength)
    }

    fun getStrength(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_STRENGTH)
    }

    fun setDuration(instance: SpellComponentInstance<*>, duration: Int) {
        instance.data.putInt(NBT_DURATION, duration)
    }

    fun getDuration(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_DURATION)
    }

    fun setRadius(instance: SpellComponentInstance<*>, radius: Float) {
        instance.data.putFloat(NBT_RADIUS, radius)
    }

    fun getRadius(instance: SpellComponentInstance<*>): Float {
        return instance.data.getFloat(NBT_RADIUS)
    }

    companion object {
        // NBT constants spell potion stats
        private const val NBT_TYPE = "type"
        private const val NBT_STRENGTH = "strength"
        private const val NBT_DURATION = "duration"
        private const val NBT_RADIUS = "radius"
    }
}