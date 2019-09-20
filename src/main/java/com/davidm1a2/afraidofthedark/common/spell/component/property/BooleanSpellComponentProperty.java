package com.davidm1a2.afraidofthedark.common.spell.component.property;

import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Special spell component property that encapsulates boolean parsing
 */
class BooleanSpellComponentProperty extends SpellComponentProperty
{
    /**
     * Constructor just sets all the fields
     *
     * @param name         The name of the property being edited
     * @param description  The description of the property being edited
     * @param getter       The getter to get this property's current value
     * @param setter       The setter to be used when setting this property's value
     * @param defaultValue The default value to use if the input is invalid
     */
    BooleanSpellComponentProperty(String name, String description, Consumer<Boolean> setter, Supplier<Boolean> getter, Boolean defaultValue)
    {
        super(name,
                description,
                newValue ->
                {
                    // Ensure the boolean is valid
                    if (newValue.equalsIgnoreCase("false"))
                    {
                        setter.accept(false);
                    }
                    else if (newValue.equalsIgnoreCase("true"))
                    {
                        setter.accept(true);
                    }
                    else
                    {
                        setter.accept(defaultValue);
                        throw new InvalidValueException(newValue + " must be 'true' or 'false'!");
                    }
                },
                () -> getter.get().toString());
    }
}
