package com.davidm1a2.afraidofthedark.common.spell

import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectInstance
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.common.util.INBTSerializable

/**
 * Class representing a stage in a spell
 *
 * @property deliveryInstance The delivery method for this spell stage, can be null
 * @property effects A list of 4 effects for this spell stage, each can be null but the array can't be
 */
class SpellStage : INBTSerializable<CompoundNBT> {
    var deliveryInstance: SpellComponentInstance<SpellDeliveryMethod>? = null
    val effects: Array<SpellComponentInstance<SpellEffect>?> = arrayOfNulls(MAX_EFFECTS_PER_STAGE)

    /**
     * Default constructor just initializes the spell stage's components to their default value
     */
    constructor() {
        // Null spell delivery method is default
        deliveryInstance = null
    }

    /**
     * Constructor that takes in an NBT compound and creates the spell stage from NBT
     *
     * @param spellStageNBT The NBT containing the spell stage's information
     */
    internal constructor(spellStageNBT: CompoundNBT) {
        deserializeNBT(spellStageNBT)
    }

    /**
     * Gets the cost of the spell stage if it were fired
     *
     * @return The cost of this specific spell stage
     */
    fun getCost(): Double {
        // Ensure the stage is valid first, otherwise cost is 0
        if (this.isValid()) {
            val deliveryType = this.deliveryInstance!!.component
            // Grab the cost of the delivery method
            var cost = deliveryType.getDeliveryCost(this.deliveryInstance!!)
            // Go over every effect and add its cost
            for (effect in effects) {
                if (effect != null) {
                    // Multiply each effect's cost by the number of times it will get proc'd
                    cost = cost + deliveryType.getMultiplicity(this.deliveryInstance!!) * effect.component.getCost(effect)
                }
            }
            return cost
        }
        return 0.0
    }

    /**
     * Returns true if this spell stage is ready to fire, false otherwise
     *
     * @return True if the delivery method is non-null
     */
    fun isValid(): Boolean {
        return this.deliveryInstance != null
    }

    /**
     * Writes the contents of the object into a new NBT compound
     *
     * @return An NBT compound with all this spell stage's data
     */
    override fun serializeNBT(): CompoundNBT {
        val nbt = CompoundNBT()

        // The spell stage delivery method can be null, double check that it isn't before writing it and its state
        deliveryInstance?.let { nbt.put(NBT_DELIVERY_METHOD, it.serializeNBT()) }

        // The spell stage effects can be null, so we need to skip null effects
        effects.forEachIndexed { i, effect -> effect?.let { nbt.put(NBT_EFFECT_BASE + i, it.serializeNBT()) } }

        return nbt
    }

    /**
     * Reads in the contents of the NBT into the object
     *
     * @param nbt The NBT compound to read from
     */
    override fun deserializeNBT(nbt: CompoundNBT) {
        // The spell stage delivery method can be null, double check that it exists before reading it and its state
        if (nbt.contains(NBT_DELIVERY_METHOD)) {
            deliveryInstance = SpellDeliveryMethodInstance.createFromNBT(nbt.getCompound(NBT_DELIVERY_METHOD))
        }

        // Go over each spell effect
        for (i in effects.indices) {
            // The spell stage effects can be null, so we need to skip null effects
            if (nbt.contains(NBT_EFFECT_BASE + i)) {
                effects[i] = SpellEffectInstance.createFromNBT(nbt.getCompound(NBT_EFFECT_BASE + i))
            }
        }
    }

    /**
     * @return a pretty printed spell stage
     */
    override fun toString(): String {
        return "${deliveryInstance?.component?.registryName?.path}: ${
            effects.filterNotNull()
                .joinToString(separator = ", ", prefix = "[", postfix = "]") { it.component.registryName?.path!! }
        }"
    }

    companion object {
        // NBT Tag constants
        private const val NBT_DELIVERY_METHOD = "delivery_method"
        private const val NBT_EFFECT_BASE = "effect_"

        // The max number of effects per spell stage
        private const val MAX_EFFECTS_PER_STAGE = 4
    }
}