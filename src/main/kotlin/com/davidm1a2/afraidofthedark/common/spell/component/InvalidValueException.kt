package com.davidm1a2.afraidofthedark.common.spell.component

/**
 * An exception that gets thrown when an editable spell component property gets an invalid value
 *
 * @constructor that takes in an error message to be displayed to the user
 * @param message the detail message
 */
class InvalidValueException(message: String) : RuntimeException(message)
