package com.DavidM1A2.afraidofthedark.common.entity.bolt;

import com.DavidM1A2.afraidofthedark.common.constants.ModDamageSources;
import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * The igneous bolt entity that can be fired from a crossbow
 */
public class EntityIgneousBolt extends EntityBolt
{
    /**
     * Creates the entity in the world with a shooter source
     *
     * @param worldIn The world to create the bolt in
     */
    public EntityIgneousBolt(World worldIn)
    {
        super(worldIn);
    }

    /**
     * Creates the entity in the world without a source at a position
     *
     * @param worldIn The world to create the bolt in
     * @param x       The x position of the bolt
     * @param y       The y position of the bolt
     * @param z       The z position of the bolt
     */
    public EntityIgneousBolt(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    /**
     * Creates the entity in the world with a shooter source
     *
     * @param worldIn   The world to create the bolt in
     * @param throwerIn The shooter of the bolt
     */
    public EntityIgneousBolt(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    /**
     * Called to setup the various properties of the bolt such as its damage
     */
    @Override
    void setupProperties()
    {
        this.setDamage(18);
        this.setDrop(ModItems.IGNEOUS_BOLT);
        this.setChanceToDropHitEntity(.4);
        this.setChanceToDropHitGround(.8);
        this.setDamageSourceProducer(ModDamageSources::getSilverDamage);
    }

    /**
     * Called when the bolt hits something
     *
     * @param result The object containing hit information
     */
    @Override
    protected void onImpact(RayTraceResult result)
    {
        super.onImpact(result);
        // On top of doing damage this bolt lights the entity hit on fire
        if (result.entityHit != null)
        {
            result.entityHit.setFire(10);
        }
    }
}
