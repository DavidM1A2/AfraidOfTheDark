package com.DavidM1A2.afraidofthedark.common.spell.component.property;

import com.DavidM1A2.afraidofthedark.common.spell.component.InvalidValueException;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Special spell component property that encapsulates long parsing
 */
class LongSpellComponentProperty extends SpellComponentProperty
{
    /**
     * Constructor just sets all the fields
     *
     * @param name         The name of the property being edited
     * @param description  The description of the property being edited
     * @param getter       The getter to get this property's current value
     * @param setter       The setter to be used when setting this property's value
     * @param defaultValue The default value of the property
     * @param minValue     The minimum value of the property
     * @param maxValue     The maximum value of the property
     */
    LongSpellComponentProperty(String name, String description, Consumer<Long> setter, Supplier<Long> getter, Long defaultValue, Long minValue, Long maxValue)
    {
        super(name,
                description,
                newValue ->
                {
                    // Ensure the number is parsable
                    try
                    {
                        // Parse the long
                        Long longValue = Long.parseLong(newValue);
                        // Ensure the long is valid
                        if (minValue != null && longValue < minValue)
                        {
                            setter.accept(defaultValue);
                            throw new InvalidValueException(name + " must be larger than or equal to " + minValue);
                        }
                        if (maxValue != null && longValue > maxValue)
                        {
                            setter.accept(defaultValue);
                            throw new InvalidValueException(name + " must be smaller than than or equal to " + minValue);
                        }
                        setter.accept(longValue);
                    }
                    // If it's not valid return an error
                    catch (NumberFormatException e)
                    {
                        throw new InvalidValueException(newValue + " is not a valid integer!");
                    }
                },
                () -> getter.get().toString());
    }
}
