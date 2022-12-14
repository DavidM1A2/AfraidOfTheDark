package com.davidm1a2.afraidofthedark.common.entity.spell

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.entity.spell.base.SpellEffectEntity
import com.davidm1a2.afraidofthedark.common.utility.ConeUtils
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
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
    var direction: Vector3d
        get() = this.entityData[DIRECTION]
        private set(value) {
            this.entityData[DIRECTION] = value
        }

    constructor(
        entityType: EntityType<out SpellConeEntity>,
        world: World
    ) : super(entityType, world)

    constructor(
        world: World,
        tipPos: Vector3d,
        radius: Float,
        length: Float,
        color: Color,
        direction: Vector3d
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
        this.entityData.define(DIRECTION, Vector3d(0.0, 1.0, 0.0))
    }

    override fun onSyncedDataUpdated(dataParameter: DataParameter<*>) {
        super.onSyncedDataUpdated(dataParameter)
        if (dataParameter == RADIUS || dataParameter == LENGTH || dataParameter == DIRECTION) {
            this.renderBoundingBox = ConeUtils.getBoundingBox(position(), direction, radius.toDouble(), length.toDouble())
        }
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        super.readAdditionalSaveData(compound)
        this.color = Color(compound.getInt("red"), compound.getInt("green"), compound.getInt("blue"))
        this.radius = compound.getFloat("radius")
        this.length = compound.getFloat("length")
        this.direction = Vector3d(compound.getDouble("direction_x"), compound.getDouble("direction_y"), compound.getDouble("direction_z"))
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
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
        private val COLOR = EntityDataManager.defineId(SpellConeEntity::class.java, ModDataSerializers.COLOR)
        private val RADIUS = EntityDataManager.defineId(SpellConeEntity::class.java, DataSerializers.FLOAT)
        private val LENGTH = EntityDataManager.defineId(SpellConeEntity::class.java, DataSerializers.FLOAT)
        private val DIRECTION = EntityDataManager.defineId(SpellConeEntity::class.java, ModDataSerializers.VECTOR3D)
    }
}