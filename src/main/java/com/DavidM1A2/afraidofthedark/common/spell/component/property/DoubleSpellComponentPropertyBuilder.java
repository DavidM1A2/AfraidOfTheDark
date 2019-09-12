package com.DavidM1A2.afraidofthedark.common.spell.component.property;

import org.apache.commons.lang3.Validate;

/**
 * Builder for double spell component property
 */
public class DoubleSpellComponentPropertyBuilder extends BoundedSpellComponentPropertyBuilder<Double, DoubleSpellComponentPropertyBuilder>
{
    /**
     * Builds the spell component property
     *
     * @return The built spell component property
     */
    public SpellComponentProperty build()
    {
        Validate.notNull(name);
        Validate.notNull(description);
        Validate.notNull(setter);
        Validate.notNull(getter);
        Validate.notNull(defaultValue);

        return new DoubleSpellComponentProperty(name, description, setter, getter, defaultValue, minValue, maxValue);
    }
}
