package com.davidm1a2.afraidofthedark.common.spell.component.property;

import org.apache.commons.lang3.Validate;

/**
 * Builder for float spell component property
 */
public class FloatSpellComponentPropertyBuilder extends BoundedSpellComponentPropertyBuilder<Float, FloatSpellComponentPropertyBuilder>
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

        return new FloatSpellComponentProperty(name, description, setter, getter, defaultValue, minValue, maxValue);
    }
}
