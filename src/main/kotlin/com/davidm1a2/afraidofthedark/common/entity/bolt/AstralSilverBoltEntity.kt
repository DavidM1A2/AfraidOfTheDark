package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.utility.damagesource.AOTDDamageType
import com.davidm1a2.afraidofthedark.common.utility.damagesource.IDoesAOTDDamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

/**
 * Class representing a silver bolt entity shot by crossbows
 *
 * @property drop The item that is dropped by the bolt
 * @property damage The damage that this bolt will do
 * @property chanceToDropHitEntity The chance that the bolt will drop its item after hitting an entity
 * @property chanceToDropHitGround The chance that the bolt will drop its item after hitting the ground
 */
class AstralSilverBoltEntity(entityType: EntityType<out AstralSilverBoltEntity>, world: Level) : BoltEntity(entityType, world), IDoesAOTDDamageType {
    override val type: AOTDDamageType = AOTDDamageType.SILVER
    override val drop = ModItems.ASTRAL_SILVER_BOLT
    override val damage = 16
    override val chanceToDropHitEntity = 0.4
    override val chanceToDropHitGround = 0.8

    constructor(world: Level) : this(ModEntities.ASTRAL_SILVER_BOLT, world)
}