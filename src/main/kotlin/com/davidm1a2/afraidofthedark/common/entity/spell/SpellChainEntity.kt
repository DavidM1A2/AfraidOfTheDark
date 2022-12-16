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

class SpellChainEntity : SpellEffectEntity {
    var endPos: Vector3d
        get() = this.entityData[END_POS]
        private set(value) {
            this.entityData[END_POS] = value
        }

    constructor(
        entityType: EntityType<out SpellChainEntity>,
        world: World
    ) : super(entityType, world)

    constructor(
        world: World,
        startPos: Vector3d,
        endPos: Vector3d
    ) : super(ModEntities.SPELL_CHAIN, world, startPos, 10) {
        this.endPos = endPos
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(END_POS, Vector3d(0.0, 0.0, 0.0))
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
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.putDouble("end_x", endPos.x)
        compound.putDouble("end_y", endPos.y)
        compound.putDouble("end_z", endPos.z)
    }

    companion object {
        private val END_POS = EntityDataManager.defineId(SpellChainEntity::class.java, ModDataSerializers.VECTOR3D)
    }
}