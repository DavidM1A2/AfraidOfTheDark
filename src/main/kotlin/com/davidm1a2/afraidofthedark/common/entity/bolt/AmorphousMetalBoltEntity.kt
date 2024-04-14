package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.utility.damagesource.AOTDDamageType
import com.davidm1a2.afraidofthedark.common.utility.damagesource.IDoesAOTDDamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

class AmorphousMetalBoltEntity(entityType: EntityType<out AmorphousMetalBoltEntity>, world: Level) : BoltEntity(entityType, world), IDoesAOTDDamageType {
    override val type: AOTDDamageType = AOTDDamageType.SILVER
    override val drop = ModItems.AMORPHOUS_METAL_BOLT
    override val damage = 21
    override val chanceToDropHitEntity = 0.4
    override val chanceToDropHitGround = 0.8

    constructor(world: Level) : this(ModEntities.AMORPHOUS_METAL_BOLT, world)
}
