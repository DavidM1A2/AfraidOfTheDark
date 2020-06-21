package com.davidm1a2.afraidofthedark.common.spell.component.property

import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance

/**
 * Class containing information about a property of a spell component (power source, effect, or delivery method)
 * that is used by the UI to edit the value
 *
 * @constructor just sets all the fields
 * @property name        The name of the property being edited
 * @property description The description of the property being edited
 * @property setter      The setter to be used when setting this property's value
 * @property getter      The getter to get this property's current value
 */
open class SpellComponentProperty(
    val name: String,
    val description: String,
    val setter: (SpellComponentInstance<*>, String) -> Unit,
    val getter: (SpellComponentInstance<*>) -> String,
    val defaultSetter: (SpellComponentInstance<*>) -> Unit
)
