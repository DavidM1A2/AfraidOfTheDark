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
 * Class representing a silver bolt entity shot by crossbows
 *
 * @property damageSourceProducer Player silver damage producer
 * @property drop The item that is dropped by the bolt
 * @property damage The damage that this bolt will do
 * @property chanceToDropHitEntity The chance that the bolt will drop its item after hitting an entity
 * @property chanceToDropHitGround The chance that the bolt will drop its item after hitting the ground
 */
class SilverBoltEntity(entityType: EntityType<out SilverBoltEntity>, world: World) : BoltEntity(entityType, world) {
    override val damageSourceProducer: (PlayerEntity) -> DamageSource = { ModDamageSources.getSilverDamage(it) }
    override val drop = ModItems.SILVER_BOLT
    override val damage = 16
    override val chanceToDropHitEntity = 0.4
    override val chanceToDropHitGround = 0.8
    override val research: Research = ModResearches.ASTRAL_SILVER

    constructor(world: World) : this(ModEntities.SILVER_BOLT, world)
}