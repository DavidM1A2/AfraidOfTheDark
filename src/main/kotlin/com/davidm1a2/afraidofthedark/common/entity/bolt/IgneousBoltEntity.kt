package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult

/**
 * The igneous bolt entity that can be fired from a crossbow
 *
 * @property damageSourceProducer Player silver damage producer
 * @property drop The item that is dropped by the bolt
 * @property damage The damage that this bolt will do
 * @property chanceToDropHitEntity The chance that the bolt will drop its item after hitting an entity
 * @property chanceToDropHitGround The chance that the bolt will drop its item after hitting the ground
 */
class IgneousBoltEntity(entityType: EntityType<out IgneousBoltEntity>, world: Level) : BoltEntity(entityType, world) {
    override val damageSourceProducer: (Player) -> DamageSource = { ModDamageSources.getSilverDamage(it) }
    override val drop = ModItems.IGNEOUS_BOLT
    override val damage = 22
    override val chanceToDropHitEntity = 0.4
    override val chanceToDropHitGround = 0.8

    constructor(world: Level) : this(ModEntities.IGNEOUS_BOLT, world)

    /**
     * Called when the bolt hits something
     *
     * @param result The object containing hit information
     */
    override fun onHitEntity(result: EntityHitResult) {
        if (hasResearch) {
            // On top of doing damage this bolt lights the entity hit on fire
            result.entity.setSecondsOnFire(10)
        }
    }
}