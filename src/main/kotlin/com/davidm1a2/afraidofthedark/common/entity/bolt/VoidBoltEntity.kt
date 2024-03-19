package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class VoidBoltEntity(entityType: EntityType<out VoidBoltEntity>, world: Level) : BoltEntity(entityType, world) {
    override val damageSourceProducer: (Player) -> DamageSource = { ModDamageSources.getSilverDamage(it) }
    override val drop = ModItems.VOID_BOLT
    override val damage = 100
    override val chanceToDropHitEntity = 0.1
    override val chanceToDropHitGround = 0.2

    constructor(world: Level) : this(ModEntities.VOID_BOLT, world)
}