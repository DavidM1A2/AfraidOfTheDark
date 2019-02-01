package com.DavidM1A2.afraidofthedark.common.constants;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

/**
 * Class containing definitions for AOTD damage sources
 */
public class ModDamageSources
{
	// Constant names for the damage sources
	public static final String SILVER_DAMAGE = "silver_damage";

	/**
	 * Returns a silver damage source given an entity to cause the damage
	 *
	 * @param entity The entity hitting
	 * @return The silver damage source
	 */
	public static DamageSource getSilverDamage(Entity entity)
	{
		return new EntityDamageSource(SILVER_DAMAGE, entity);
	}
}
