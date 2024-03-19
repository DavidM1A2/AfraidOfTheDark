package com.davidm1a2.afraidofthedark.common.entity.spell

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.entity.spell.base.SpellEffectEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.awt.Color

class SpellWallEntity : SpellEffectEntity {
    var color: Color
        get() = this.entityData[COLOR]
        private set(value) {
            this.entityData[COLOR] = value
        }

    // Height and width are vectors, since the wall may be rotated so width does
    // not mean x-axis, and height does not mean y-axis
    var width: Vec3
        get() = this.entityData[WIDTH]
        private set(value) {
            this.entityData[WIDTH] = value
        }
    var height: Vec3
        get() = this.entityData[HEIGHT]
        private set(value) {
            this.entityData[HEIGHT] = value
        }

    constructor(
        entityType: EntityType<out SpellWallEntity>,
        world: Level
    ) : super(entityType, world)

    constructor(
        world: Level,
        centerPos: Vec3,
        width: Vec3,
        height: Vec3,
        color: Color
    ) : super(ModEntities.SPELL_WALL, world, centerPos, 12) {
        this.color = color
        this.width = width
        this.height = height
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(COLOR, Color.WHITE)
        this.entityData.define(WIDTH, Vec3(1.0, 0.0, 0.0))
        this.entityData.define(HEIGHT, Vec3(0.0, 1.0, 0.0))
    }

    override fun onSyncedDataUpdated(dataParameter: EntityDataAccessor<*>) {
        super.onSyncedDataUpdated(dataParameter)
        if (dataParameter == WIDTH || dataParameter == HEIGHT) {
            val position = position()
            this.renderBoundingBox = AABB(
                position.subtract(width).subtract(height),
                position.add(width).add(height)
            )
        }
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        this.color = Color(compound.getInt("red"), compound.getInt("green"), compound.getInt("blue"))
        this.width = Vec3(compound.getDouble("width_x"), compound.getDouble("width_y"), compound.getDouble("width_z"))
        this.height = Vec3(compound.getDouble("height_x"), compound.getDouble("height_y"), compound.getDouble("height_z"))
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
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
        private val COLOR = SynchedEntityData.defineId(SpellWallEntity::class.java, ModDataSerializers.COLOR)
        private val WIDTH = SynchedEntityData.defineId(SpellWallEntity::class.java, ModDataSerializers.VECTOR3D)
        private val HEIGHT = SynchedEntityData.defineId(SpellWallEntity::class.java, ModDataSerializers.VECTOR3D)
    }
}