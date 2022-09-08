package com.davidm1a2.afraidofthedark.common.spell.component.effect.base

import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory

abstract class AOTDDurationSpellEffect(
    name: String,
    prerequisiteResearch: Research? = null,
    minDuration: Double? = null,
    defaultDuration: Double? = null,
    maxDuration: Double? = null
) : AOTDSpellEffect(name, prerequisiteResearch) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
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

    fun setDuration(instance: SpellComponentInstance<*>, duration: Double) {
        instance.data.putDouble(NBT_DURATION, duration)
    }

    fun getDuration(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_DURATION)
    }

    companion object {
        private const val NBT_DURATION = "duration"
    }
}