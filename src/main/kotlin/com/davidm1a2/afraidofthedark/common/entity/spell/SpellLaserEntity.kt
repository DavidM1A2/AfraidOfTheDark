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

class SpellLaserEntity : SpellEffectEntity {
    var endPos: Vec3
        get() = this.entityData[END_POS]
        private set(value) {
            this.entityData[END_POS] = value
        }
    var color: Color
        get() = this.entityData[COLOR]
        private set(value) {
            this.entityData[COLOR] = value
        }

    constructor(
        entityType: EntityType<out SpellLaserEntity>,
        world: Level
    ) : super(entityType, world)

    constructor(
        world: Level,
        startPos: Vec3,
        endPos: Vec3,
        color: Color
    ) : super(ModEntities.SPELL_LASER, world, startPos, 10) {
        this.endPos = endPos
        this.color = color
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(END_POS, Vec3(0.0, 0.0, 0.0))
        this.entityData.define(COLOR, Color.RED)
    }

    override fun onSyncedDataUpdated(dataParameter: EntityDataAccessor<*>) {
        super.onSyncedDataUpdated(dataParameter)
        if (dataParameter == END_POS) {
            this.renderBoundingBox = AABB(position(), this.endPos)
        }
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        this.endPos = Vec3(compound.getDouble("end_x"), compound.getDouble("end_y"), compound.getDouble("end_z"))
        this.color = Color(compound.getInt("red"), compound.getInt("green"), compound.getInt("blue"))
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putDouble("end_x", endPos.x)
        compound.putDouble("end_y", endPos.y)
        compound.putDouble("end_z", endPos.z)
        compound.putInt("red", color.red)
        compound.putInt("green", color.green)
        compound.putInt("blue", color.blue)
    }

    companion object {
        private val END_POS = SynchedEntityData.defineId(SpellLaserEntity::class.java, ModDataSerializers.VECTOR3D)
        private val COLOR = SynchedEntityData.defineId(SpellLaserEntity::class.java, ModDataSerializers.COLOR)
    }
}