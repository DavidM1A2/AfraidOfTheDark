package com.DavidM1A2.afraidofthedark.client.keybindings;

import org.lwjgl.input.Keyboard;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Utility class for determining what combinations of keys are down
 */
public class KeybindingUtils
{
    // A set of keys that are unbindable alone and require additional keys down
    private static final Set<Integer> UNBINDABLE_KEYS = new LinkedHashSet<Integer>()
    {{
        add(Keyboard.KEY_RMENU);
        add(Keyboard.KEY_LMENU);
        add(Keyboard.KEY_RCONTROL);
        add(Keyboard.KEY_LCONTROL);
        add(Keyboard.KEY_RSHIFT);
        add(Keyboard.KEY_LSHIFT);
    }};

    /**
     * @return True if a keybindable key is down, false otherwise
     */
    public static boolean keybindableKeyDown()
    {
        Integer keyCode = Keyboard.getEventKey();
        return !UNBINDABLE_KEYS.contains(keyCode);
    }

    /**
     * @return Gets the current keybinding that is being held
     */
    public static String getCurrentlyHeldKeybind()
    {
        // The string that is being bound
        StringBuilder keybindString = new StringBuilder();
        // Go over all unbindable key codes and test if they're down
        for (int unbindableKeyCode : UNBINDABLE_KEYS)
        {
            // If they are down then append the key to the string
            if (Keyboard.isKeyDown(unbindableKeyCode))
            {
                // Append the key and a + symbol
                keybindString.append(Keyboard.getKeyName(unbindableKeyCode).toUpperCase()).append("+");
            }
        }
        // Finally append the key pressed
        String keyName = Keyboard.getKeyName(Keyboard.getEventKey()).toUpperCase();
        keybindString.append(keyName);
        // Return the keybinding string
        return keybindString.toString();
    }
}
