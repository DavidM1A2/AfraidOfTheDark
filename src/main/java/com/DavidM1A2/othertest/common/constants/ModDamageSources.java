package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone;
import com.DavidM1A2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDroneProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

/**
 * Class containing definitions for AOTD damage sources
 */
public class ModDamageSources
{
    // Constant names for the damage sources
    public static final String SILVER_DAMAGE = "silver_damage";
    public static final String PLASMA_BALL = "plasma_ball";

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

    /**
     * Returns a plasma ball damage source given an entity source and target
     *
     * @param source         The projectile that hit the entity
     * @param indirectSource The drone that fired the shot
     * @return The damage source to apply
     */
    public static DamageSource causePlasmaBallDamage(EntitySplinterDroneProjectile source, EntitySplinterDrone indirectSource)
    {
        return new EntityDamageSourceIndirect(PLASMA_BALL, source, indirectSource).setProjectile();
    }
}
