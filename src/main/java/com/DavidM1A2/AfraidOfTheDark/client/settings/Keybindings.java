/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.settings;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;

public class Keybindings
{
	public static final String CATEGORY = "keys.afraidofthedark.category";
	public static final String ROLL_CLOAK_OF_AGILITY = "keys.afraidofthedark.rollWithCloakOfAgility";
	public static final String FIRE_WRIST_CROSSBOW = "keys.afraidofthedark.fireWristCrossbow";

	public static KeyBinding rollWithCloakOfAgility = new KeyBinding(ROLL_CLOAK_OF_AGILITY, Keyboard.KEY_G, CATEGORY);
	public static KeyBinding fireWristCrossbow = new KeyBinding(FIRE_WRIST_CROSSBOW, Keyboard.KEY_F, CATEGORY);
}
