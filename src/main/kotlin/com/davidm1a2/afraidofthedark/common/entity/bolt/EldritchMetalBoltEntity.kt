package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class EldritchMetalBoltEntity(entityType: EntityType<out EldritchMetalBoltEntity>, world: Level) : BoltEntity(entityType, world) {
    override val damageSourceProducer: (Player) -> DamageSource = { ModDamageSources.getSilverDamage(it) }
    override val drop = ModItems.ELDRITCH_METAL_BOLT
    override val damage = 18
    override val chanceToDropHitEntity = 0.2
    override val chanceToDropHitGround = 0.4

    constructor(world: Level) : this(ModEntities.ELDRITCH_METAL_BOLT, world)
}