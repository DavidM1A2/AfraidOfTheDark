/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.settings;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Names;

public class Keybindings
{
	public static KeyBinding rollWithCloakOfAgility = new KeyBinding(Names.Keys.ROLL_CLOAK_OF_AGILITY, Keyboard.KEY_G, Names.Keys.CATEGORY);
	public static KeyBinding fireWristCrossbow = new KeyBinding(Names.Keys.FIRE_WRIST_CROSSBOW, Keyboard.KEY_F, Names.Keys.CATEGORY);
}
