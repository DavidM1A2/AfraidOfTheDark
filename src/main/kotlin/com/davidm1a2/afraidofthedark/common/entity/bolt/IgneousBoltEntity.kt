package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.entity.EntityType
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
class IgneousBoltEntity(entityType: EntityType<out IgneousBoltEntity>, world: World) : BoltEntity(entityType, world) {
    override val damageSourceProducer: (PlayerEntity) -> DamageSource = { ModDamageSources.getSilverDamage(it) }
    override val drop = ModItems.IGNEOUS_BOLT
    override val damage = 22
    override val chanceToDropHitEntity = 0.4
    override val chanceToDropHitGround = 0.8

    constructor(world: World) : this(ModEntities.IGNEOUS_BOLT, world)

    /**
     * Called when the bolt hits something
     *
     * @param result The object containing hit information
     */
    override fun onHitEntity(result: EntityRayTraceResult) {
        if (hasResearch) {
            // On top of doing damage this bolt lights the entity hit on fire
            result.entity.setSecondsOnFire(10)
        }
    }
}