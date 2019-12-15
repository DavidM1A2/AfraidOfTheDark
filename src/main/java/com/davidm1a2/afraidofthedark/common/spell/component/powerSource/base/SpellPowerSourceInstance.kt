package com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation

class SpellPowerSourceInstance(component: SpellPowerSource) : SpellComponentInstance<SpellPowerSource>(component)
{
    companion object
    {
        /**
         * Utility function to create a spell power source from NBT
         *
         * @param nbt The NBT to get the power source information from
         * @return The spell power source instance from NBT
         */
        fun createFromNBT(nbt: NBTTagCompound): SpellComponentInstance<SpellPowerSource>
        {
            // Figure out the type of power source that this NBT represents
            val powerSourceTypeId = nbt.getString(NBT_TYPE_ID)
            // Use our registry to create a new instance of this type
            return SpellPowerSourceInstance(
                ModRegistries.SPELL_POWER_SOURCES.getValue(ResourceLocation(powerSourceTypeId))
                    ?: throw IllegalArgumentException("$powerSourceTypeId doesn't exist!")
            )
        }
    }
}