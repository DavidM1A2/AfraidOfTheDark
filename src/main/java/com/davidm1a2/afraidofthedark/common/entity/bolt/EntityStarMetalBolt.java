package com.davidm1a2.afraidofthedark.common.entity.bolt;

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources;
import com.davidm1a2.afraidofthedark.common.constants.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * The star metal bolt entity that can be fired from a crossbow
 */
public class EntityStarMetalBolt extends EntityBolt
{
    /**
     * Creates the entity in the world with a shooter source
     *
     * @param worldIn The world to create the bolt in
     */
    public EntityStarMetalBolt(World worldIn)
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
    public EntityStarMetalBolt(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    /**
     * Creates the entity in the world with a shooter source
     *
     * @param worldIn   The world to create the bolt in
     * @param throwerIn The shooter of the bolt
     */
    public EntityStarMetalBolt(World worldIn, EntityLivingBase throwerIn)
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
        this.setDrop(ModItems.STAR_METAL_BOLT);
        this.setChanceToDropHitEntity(.5);
        this.setChanceToDropHitGround(.95);
        this.setDamageSourceProducer(ModDamageSources::getSilverDamage);
    }
}
