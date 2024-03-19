package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

/**
 * Class representing a wooden bolt entity shot by crossbows
 *
 * @property damageSourceProducer Player damage producer
 * @property drop The item that is dropped by the bolt
 * @property damage The damage that this bolt will do
 * @property chanceToDropHitEntity The chance that the bolt will drop its item after hitting an entity
 * @property chanceToDropHitGround The chance that the bolt will drop its item after hitting the ground
 */
class WoodenBoltEntity(entityType: EntityType<out WoodenBoltEntity>, world: Level) : BoltEntity(entityType, world) {
    override val damageSourceProducer: (Player) -> DamageSource = { DamageSource.playerAttack(it) }
    override val drop = ModItems.WOODEN_BOLT
    override val damage = 4
    override val chanceToDropHitEntity = 0.4
    override val chanceToDropHitGround = 0.8

    constructor(world: Level) : this(ModEntities.WOODEN_BOLT, world)
}