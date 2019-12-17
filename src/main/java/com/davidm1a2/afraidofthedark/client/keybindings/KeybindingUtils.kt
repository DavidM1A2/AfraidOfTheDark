package com.davidm1a2.afraidofthedark.client.keybindings

import org.lwjgl.input.Keyboard

/**
 * Utility class for determining what combinations of keys are down
 */
object KeybindingUtils
{
    // A set of keys that are unbindable alone and require additional keys down
    private val UNBINDABLE_KEYS = setOf(
        Keyboard.KEY_RMENU,
        Keyboard.KEY_LMENU,
        Keyboard.KEY_RCONTROL,
        Keyboard.KEY_LCONTROL,
        Keyboard.KEY_RSHIFT,
        Keyboard.KEY_LSHIFT
    )

    /**
     * @return True if a keybindable key is down, false otherwise
     */
    fun keybindableKeyDown(): Boolean
    {
        val keyCode = Keyboard.getEventKey()
        return !UNBINDABLE_KEYS.contains(keyCode)
    }

    /**
     * @return Gets the current keybinding that is being held
     */
    fun getCurrentlyHeldKeybind(): String
    {
        // The string that is being bound
        val keybindString = StringBuilder()

        // Go over all unbindable key codes and test if they're down
        for (unbindableKeyCode in UNBINDABLE_KEYS)
        {
            // If they are down then append the key to the string
            if (Keyboard.isKeyDown(unbindableKeyCode))
            {
                // Append the key and a + symbol
                keybindString.append(Keyboard.getKeyName(unbindableKeyCode).toUpperCase()).append("+")
            }
        }

        // Finally append the key pressed
        val keyName = Keyboard.getKeyName(Keyboard.getEventKey()).toUpperCase()
        keybindString.append(keyName)

        // Return the keybinding string
        return keybindString.toString()
    }
}