package com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events

import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.EnariaFight
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.Vec3
import net.minecraftforge.common.util.INBTSerializable
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

abstract class EnariaFightEvent(
    protected val fight: EnariaFight,
    val type: EnariaFightEvents
) : INBTSerializable<CompoundTag> {
    abstract fun start()

    abstract fun tick()

    abstract fun forceStop()

    abstract fun isOver(): Boolean

    open fun canBasicAttackDuringThis(): Boolean {
        return true
    }

    protected fun relativeToAbsolutePosition(relativeX: Int, relativeY: Int, relativeZ: Int): BlockPos {
        return BlockPos(relativeX, relativeY, relativeZ).rotate(fight.rotation).offset(fight.position)
    }

    protected fun iterateOverRegion(cornerOne: BlockPos, cornerTwo: BlockPos, processor: (BlockPos) -> Unit) {
        val minX = min(cornerOne.x, cornerTwo.x)
        val minY = min(cornerOne.y, cornerTwo.y)
        val minZ = min(cornerOne.z, cornerTwo.z)
        val maxX = max(cornerOne.x, cornerTwo.x)
        val maxY = max(cornerOne.y, cornerTwo.y)
        val maxZ = max(cornerOne.z, cornerTwo.z)
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val pos = BlockPos(x, y, z)
                    processor(pos)
                }
            }
        }
    }

    protected fun getRandomVectorBetween(pointOne: BlockPos, pointTwo: BlockPos): Vec3 {
        return Vec3(
            getRandomValueBetween(pointOne.x, pointTwo.x),
            getRandomValueBetween(pointOne.y, pointTwo.y),
            getRandomValueBetween(pointOne.z, pointTwo.z)
        )
    }

    private fun getRandomValueBetween(pointOne: Int, pointTwo: Int): Double {
        return Random.nextDouble(min(pointOne, pointTwo).toDouble(), max(pointOne, pointTwo).toDouble() + 1.0)
    }

    override fun serializeNBT(): CompoundTag {
        return CompoundTag()
    }

    override fun deserializeNBT(nbt: CompoundTag) {
    }
}