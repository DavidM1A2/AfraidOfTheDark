package com.davidm1a2.afraidofthedark.common.entity.spell.laser

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks
import java.awt.Color

class SpellLaserEntity(
    entityType: EntityType<out SpellLaserEntity>,
    world: World
) : Entity(entityType, world) {
    // RenderBoundingBox defaults to 0 server side, and gets initialized properly client side
    private var renderBoundingBox = AxisAlignedBB.ofSize(0.0, 0.0, 0.0)

    constructor(
        world: World,
        startPos: Vector3d,
        endPos: Vector3d,
        color: Color
    ) : this(ModEntities.SPELL_LASER, world) {
        this.entityData[END_POS] = endPos
        this.entityData[COLOR] = color
        this.setPos(startPos.x, startPos.y, startPos.z)
        this.setRot(0f, 0f)
        this.deltaMovement = Vector3d(0.0, 0.0, 0.0)
    }

    override fun tick() {
        super.tick()

        if (!level.isClientSide) {
            if (tickCount > LIFESPAN_TICKS) {
                remove()
            }
        }
    }

    fun getEndPos(): Vector3d {
        return this.entityData[END_POS]
    }

    fun getColor(): Color {
        return this.entityData[COLOR]
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

    override fun defineSynchedData() {
        this.entityData.define(END_POS, Vector3d(0.0, 0.0, 0.0))
        this.entityData.define(COLOR, Color.RED)
    }

    override fun onSyncedDataUpdated(dataParameter: DataParameter<*>) {
        super.onSyncedDataUpdated(dataParameter)
        if (dataParameter == END_POS) {
            this.renderBoundingBox = AxisAlignedBB(position(), getEndPos())
        }
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        this.entityData[END_POS] = Vector3d(compound.getDouble("end_x"), compound.getDouble("end_y"), compound.getDouble("end_z"))
        this.entityData[COLOR] = Color(compound.getInt("red"), compound.getInt("green"), compound.getInt("blue"))
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        val endPos = this.getEndPos()
        compound.putDouble("end_x", endPos.x)
        compound.putDouble("end_y", endPos.y)
        compound.putDouble("end_z", endPos.z)
        val color = this.getColor()
        compound.putInt("red", color.red)
        compound.putInt("green", color.green)
        compound.putInt("blue", color.blue)
    }

    override fun getAddEntityPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    companion object {
        // Lasers only live for 1/2 a second
        private const val LIFESPAN_TICKS = 10

        private val END_POS = EntityDataManager.defineId(SpellLaserEntity::class.java, ModDataSerializers.VECTOR3D)
        private val COLOR = EntityDataManager.defineId(SpellLaserEntity::class.java, ModDataSerializers.COLOR)
    }
}