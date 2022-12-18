package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.world.World

class AmorphousMetalBoltEntity(entityType: EntityType<out AmorphousMetalBoltEntity>, world: World) : BoltEntity(entityType, world) {
    override val damageSourceProducer: (PlayerEntity) -> DamageSource = { ModDamageSources.getSilverDamage(it) }
    override val drop = ModItems.AMORPHOUS_METAL_BOLT
    override val damage = 21
    override val chanceToDropHitEntity = 0.4
    override val chanceToDropHitGround = 0.8

    constructor(world: World) : this(ModEntities.AMORPHOUS_METAL_BOLT, world)
}