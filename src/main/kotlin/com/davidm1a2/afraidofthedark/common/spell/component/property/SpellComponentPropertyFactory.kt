package com.davidm1a2.afraidofthedark.common.spell.component.property

/**
 * Builder for spell component property
 */
object SpellComponentPropertyFactory {
    /**
     * @return A new integer spell component property builder
     */
    fun intProperty(): IntSpellComponentPropertyBuilder {
        return IntSpellComponentPropertyBuilder()
    }

    /**
     * @return A new long spell component property builder
     */
    fun longProperty(): LongSpellComponentPropertyBuilder {
        return LongSpellComponentPropertyBuilder()
    }

    /**
     * @return A new double spell component property builder
     */
    fun doubleProperty(): DoubleSpellComponentPropertyBuilder {
        return DoubleSpellComponentPropertyBuilder()
    }

    /**
     * @return A new float spell component property builder
     */
    fun floatProperty(): FloatSpellComponentPropertyBuilder {
        return FloatSpellComponentPropertyBuilder()
    }

    /**
     * @return A new boolean spell component property builder
     */
    fun booleanProperty(): BooleanSpellComponentPropertyBuilder {
        return BooleanSpellComponentPropertyBuilder()
    }

    /**
     * @return A new enum spell component property builder
     */
    inline fun <reified T : Enum<T>> enumProperty(): EnumSpellComponentPropertyBuilder<T> {
        return EnumSpellComponentPropertyBuilder(T::class.java.enumConstants)
    }

    fun colorProperty(): ColorSpellComponentPropertyBuilder {
        return ColorSpellComponentPropertyBuilder()
    }
}