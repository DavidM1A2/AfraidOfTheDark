package com.davidm1a2.afraidofthedark.common.spell.component.effect.base

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation

class SpellEffectInstance(component: SpellEffect) : SpellComponentInstance<SpellEffect>(component) {
    companion object {
        /**
         * Utility function to create a spell effect from NBT
         *
         * @param nbt The NBT to get the effect information from
         * @return The spell effect instance from NBT
         */
        fun createFromNBT(nbt: CompoundTag): SpellComponentInstance<SpellEffect> {
            // Figure out the type of delivery method that this NBT represents
            val effectTypeId = nbt.getString(NBT_TYPE_ID)
            // Use our registry to create a new instance of this type
            val instance = SpellEffectInstance(
                ModRegistries.SPELL_EFFECTS.getValue(ResourceLocation(effectTypeId))
                    ?: throw IllegalArgumentException("$effectTypeId doesn't exist!")
            )
            // Deserialize the instance and return it
            instance.deserializeNBT(nbt)
            return instance
        }
    }
}