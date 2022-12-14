package com.davidm1a2.afraidofthedark.common.entity.spell

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.entity.spell.base.SpellEffectEntity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import java.awt.Color

class SpellWallEntity : SpellEffectEntity {
    var color: Color
        get() = this.entityData[COLOR]
        private set(value) {
            this.entityData[COLOR] = value
        }

    // Height and width are vectors, since the wall may be rotated so width does
    // not mean x-axis, and height does not mean y-axis
    var width: Vector3d
        get() = this.entityData[WIDTH]
        private set(value) {
            this.entityData[WIDTH] = value
        }
    var height: Vector3d
        get() = this.entityData[HEIGHT]
        private set(value) {
            this.entityData[HEIGHT] = value
        }

    constructor(
        entityType: EntityType<out SpellWallEntity>,
        world: World
    ) : super(entityType, world)

    constructor(
        world: World,
        centerPos: Vector3d,
        width: Vector3d,
        height: Vector3d,
        color: Color
    ) : super(ModEntities.SPELL_WALL, world, centerPos, 12) {
        this.color = color
        this.width = width
        this.height = height
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(COLOR, Color.WHITE)
        this.entityData.define(WIDTH, Vector3d(1.0, 0.0, 0.0))
        this.entityData.define(HEIGHT, Vector3d(0.0, 1.0, 0.0))
    }

    override fun onSyncedDataUpdated(dataParameter: DataParameter<*>) {
        super.onSyncedDataUpdated(dataParameter)
        if (dataParameter == WIDTH || dataParameter == HEIGHT) {
            val position = position()
            this.renderBoundingBox = AxisAlignedBB(
                position.subtract(width).subtract(height),
                position.add(width).add(height)
            )
        }
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        super.readAdditionalSaveData(compound)
        this.color = Color(compound.getInt("red"), compound.getInt("green"), compound.getInt("blue"))
        this.width = Vector3d(compound.getDouble("width_x"), compound.getDouble("width_y"), compound.getDouble("width_z"))
        this.height = Vector3d(compound.getDouble("height_x"), compound.getDouble("height_y"), compound.getDouble("height_z"))
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.putInt("red", color.red)
        compound.putInt("green", color.green)
        compound.putInt("blue", color.blue)
        compound.putDouble("width_x", width.x)
        compound.putDouble("width_y", width.y)
        compound.putDouble("width_z", width.z)
        compound.putDouble("height_x", height.x)
        compound.putDouble("height_y", height.y)
        compound.putDouble("height_z", height.z)
    }

    companion object {
        private val COLOR = EntityDataManager.defineId(SpellWallEntity::class.java, ModDataSerializers.COLOR)
        private val WIDTH = EntityDataManager.defineId(SpellWallEntity::class.java, ModDataSerializers.VECTOR3D)
        private val HEIGHT = EntityDataManager.defineId(SpellWallEntity::class.java, ModDataSerializers.VECTOR3D)
    }
}