package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.world.World

/**
 * The star metal bolt entity that can be fired from a crossbow
 *
 * @property damageSourceProducer Player silver damage producer
 * @property drop The item that is dropped by the bolt
 * @property damage The damage that this bolt will do
 * @property chanceToDropHitEntity The chance that the bolt will drop its item after hitting an entity
 * @property chanceToDropHitGround The chance that the bolt will drop its item after hitting the ground
 */
class StarMetalBoltEntity(entityType: EntityType<out StarMetalBoltEntity>, world: World) : BoltEntity(entityType, world) {
    override val damageSourceProducer: (PlayerEntity) -> DamageSource = { ModDamageSources.getSilverDamage(it) }
    override val drop = ModItems.STAR_METAL_BOLT
    override val damage = 20
    override val chanceToDropHitEntity = 0.5
    override val chanceToDropHitGround = 0.95
    override val research: Research = ModResearches.STAR_METAL

    constructor(world: World) : this(ModEntities.STAR_METAL_BOLT, world)
}