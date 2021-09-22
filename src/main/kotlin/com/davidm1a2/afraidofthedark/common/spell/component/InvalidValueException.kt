package com.davidm1a2.afraidofthedark.common.spell.component

import net.minecraft.util.text.ITextComponent

/**
 * An exception that gets thrown when an editable spell component property gets an invalid value
 *
 * @constructor that takes in an error message to be displayed to the user
 * @param reason the detail message
 */
class InvalidValueException(val reason: ITextComponent) : RuntimeException()
