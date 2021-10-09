package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.AbstractArrowEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.IPacket
import net.minecraft.util.DamageSource
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks

/**
 * Class representing a bolt entity shot by crossbows
 *
 * @property damageSourceProducer The factory used to compute what type of damage to inflict
 * @property drop The item that is dropped by the bolt
 * @property damage The damage that this bolt will do
 * @property chanceToDropHitEntity The chance that the bolt will drop its item after hitting an entity
 * @property chanceToDropHitGround The chance that the bolt will drop its item after hitting the ground
 */
abstract class BoltEntity(entityType: EntityType<out BoltEntity>, world: World) : AbstractArrowEntity(entityType, world) {
    abstract val damageSourceProducer: (PlayerEntity) -> DamageSource
    abstract val drop: Item
    open val damage: Int = 6
    open val chanceToDropHitEntity: Double = 0.4
    open val chanceToDropHitGround: Double = 0.8
    open val research: Research? = null

    internal var hasResearch = false

    fun setShotFrom(entity: LivingEntity) {
        owner = entity
        hasResearch = research == null || (entity is PlayerEntity && entity.getResearch().isResearched(research!!))
        if (!hasResearch && !entity.level.isClientSide) {
            entity.sendMessage(TranslationTextComponent("message.afraidofthedark.bolt.dont_understand", typeName))
        }
        pickup = if (entity is PlayerEntity && entity.isCreative) {
            PickupStatus.DISALLOWED
        } else {
            PickupStatus.ALLOWED
        }
        setPos(entity.x, entity.eyeY - 0.1, entity.z)
        shootFromRotation(entity, entity.xRot, entity.yRot, 0f, 5f, 0f)
    }

    override fun shoot(xDir: Double, yDir: Double, zDir: Double, speed: Float, inaccuracy: Float) {
        // Bolts can only be shot from crossbows, or they're super inaccurate
        super.shoot(xDir, yDir, zDir, if (shotFromCrossbow() && hasResearch) 5.0f else 0.3f, if (shotFromCrossbow() && hasResearch) 0.0f else 30.0f)
    }

    /**
     * Called when the bolt hits something
     *
     * @param result The object containing hit information
     */
    override fun onHit(result: RayTraceResult) {
        super.onHit(result)

        // Server side processing only
        if (!level.isClientSide) {
            // Test if we hit an entity or the ground
            if (result.type == RayTraceResult.Type.ENTITY) {
                val entityHit = (result as EntityRayTraceResult).entity
                // Test if the shooter of the bolt is a player and the player has the right research
                if (owner is PlayerEntity && hasResearch) {
                    entityHit.hurt(damageSourceProducer(owner as PlayerEntity), damage.toFloat())
                }

                if (Math.random() >= chanceToDropHitEntity) {
                    remove()
                }
            } else {
                if (Math.random() >= chanceToDropHitGround) {
                    remove()
                }
            }
        }
    }

    override fun onHitEntity(result: EntityRayTraceResult) {
        // Called when an entity is hit, no op since we do that calculation in onHit()
    }

    override fun getPickupItem(): ItemStack {
        return ItemStack(drop)
    }

    override fun getAddEntityPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }
}