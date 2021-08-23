package com.davidm1a2.afraidofthedark.common.entity.bolt

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.AbstractArrowEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
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
abstract class BoltEntity : AbstractArrowEntity {
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

    fun setShotFrom(entity: LivingEntity) {
        owner = entity
        pickup = if (entity is PlayerEntity && entity.isCreative) {
            PickupStatus.DISALLOWED
        } else {
            PickupStatus.ALLOWED
        }
        setPos(entity.x, entity.eyeY - 0.1, entity.z)
        shootFromRotation(entity, entity.xRot, entity.yRot, 0f, 5f, 0f)
    }

    /**
     * Called when the bolt hits something
     *
     * @param result The object containing hit information
     */
    override fun onHit(result: RayTraceResult) {
        super.onHit(result)

        // Server side processing only
        if (!level.isClientSide) {
            // Test if we hit an entity or the ground
            if (result.type == RayTraceResult.Type.ENTITY) {
                val entityHit = (result as EntityRayTraceResult).entity
                // Test if the shooter of the bolt is a player
                if (owner is PlayerEntity) {
                    entityHit.hurt(damageSourceProducer(owner as PlayerEntity), damage.toFloat())
                }

                if (Math.random() >= chanceToDropHitEntity) {
                    remove()
                }
            } else {
                if (Math.random() >= chanceToDropHitGround) {
                    remove()
                }
            }
        }
    }

    override fun onHitEntity(result: EntityRayTraceResult) {
        // Called when an entity is hit, no op since we do that calculation in onHit()
    }

    override fun getPickupItem(): ItemStack {
        return ItemStack(drop)
    }

    override fun getAddEntityPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }
}