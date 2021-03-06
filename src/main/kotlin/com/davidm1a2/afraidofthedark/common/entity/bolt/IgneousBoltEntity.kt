package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.world.World

/**
 * The igneous bolt entity that can be fired from a crossbow
 *
 * @property damageSourceProducer Player silver damage producer
 * @property drop The item that is dropped by the bolt
 * @property damage The damage that this bolt will do
 * @property chanceToDropHitEntity The chance that the bolt will drop its item after hitting an entity
 * @property chanceToDropHitGround The chance that the bolt will drop its item after hitting the ground
 */
class IgneousBoltEntity : BoltEntity {
    override val damageSourceProducer: (PlayerEntity) -> DamageSource = { ModDamageSources.getSilverDamage(it) }
    override val drop = ModItems.IGNEOUS_BOLT
    override val damage = 22
    override val chanceToDropHitEntity = 0.4
    override val chanceToDropHitGround = 0.8

    /**
     * Creates the entity in the world with a shooter source
     *
     * @param world The world to create the bolt in
     */
    constructor(entityType: EntityType<out IgneousBoltEntity>, world: World) : super(entityType, world)

    /**
     * Creates the entity in the world without a source at a position
     *
     * @param world The world to create the bolt in
     * @param x       The x position of the bolt
     * @param y       The y position of the bolt
     * @param z       The z position of the bolt
     */
    constructor(entityType: EntityType<out IgneousBoltEntity>, x: Double, y: Double, z: Double, world: World) : super(entityType, x, y, z, world)

    /**
     * Creates the entity in the world with a shooter source
     *
     * @param world   The world to create the bolt in
     * @param thrower The shooter of the bolt
     */
    constructor(entityType: EntityType<out IgneousBoltEntity>, thrower: LivingEntity, world: World) : super(entityType, thrower, world)

    /**
     * Called when the bolt hits something
     *
     * @param result The object containing hit information
     */
    override fun onEntityHit(result: EntityRayTraceResult) {
        // On top of doing damage this bolt lights the entity hit on fire
        result.entity.setFire(10)
    }
}