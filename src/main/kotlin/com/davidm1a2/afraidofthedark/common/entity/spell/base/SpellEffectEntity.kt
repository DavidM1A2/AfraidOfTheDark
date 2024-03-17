package com.davidm1a2.afraidofthedark.common.entity.spell.base

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraft.world.entity.Entity
import net.minecraftforge.fml.network.NetworkHooks

abstract class SpellEffectEntity(
    entityType: EntityType<*>,
    world: World
) : Entity(entityType, world) {
    // RenderBoundingBox defaults to 0 server side, and gets initialized properly client side
    protected var renderBoundingBox: AxisAlignedBB = AxisAlignedBB.ofSize(0.0, 0.0, 0.0)
    var lifespanTicks: Int
        get() = this.entityData[LIFESPAN_TICKS]
        private set(value) {
            this.entityData[LIFESPAN_TICKS] = value
        }

    constructor(entityType: EntityType<*>, world: World, startPos: Vector3d, lifespanTicks: Int) : this(entityType, world) {
        this.lifespanTicks = lifespanTicks
        this.setPos(startPos.x, startPos.y, startPos.z)
        this.setRot(0f, 0f)
        this.deltaMovement = Vector3d(0.0, 0.0, 0.0)
    }

    override fun tick() {
        super.tick()

        if (!level.isClientSide) {
            if (tickCount > lifespanTicks) {
                remove()
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

    override fun getBoundingBoxForCulling(): AxisAlignedBB {
        return renderBoundingBox
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        this.lifespanTicks = compound.getInt(NBT_LIFESPAN_TICKS)
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        compound.putInt(NBT_LIFESPAN_TICKS, lifespanTicks)
    }

    override fun getAddEntityPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    companion object {
        private const val NBT_LIFESPAN_TICKS = "lifespan_ticks"
        private val LIFESPAN_TICKS = EntityDataManager.defineId(SpellEffectEntity::class.java, DataSerializers.INT)
    }
}