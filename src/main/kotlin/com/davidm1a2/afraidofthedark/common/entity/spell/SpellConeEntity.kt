package com.davidm1a2.afraidofthedark.common.entity.spell

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.entity.spell.base.SpellEffectEntity
import com.davidm1a2.afraidofthedark.common.utility.ConeUtils
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.awt.Color

class SpellConeEntity : SpellEffectEntity {
    var color: Color
        get() = this.entityData[COLOR]
        private set(value) {
            this.entityData[COLOR] = value
        }
    var radius: Float
        get() = this.entityData[RADIUS]
        private set(value) {
            this.entityData[RADIUS] = value
        }
    var length: Float
        get() = this.entityData[LENGTH]
        private set(value) {
            this.entityData[LENGTH] = value
        }
    var direction: Vec3
        get() = this.entityData[DIRECTION]
        private set(value) {
            this.entityData[DIRECTION] = value
        }

    constructor(
        entityType: EntityType<out SpellConeEntity>,
        world: Level
    ) : super(entityType, world)

    constructor(
        world: Level,
        tipPos: Vec3,
        radius: Float,
        length: Float,
        color: Color,
        direction: Vec3
    ) : super(ModEntities.SPELL_CONE, world, tipPos, 12) {
        this.color = color
        this.radius = radius
        this.length = length
        this.direction = direction
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(COLOR, Color.WHITE)
        this.entityData.define(RADIUS, 1f)
        this.entityData.define(LENGTH, 2f)
        this.entityData.define(DIRECTION, Vec3(0.0, 1.0, 0.0))
    }

    override fun onSyncedDataUpdated(dataParameter: EntityDataAccessor<*>) {
        super.onSyncedDataUpdated(dataParameter)
        if (dataParameter == RADIUS || dataParameter == LENGTH || dataParameter == DIRECTION) {
            this.renderBoundingBox = ConeUtils.getBoundingBox(position(), direction, radius.toDouble(), length.toDouble())
        }
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        this.color = Color(compound.getInt("red"), compound.getInt("green"), compound.getInt("blue"))
        this.radius = compound.getFloat("radius")
        this.length = compound.getFloat("length")
        this.direction = Vec3(compound.getDouble("direction_x"), compound.getDouble("direction_y"), compound.getDouble("direction_z"))
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putInt("red", color.red)
        compound.putInt("green", color.green)
        compound.putInt("blue", color.blue)
        compound.putFloat("radius", radius)
        compound.putFloat("length", length)
        compound.putDouble("direction_x", direction.x)
        compound.putDouble("direction_y", direction.y)
        compound.putDouble("direction_z", direction.z)
    }

    companion object {
        private val COLOR = SynchedEntityData.defineId(SpellConeEntity::class.java, ModDataSerializers.COLOR)
        private val RADIUS = SynchedEntityData.defineId(SpellConeEntity::class.java, EntityDataSerializers.FLOAT)
        private val LENGTH = SynchedEntityData.defineId(SpellConeEntity::class.java, EntityDataSerializers.FLOAT)
        private val DIRECTION = SynchedEntityData.defineId(SpellConeEntity::class.java, ModDataSerializers.VECTOR3D)
    }
}