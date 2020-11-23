package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation

class SpellDeliveryMethodInstance(component: SpellDeliveryMethod) :
    SpellComponentInstance<SpellDeliveryMethod>(component) {
    companion object {
        /**
         * Utility function to create a spell delivery method from NBT
         *
         * @param nbt The NBT to get the delivery method information from
         * @return The spell delivery method instance from NBT
         */
        fun createFromNBT(nbt: CompoundNBT): SpellComponentInstance<SpellDeliveryMethod> {
            // Figure out the type of delivery method that this NBT represents
            val deliveryMethodTypeId = nbt.getString(NBT_TYPE_ID)
            // Use our registry to create a new instance of this type
            val instance = SpellDeliveryMethodInstance(
                ModRegistries.SPELL_DELIVERY_METHODS.getValue(ResourceLocation(deliveryMethodTypeId))
                    ?: throw IllegalArgumentException("$deliveryMethodTypeId doesn't exist!")
            )
            // Deserialize the instance and return it
            instance.deserializeNBT(nbt)
            return instance
        }
    }
}