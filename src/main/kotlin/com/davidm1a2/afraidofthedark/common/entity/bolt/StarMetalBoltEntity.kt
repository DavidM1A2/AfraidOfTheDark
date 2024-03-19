package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

/**
 * The star metal bolt entity that can be fired from a crossbow
 *
 * @property damageSourceProducer Player silver damage producer
 * @property drop The item that is dropped by the bolt
 * @property damage The damage that this bolt will do
 * @property chanceToDropHitEntity The chance that the bolt will drop its item after hitting an entity
 * @property chanceToDropHitGround The chance that the bolt will drop its item after hitting the ground
 */
class StarMetalBoltEntity(entityType: EntityType<out StarMetalBoltEntity>, world: Level) : BoltEntity(entityType, world) {
    override val damageSourceProducer: (Player) -> DamageSource = { ModDamageSources.getSilverDamage(it) }
    override val drop = ModItems.STAR_METAL_BOLT
    override val damage = 20
    override val chanceToDropHitEntity = 0.5
    override val chanceToDropHitGround = 0.95

    constructor(world: Level) : this(ModEntities.STAR_METAL_BOLT, world)
}