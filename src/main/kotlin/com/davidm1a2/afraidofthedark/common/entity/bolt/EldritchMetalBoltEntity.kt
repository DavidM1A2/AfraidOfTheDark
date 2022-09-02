package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.world.World

class EldritchMetalBoltEntity(entityType: EntityType<out EldritchMetalBoltEntity>, world: World) : BoltEntity(entityType, world) {
    override val damageSourceProducer: (PlayerEntity) -> DamageSource = { ModDamageSources.getSilverDamage(it) }
    override val drop = ModItems.ELDRITCH_METAL_BOLT
    override val damage = 18
    override val chanceToDropHitEntity = 0.2
    override val chanceToDropHitGround = 0.4

    constructor(world: World) : this(ModEntities.ELDRITCH_METAL_BOLT, world)
}