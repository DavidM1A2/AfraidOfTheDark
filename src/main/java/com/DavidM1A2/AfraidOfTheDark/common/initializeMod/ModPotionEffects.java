/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.DavidM1A2.AfraidOfTheDark.common.item.potion.SleepingPotion;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModPotionEffects
{
	public static Potion sleepingPotion = new SleepingPotion(new ResourceLocation("afraidofthedark:textures/potion_effect/sleeping.png"), false, 0x000000).setRegistryName("sleeping_potion");

	// Add recipes
	public static void initialize()
	{
		GameRegistry.register(sleepingPotion);
	}
}
