package com.davidm1a2.afraidofthedark.client.keybindings

import net.minecraft.client.settings.KeyBinding
import org.lwjgl.glfw.GLFW

/**
 * Class containing a list of keybindings that the player can modify
 */
object ModKeybindings {
    // AOTD will have its own keybinding category
    private const val CATEGORY_NAME = "key.categories.afraidofthedark"

    private const val FIRE_WRIST_CROSSBOW_NAME = "afraidofthedark.key.fire_wrist_crossbow"
    private const val ROLL_CLOAK_OF_AGILITY_NAME = "afraidofthedark.key.roll_with_cloak"
    private const val POWER_SOURCE_SELECTOR_NAME = "afraidofthedark.key.power_source_selector"

    // Public keybinding variables used to access the key states
    val ROLL_WITH_CLOAK_OF_AGILITY = KeyBinding(ROLL_CLOAK_OF_AGILITY_NAME, GLFW.GLFW_KEY_G, CATEGORY_NAME)
    val FIRE_WRIST_CROSSBOW = KeyBinding(FIRE_WRIST_CROSSBOW_NAME, GLFW.GLFW_KEY_Z, CATEGORY_NAME)
    val POWER_SOURCE_SELECTOR = KeyBinding(POWER_SOURCE_SELECTOR_NAME, GLFW.GLFW_KEY_R, CATEGORY_NAME)

    // A list of mod keybindings
    val KEY_BINDING_LIST = arrayOf(
        ROLL_WITH_CLOAK_OF_AGILITY,
        FIRE_WRIST_CROSSBOW,
        POWER_SOURCE_SELECTOR
    )
}