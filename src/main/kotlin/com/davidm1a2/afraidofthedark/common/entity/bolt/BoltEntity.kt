package com.davidm1a2.afraidofthedark.common.entity.bolt

import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.network.protocol.Packet
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraftforge.fmllegacy.network.NetworkHooks

/**
 * Class representing a bolt entity shot by crossbows
 *
 * @property damageSourceProducer The factory used to compute what type of damage to inflict
 * @property drop The item that is dropped by the bolt
 * @property damage The damage that this bolt will do
 * @property chanceToDropHitEntity The chance that the bolt will drop its item after hitting an entity
 * @property chanceToDropHitGround The chance that the bolt will drop its item after hitting the ground
 */
abstract class BoltEntity(entityType: EntityType<out BoltEntity>, level: Level) : AbstractArrow(entityType, level) {
    internal abstract val damageSourceProducer: (Player) -> DamageSource
    internal abstract val drop: Item
    internal open val damage: Int = 6
    internal open val chanceToDropHitEntity: Double = 0.4
    internal open val chanceToDropHitGround: Double = 0.8
    internal var hasResearch = false

    fun initUsingShooter(shooter: LivingEntity, hasResearch: Boolean) {
        owner = shooter
        this.hasResearch = hasResearch
        if (!hasResearch && !shooter.level.isClientSide) {
            shooter.sendMessage(TranslatableComponent("message.afraidofthedark.bolt.dont_understand", typeName))
        }
        pickup = if (shooter is Player && shooter.isCreative) {
            Pickup.DISALLOWED
        } else {
            Pickup.ALLOWED
        }
        setPos(shooter.x, shooter.eyeY - 0.1, shooter.z)
        shootFromRotation(shooter, shooter.xRot, shooter.yRot, 0f, BOLT_SPEED, 0f)
    }

    override fun shoot(xDir: Double, yDir: Double, zDir: Double, speed: Float, inaccuracy: Float) {
        // Bolts can only be shot from crossbows, or they're super inaccurate
        super.shoot(xDir, yDir, zDir, if (shotFromCrossbow() && hasResearch) BOLT_SPEED else 0.3f, if (shotFromCrossbow() && hasResearch) 0.0f else 30.0f)
    }

    /**
     * Called when the bolt hits something
     *
     * @param result The object containing hit information
     */
    override fun onHit(result: HitResult) {
        super.onHit(result)

        // Server side processing only
        if (!level.isClientSide) {
            // Test if we hit an entity or the ground
            if (result.type == HitResult.Type.ENTITY) {
                val entityHit = (result as EntityHitResult).entity
                // Test if the shooter of the bolt is a player and the player has the right research
                if (owner is Player && hasResearch) {
                    entityHit.hurt(damageSourceProducer(owner as Player), damage.toFloat())
                }

                if (Math.random() >= chanceToDropHitEntity) {
                    remove(RemovalReason.DISCARDED)
                }
            } else {
                if (Math.random() >= chanceToDropHitGround) {
                    remove(RemovalReason.DISCARDED)
                }
            }
        }
    }

    override fun onHitEntity(result: EntityHitResult) {
        // Called when an entity is hit, no op since we do that calculation in onHit()
    }

    override fun getPickupItem(): ItemStack {
        return ItemStack(drop)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        hasResearch = compound.getBoolean("has_research")
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putBoolean("has_research", hasResearch)
    }

    override fun getAddEntityPacket(): Packet<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    companion object {
        private const val BOLT_SPEED = 5f
    }
}