package com.davidm1a2.afraidofthedark.common.entity.spell

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.entity.spell.base.SpellEffectEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.awt.Color

class SpellAOEEntity : SpellEffectEntity {
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

    constructor(
        entityType: EntityType<out SpellAOEEntity>,
        world: Level
    ) : super(entityType, world)

    constructor(
        world: Level,
        startPos: Vec3,
        radius: Float,
        color: Color
    ) : super(ModEntities.SPELL_AOE, world, startPos, 10) {
        this.color = color
        this.radius = radius
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(COLOR, Color.WHITE)
        this.entityData.define(RADIUS, 0f)
    }

    override fun onSyncedDataUpdated(dataParameter: EntityDataAccessor<*>) {
        super.onSyncedDataUpdated(dataParameter)
        if (dataParameter == RADIUS) {
            this.renderBoundingBox = AABB(
                position().subtract(radius.toDouble(), radius.toDouble(), radius.toDouble()),
                position().add(radius.toDouble(), radius.toDouble(), radius.toDouble())
            )
        }
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        this.color = Color(compound.getInt("red"), compound.getInt("green"), compound.getInt("blue"))
        this.radius = compound.getFloat("radius")
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putInt("red", color.red)
        compound.putInt("green", color.green)
        compound.putInt("blue", color.blue)
        compound.putFloat("radius", radius)
    }

    companion object {
        private val COLOR = SynchedEntityData.defineId(SpellAOEEntity::class.java, ModDataSerializers.COLOR)
        private val RADIUS = SynchedEntityData.defineId(SpellAOEEntity::class.java, EntityDataSerializers.FLOAT)
    }
}