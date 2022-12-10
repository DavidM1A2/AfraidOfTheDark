package com.davidm1a2.afraidofthedark.common.entity.spell

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.entity.spell.base.SpellEffectEntity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
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
        world: World
    ) : super(entityType, world)

    constructor(
        world: World,
        startPos: Vector3d,
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

    override fun onSyncedDataUpdated(dataParameter: DataParameter<*>) {
        super.onSyncedDataUpdated(dataParameter)
        if (dataParameter == RADIUS) {
            this.renderBoundingBox = AxisAlignedBB(
                position().subtract(radius.toDouble(), radius.toDouble(), radius.toDouble()),
                position().add(radius.toDouble(), radius.toDouble(), radius.toDouble())
            )
        }
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        super.readAdditionalSaveData(compound)
        this.color = Color(compound.getInt("red"), compound.getInt("green"), compound.getInt("blue"))
        this.radius = compound.getFloat("radius")
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.putInt("red", color.red)
        compound.putInt("green", color.green)
        compound.putInt("blue", color.blue)
        compound.putFloat("radius", radius)
    }

    companion object {
        private val COLOR = EntityDataManager.defineId(SpellAOEEntity::class.java, ModDataSerializers.COLOR)
        private val RADIUS = EntityDataManager.defineId(SpellAOEEntity::class.java, DataSerializers.FLOAT)
    }
}