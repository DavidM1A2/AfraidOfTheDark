package com.davidm1a2.afraidofthedark.common.entity.bolt

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ThrowableEntity
import net.minecraft.item.Item
import net.minecraft.network.IPacket
import net.minecraft.util.DamageSource
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks

/**
 * Class representing a bolt entity shot by crossbows
 *
 * @property damageSourceProducer The factory used to compute what type of damage to inflict
 * @property drop The item that is dropped by the bolt
 * @property damage The damage that this bolt will do
 * @property chanceToDropHitEntity The chance that the bolt will drop its item after hitting an entity
 * @property chanceToDropHitGround The chance that the bolt will drop its item after hitting the ground
 */
abstract class BoltEntity : ThrowableEntity {
    abstract val damageSourceProducer: (PlayerEntity) -> DamageSource
    abstract val drop: Item
    open val damage: Int = 6
    open val chanceToDropHitEntity: Double = 0.4
    open val chanceToDropHitGround: Double = 0.8

    /**
     * Creates the entity in the world with a shooter source
     *
     * @param entityType The entity type of the bolt
     * @param world The world to create the bolt in
     */
    constructor(entityType: EntityType<out BoltEntity>, world: World) : super(entityType, world)

    /**
     * Creates the entity in the world without a source at a position
     *
     * @param entityType The entity type of the bolt
     * @param world The world to create the bolt in
     * @param x       The x position of the bolt
     * @param y       The y position of the bolt
     * @param z       The z position of the bolt
     */
    constructor(entityType: EntityType<out BoltEntity>, x: Double, y: Double, z: Double, world: World) : super(entityType, x, y, z, world)

    /**
     * Creates the entity in the world with a shooter source
     *
     * @param entityType The entity type of the bolt
     * @param world   The world to create the bolt in
     * @param thrower The shooter of the bolt
     */
    constructor(entityType: EntityType<out BoltEntity>, thrower: LivingEntity, world: World) : super(entityType, thrower, world)

    /**
     * Register any entity data. Anything registered here is automatically synced from Server -> Client
     */
    override fun registerData() {
    }

    /**
     * Called when the bolt hits something
     *
     * @param result The object containing hit information
     */
    override fun onImpact(result: RayTraceResult) {
        // Server side processing only
        if (!world.isRemote) {
            // Test if we hit an entity or the ground
            if (result.type == RayTraceResult.Type.ENTITY) {
                val entityHit = (result as EntityRayTraceResult).entity
                // Test if the shooter of the bolt is a player
                if (thrower is PlayerEntity) {
                    entityHit.attackEntityFrom(damageSourceProducer(thrower as PlayerEntity), damage.toFloat())
                }

                // If the random chance succeeds, drop the bolt item
                if (Math.random() < chanceToDropHitEntity) {
                    entityHit.entityDropItem(drop, 1)
                }
            } else {
                // If the random chance succeeds, drop the bolt item
                if (Math.random() < chanceToDropHitGround) {
                    entityDropItem(drop, 1)
                }
            }
        }

        // Kill the bolt on server and client side after impact
        remove()
    }

    override fun createSpawnPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }
}