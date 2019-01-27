package com.DavidM1A2.afraidofthedark.client.keybindings;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * Class containing a list of keybindings that the player can modify
 */
public class ModKeybindings
{
	// AOTD will have its own keybinding category
	private static final String CATEGORY = "keys.afraidofthedark.category";
	// One keybinding is to roll with the cloak and the other is to fire the wrist crossbow
	private static final String ROLL_CLOAK_OF_AGILITY_NAME = "keys.afraidofthedark.roll_with_cloak";
	private static final String FIRE_WRIST_CROSSBOW_NAME = "keys.afraidofthedark.fire_wrist_crossbow";

	// Public keybinding variables used to access the key states
	public static final KeyBinding ROLL_WITH_CLOAK_OF_AGILITY = new KeyBinding(ROLL_CLOAK_OF_AGILITY_NAME, Keyboard.KEY_G, CATEGORY);
	public static final KeyBinding FIRE_WRIST_CROSSBOW = new KeyBinding(FIRE_WRIST_CROSSBOW_NAME, Keyboard.KEY_F, CATEGORY);

	// A list of mod keybindings
	public static final KeyBinding[] KEY_BINDING_LIST = new KeyBinding[]
	{
		ROLL_WITH_CLOAK_OF_AGILITY,
		FIRE_WRIST_CROSSBOW
	};
}
