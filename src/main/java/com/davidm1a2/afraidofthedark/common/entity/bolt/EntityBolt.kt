package com.davidm1a2.afraidofthedark.common.entity.bolt

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.Item
import net.minecraft.util.DamageSource
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World

/**
 * Class representing a bolt entity shot by crossbows
 *
 * @property damageSourceProducer The factory used to compute what type of damage to inflict
 * @property drop The item that is dropped by the bolt
 * @property damage The damage that this bolt will do
 * @property chanceToDropHitEntity The chance that the bolt will drop its item after hitting an entity
 * @property chanceToDropHitGround The chance that the bolt will drop its item after hitting the ground
 */
abstract class EntityBolt : EntityThrowable {
    abstract val damageSourceProducer: (EntityPlayer) -> DamageSource
    abstract val drop: Item
    open val damage: Int = 6
    open val chanceToDropHitEntity: Double = 0.4
    open val chanceToDropHitGround: Double = 0.8

    /**
     * Creates the entity in the world with a shooter source
     *
     * @param world The world to create the bolt in
     */
    constructor(world: World) : super(world)

    /**
     * Creates the entity in the world without a source at a position
     *
     * @param world The world to create the bolt in
     * @param x       The x position of the bolt
     * @param y       The y position of the bolt
     * @param z       The z position of the bolt
     */
    constructor(world: World, x: Double, y: Double, z: Double) : super(world, x, y, z)

    /**
     * Creates the entity in the world with a shooter source
     *
     * @param world   The world to create the bolt in
     * @param thrower The shooter of the bolt
     */
    constructor(world: World, thrower: EntityLivingBase) : super(world, thrower)

    /**
     * Called when the bolt hits something
     *
     * @param result The object containing hit information
     */
    override fun onImpact(result: RayTraceResult) {
        // Server side processing only
        if (!world.isRemote) {
            val entityHit = result.entityHit
            // Test if we hit an entity or the ground
            if (entityHit != null) {
                // Test if the shooter of the bolt is a player
                if (thrower is EntityPlayer) {
                    entityHit.attackEntityFrom(damageSourceProducer(thrower as EntityPlayer), damage.toFloat())
                }

                // If the random chance succeeds, drop the bolt item
                if (Math.random() < chanceToDropHitEntity) {
                    entityHit.dropItem(drop, 1)
                }
            } else {
                // If the random chance succeeds, drop the bolt item
                if (Math.random() < chanceToDropHitGround) {
                    dropItem(drop, 1)
                }
            }
        }

        // Kill the bolt on server and client side after impact
        setDead()
    }

}