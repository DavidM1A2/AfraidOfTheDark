package com.davidm1a2.afraidofthedark.client.keybindings

import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW

/**
 * Utility class for determining what combinations of keys are down
 */
object KeybindingUtils {
    // A set of keys that are unbindable alone and require additional keys down
    private val UNBINDABLE_KEYS = setOf(
        GLFW.GLFW_KEY_LEFT_SUPER,
        GLFW.GLFW_KEY_RIGHT_SUPER,
        GLFW.GLFW_KEY_LEFT_CONTROL,
        GLFW.GLFW_KEY_RIGHT_CONTROL,
        GLFW.GLFW_KEY_LEFT_SHIFT,
        GLFW.GLFW_KEY_RIGHT_SHIFT
    )
    private val UNBINDABLE_KEY_NAMES = mapOf(
        GLFW.GLFW_KEY_LEFT_SUPER to "Left Super",
        GLFW.GLFW_KEY_RIGHT_SUPER to "Right Super",
        GLFW.GLFW_KEY_LEFT_CONTROL to "Left Control",
        GLFW.GLFW_KEY_RIGHT_CONTROL to "Right Control",
        GLFW.GLFW_KEY_LEFT_SHIFT to "Left Shift",
        GLFW.GLFW_KEY_RIGHT_SHIFT to "Right Shift"
    )

    /**
     * @param keyCode The code of the key to test
     * @return True if a keybindable key is down, false otherwise
     */
    fun isKeyBindable(keyCode: Int): Boolean {
        return !UNBINDABLE_KEYS.contains(keyCode)
    }

    /**
     * @param keyCode The last key that was hit
     * @param scanCode The last key's scan code
     * @return Gets the current keybinding that is being held
     */
    fun getCurrentlyHeldKeybind(keyCode: Int, scanCode: Int): String {
        // The string that is being bound
        val keybindString = StringBuilder()

        // Go over all unbindable key codes and test if they're down
        for (unbindableKeyCode in UNBINDABLE_KEYS) {
            // If they are down then append the key to the string
            if (GLFW.glfwGetKey(Minecraft.getInstance().window.window, unbindableKeyCode) == GLFW.GLFW_PRESS) {
                // Append the key and a + symbol
                keybindString.append(UNBINDABLE_KEY_NAMES[unbindableKeyCode]!!.uppercase()).append("+")
            }
        }

        // Finally append the key pressed
        val keyName = GLFW.glfwGetKeyName(keyCode, scanCode)
        if (keyName != null) {
            keybindString.append(keyName.uppercase())
        }

        // Return the keybinding string
        return keybindString.toString()
    }
}
