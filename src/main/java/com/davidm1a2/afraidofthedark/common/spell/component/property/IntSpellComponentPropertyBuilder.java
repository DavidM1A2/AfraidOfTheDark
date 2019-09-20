package com.davidm1a2.afraidofthedark.common.spell.component.property;

import org.apache.commons.lang3.Validate;

/**
 * Builder for int spell component property
 */
public class IntSpellComponentPropertyBuilder extends BoundedSpellComponentPropertyBuilder<Integer, IntSpellComponentPropertyBuilder>
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

        return new IntSpellComponentProperty(name, description, setter, getter, defaultValue, minValue, maxValue);
    }
}
