
package com.DavidM1A2.AfraidOfTheDark.common.reference;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class AOTDDamageSources
{
	public static DamageSource causeSilverDamage(Entity entity)
	{
		return new EntityDamageSource("silverDamage", entity);
	}

	public static DamageSource causePlasmaBallDamage(Entity source, Entity target)
	{
		return new EntityDamageSourceIndirect("plasmaBall", source, target).setProjectile();
	}
}
