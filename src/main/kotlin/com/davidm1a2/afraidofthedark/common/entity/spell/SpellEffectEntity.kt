package com.davidm1a2.afraidofthedark.common.entity.spell

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks

abstract class SpellEffectEntity(
    entityType: EntityType<*>,
    world: World
) : Entity(entityType, world) {
    // RenderBoundingBox defaults to 0 server side, and gets initialized properly client side
    protected var renderBoundingBox: AxisAlignedBB = AxisAlignedBB.ofSize(0.0, 0.0, 0.0)
    private var lifespanTicks = 0

    constructor(entityType: EntityType<*>, world: World, lifespanTicks: Int) : this(entityType, world) {
        this.lifespanTicks = lifespanTicks
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

    override fun getBoundingBoxForCulling(): AxisAlignedBB {
        return renderBoundingBox
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        this.lifespanTicks = compound.getInt(LIFESPAN_TICKS)
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        compound.putInt(LIFESPAN_TICKS, lifespanTicks)
    }

    override fun getAddEntityPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    companion object {
        private const val LIFESPAN_TICKS = "lifespan_ticks"
    }
}