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

		for (Field f : Potion.class.getDeclaredFields())
		{
			f.setAccessible(true);
			try
			{
				if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a"))
				{
					Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

					potionTypes = (Potion[]) f.get(null);
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

					f.set(null, newPotionTypes);
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