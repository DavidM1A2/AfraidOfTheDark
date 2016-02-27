package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellRegistry;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.Projectile;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Explosion;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.Self;

public class ModSpells {
	// Add spells
	public static void initialize()
	{
		SpellRegistry.registerDeliveryMethod("projectile", new Projectile());
		
		SpellRegistry.registerEffect("explosion", new Explosion());
		
		SpellRegistry.registerPowerSource("self", new Self());
	}
}
