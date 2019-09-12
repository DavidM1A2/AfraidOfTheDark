package com.DavidM1A2.afraidofthedark.common.spell.component.property;

/**
 * Builder for spell component property
 */
public class SpellComponentPropertyFactory
{
    /**
     * @return A new integer spell component property builder
     */
    public static IntSpellComponentPropertyBuilder intProperty()
    {
        return new IntSpellComponentPropertyBuilder();
    }

    /**
     * @return A new long spell component property builder
     */
    public static LongSpellComponentPropertyBuilder longProperty()
    {
        return new LongSpellComponentPropertyBuilder();
    }

    /**
     * @return A new double spell component property builder
     */
    public static DoubleSpellComponentPropertyBuilder doubleProperty()
    {
        return new DoubleSpellComponentPropertyBuilder();
    }

    /**
     * @return A new float spell component property builder
     */
    public static FloatSpellComponentPropertyBuilder floatProperty()
    {
        return new FloatSpellComponentPropertyBuilder();
    }

    /**
     * @return A new boolean spell component property builder
     */
    public static BooleanSpellComponentPropertyBuilder booleanProperty()
    {
        return new BooleanSpellComponentPropertyBuilder();
    }
}
