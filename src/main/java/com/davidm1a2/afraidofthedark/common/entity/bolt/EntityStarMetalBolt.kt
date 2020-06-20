package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
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
class EntityStarMetalBolt : EntityBolt {
    override val damageSourceProducer: (EntityPlayer) -> DamageSource = { ModDamageSources.getSilverDamage(it) }
    override val drop = ModItems.STAR_METAL_BOLT
    override val damage = 18
    override val chanceToDropHitEntity = 0.5
    override val chanceToDropHitGround = 0.95

    /**
     * Creates the entity in the world with a shooter source
     *
     * @param world The world to create the bolt in
     */
    constructor(world: World) : super(ModEntities.STAR_METAL_BOLT, world)

    /**
     * Creates the entity in the world without a source at a position
     *
     * @param world The world to create the bolt in
     * @param x       The x position of the bolt
     * @param y       The y position of the bolt
     * @param z       The z position of the bolt
     */
    constructor(x: Double, y: Double, z: Double, world: World) : super(ModEntities.STAR_METAL_BOLT, x, y, z, world)

    /**
     * Creates the entity in the world with a shooter source
     *
     * @param world   The world to create the bolt in
     * @param thrower The shooter of the bolt
     */
    constructor(thrower: EntityLivingBase, world: World) : super(ModEntities.STAR_METAL_BOLT, thrower, world)
}