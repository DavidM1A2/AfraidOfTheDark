package com.davidm1a2.afraidofthedark.common.spell.component

import net.minecraft.nbt.CompoundTag
import net.minecraftforge.common.util.INBTSerializable

abstract class SpellComponentInstance<T : SpellComponent<T>>(val component: T) : INBTSerializable<CompoundTag> {
    var data: CompoundTag = CompoundTag()
        private set

    fun setDefaults() {
        component.getEditableProperties().forEach { it.setDefaultValue(this) }
    }

    override fun serializeNBT(): CompoundTag {
        val nbt = CompoundTag()

        // We store type purely so we can recover what spell component type this was later when deserializing (which happens in subclasses)
        nbt.putString(NBT_TYPE_ID, component.registryName.toString())
        nbt.put(NBT_EXTRA_DATA, data)

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        data = nbt.getCompound(NBT_EXTRA_DATA)
    }

    companion object {
        const val NBT_TYPE_ID = "id"
        const val NBT_EXTRA_DATA = "extra_data"
    }
}