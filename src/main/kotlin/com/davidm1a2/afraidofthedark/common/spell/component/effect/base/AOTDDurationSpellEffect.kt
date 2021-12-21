package com.davidm1a2.afraidofthedark.common.spell.component.effect.base

import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.ResourceLocation

abstract class AOTDDurationSpellEffect(
    id: ResourceLocation,
    prerequisiteResearch: Research? = null,
    minDuration: Int? = null,
    defaultDuration: Int? = null,
    maxDuration: Int? = null
) : AOTDSpellEffect(id, prerequisiteResearch) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("duration"))
                .withSetter(::setDuration)
                .withGetter(::getDuration)
                .apply {
                    defaultDuration?.let { withDefaultValue(it) }
                    minDuration?.let { withMinValue(it) }
                    maxDuration?.let { withMaxValue(it) }
                }
                .build()
        )
    }

    fun setDuration(instance: SpellComponentInstance<*>, duration: Int) {
        instance.data.putInt(NBT_DURATION, duration)
    }

    fun getDuration(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_DURATION)
    }

    companion object {
        // NBT constants for charm duration
        private const val NBT_DURATION = "duration"
    }
}