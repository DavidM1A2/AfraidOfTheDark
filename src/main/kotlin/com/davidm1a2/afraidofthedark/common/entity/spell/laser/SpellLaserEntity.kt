package com.davidm1a2.afraidofthedark.common.entity.spell.laser

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.entity.spell.SpellEffectEntity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import java.awt.Color

class SpellLaserEntity : SpellEffectEntity {
    var endPos: Vector3d
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
        world: World
    ) : super(entityType, world)

    constructor(
        world: World,
        startPos: Vector3d,
        endPos: Vector3d,
        color: Color
    ) : super(ModEntities.SPELL_LASER, world, 10) {
        this.endPos = endPos
        this.color = color
        this.setPos(startPos.x, startPos.y, startPos.z)
        this.setRot(0f, 0f)
        this.deltaMovement = Vector3d(0.0, 0.0, 0.0)
    }

    override fun defineSynchedData() {
        this.entityData.define(END_POS, Vector3d(0.0, 0.0, 0.0))
        this.entityData.define(COLOR, Color.RED)
    }

    override fun onSyncedDataUpdated(dataParameter: DataParameter<*>) {
        super.onSyncedDataUpdated(dataParameter)
        if (dataParameter == END_POS) {
            this.renderBoundingBox = AxisAlignedBB(position(), this.endPos)
        }
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        super.readAdditionalSaveData(compound)
        this.endPos = Vector3d(compound.getDouble("end_x"), compound.getDouble("end_y"), compound.getDouble("end_z"))
        this.color = Color(compound.getInt("red"), compound.getInt("green"), compound.getInt("blue"))
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.putDouble("end_x", endPos.x)
        compound.putDouble("end_y", endPos.y)
        compound.putDouble("end_z", endPos.z)
        compound.putInt("red", color.red)
        compound.putInt("green", color.green)
        compound.putInt("blue", color.blue)
    }

    companion object {
        private val END_POS = EntityDataManager.defineId(SpellLaserEntity::class.java, ModDataSerializers.VECTOR3D)
        private val COLOR = EntityDataManager.defineId(SpellLaserEntity::class.java, ModDataSerializers.COLOR)
    }
}