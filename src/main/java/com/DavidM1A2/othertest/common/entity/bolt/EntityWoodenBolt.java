package com.DavidM1A2.afraidofthedark.common.entity.bolt;

import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Class representing a wooden bolt entity shot by crossbows
 */
public class EntityWoodenBolt extends EntityBolt
{
    /**
     * Creates the entity in the world with a shooter source
     *
     * @param worldIn The world to create the bolt in
     */
    public EntityWoodenBolt(World worldIn)
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
    public EntityWoodenBolt(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    /**
     * Creates the entity in the world with a shooter source
     *
     * @param worldIn   The world to create the bolt in
     * @param throwerIn The shooter of the bolt
     */
    public EntityWoodenBolt(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    /**
     * Called to setup the various properties of the bolt such as its damage
     */
    @Override
    void setupProperties()
    {
        this.setDamage(4);
        this.setDrop(ModItems.WOODEN_BOLT);
        this.setChanceToDropHitEntity(0.4);
        this.setChanceToDropHitGround(0.8);
        this.setDamageSourceProducer(DamageSource::causePlayerDamage);
    }
}
