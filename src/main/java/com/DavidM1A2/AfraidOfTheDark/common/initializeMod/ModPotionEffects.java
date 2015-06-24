/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

import com.DavidM1A2.AfraidOfTheDark.common.item.potion.SleepingPotion;

public class ModPotionEffects
{
	public static SleepingPotion sleepingPotion = new SleepingPotion(30, new ResourceLocation("afraidofthedark:textures/potionEffect/sleeping.png"), false, 0x000000);

	// Add recipes
	public static void initialize()
	{
		Potion[] potionTypes = null;

		for (Field field : Potion.class.getDeclaredFields())
		{
			field.setAccessible(true);
			try
			{
				if (field.getName().equals("potionTypes") || field.getName().equals("field_76425_a"))
				{
					Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(field, field.getModifiers() & ~Modifier.FINAL);

					potionTypes = (Potion[]) field.get(null);
					final Potion[] newPotionTypes = new Potion[256];
					System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);

					for (int i = 0; i < newPotionTypes.length; i++)
					{
						if (newPotionTypes[i] == null)
						{
							newPotionTypes[i] = sleepingPotion;
							break;
						}
					}

					field.set(null, newPotionTypes);
				}
			}
			catch (Exception e)
			{
				System.err.println("Severe error, please report this to the mod author:");
				System.err.println(e);
			}
		}
	}
}
