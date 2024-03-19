package com.davidm1a2.afraidofthedark.common.entity.spell.base

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraftforge.fmllegacy.network.NetworkHooks

abstract class SpellEffectEntity(
    entityType: EntityType<*>,
    world: Level
) : Entity(entityType, world) {
    // RenderBoundingBox defaults to 0 server side, and gets initialized properly client side
    protected var renderBoundingBox = AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    var lifespanTicks: Int
        get() = this.entityData[LIFESPAN_TICKS]
        private set(value) {
            this.entityData[LIFESPAN_TICKS] = value
        }

    constructor(entityType: EntityType<*>, world: Level, startPos: Vec3, lifespanTicks: Int) : this(entityType, world) {
        this.lifespanTicks = lifespanTicks
        this.setPos(startPos.x, startPos.y, startPos.z)
        this.setRot(0f, 0f)
        this.deltaMovement = Vec3(0.0, 0.0, 0.0)
    }

    override fun tick() {
        super.tick()

        if (!level.isClientSide) {
            if (tickCount > lifespanTicks) {
                remove(RemovalReason.DISCARDED)
            }
        }
    }

    override fun shouldRenderAtSqrDistance(squareDistance: Double): Boolean {
        // Copy & Pasted from base class, except uses culling AABB instead of the regular AABB
        var boundingBoxSize = this.boundingBoxForCulling.size
        if (boundingBoxSize.isNaN()) {
            boundingBoxSize = 1.0
        }
        boundingBoxSize = boundingBoxSize * 64.0 * getViewScale()
        return squareDistance < boundingBoxSize * boundingBoxSize
    }

    override fun defineSynchedData() {
        this.entityData.define(LIFESPAN_TICKS, 0)
    }

    override fun getBoundingBoxForCulling(): AABB {
        return renderBoundingBox
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        this.lifespanTicks = compound.getInt(NBT_LIFESPAN_TICKS)
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        compound.putInt(NBT_LIFESPAN_TICKS, lifespanTicks)
    }

    override fun getAddEntityPacket(): Packet<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    companion object {
        private const val NBT_LIFESPAN_TICKS = "lifespan_ticks"
        private val LIFESPAN_TICKS = SynchedEntityData.defineId(SpellEffectEntity::class.java, EntityDataSerializers.INT)
    }
}