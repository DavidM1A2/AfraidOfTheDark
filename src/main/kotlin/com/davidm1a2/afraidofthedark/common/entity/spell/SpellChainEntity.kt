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

class SpellChainEntity : SpellEffectEntity {
    var endPos: Vec3
        get() = this.entityData[END_POS]
        private set(value) {
            this.entityData[END_POS] = value
        }

    constructor(
        entityType: EntityType<out SpellChainEntity>,
        world: Level
    ) : super(entityType, world)

    constructor(
        world: Level,
        startPos: Vec3,
        endPos: Vec3
    ) : super(ModEntities.SPELL_CHAIN, world, startPos, 10) {
        this.endPos = endPos
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(END_POS, Vec3(0.0, 0.0, 0.0))
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
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putDouble("end_x", endPos.x)
        compound.putDouble("end_y", endPos.y)
        compound.putDouble("end_z", endPos.z)
    }

    companion object {
        private val END_POS = SynchedEntityData.defineId(SpellChainEntity::class.java, ModDataSerializers.VECTOR3D)
    }
}