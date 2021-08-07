package com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events

import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.EnariaFight
import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import kotlin.random.Random

class WaterFallFightEvent(fight: EnariaFight) : EnariaFightEvent(fight, EnariaFightEvents.WaterFall) {
    private var ticksUntilEnd: Int = 0
    private var ticksUntilWaterGone: Int = 0

    override fun start() {
        ticksUntilEnd = Random.nextInt(20 * MIN_EVENT_TIME_SEC, 20 * MAX_EVENT_TIME_SEC)
        ticksUntilWaterGone = TIME_TO_MAKE_WATER_DISAPPEAR
        val world = fight.enaria.level
        iterateOverRegion(relativeToAbsolutePosition(-30, 11, -3), relativeToAbsolutePosition(30, 11, 79)) {
            if (world.isEmptyBlock(it)) {
                world.setBlockAndUpdate(it, Blocks.WATER.defaultBlockState())
            }
        }
    }

    override fun forceStop() {
        ticksUntilEnd = 0
        clearArenaSourceWater()
    }

    override fun tick() {
        ticksUntilEnd = ticksUntilEnd - 1

        if (ticksUntilEnd == 0) {
            clearArenaSourceWater()
        }
        if (ticksUntilEnd < 0) {
            ticksUntilWaterGone = ticksUntilWaterGone - 1
            if (ticksUntilWaterGone == 0) {
                clearAllArenaWater()
            }
        }
    }

    private fun clearArenaSourceWater() {
        val world = fight.enaria.level
        val cornerOne = relativeToAbsolutePosition(-30, 11, -3)
        val cornerTwo = relativeToAbsolutePosition(30, 11, 79)
        iterateOverRegion(cornerOne, cornerTwo) {
            if (world.getBlockState(it).block == Blocks.WATER) {
                world.setBlockAndUpdate(it, Blocks.AIR.defaultBlockState())
            }
        }

        spawnEventParticles(List(30) { getRandomVectorBetween(cornerOne, cornerTwo) })
    }

    private fun clearAllArenaWater() {
        val world = fight.enaria.level
        val cornerOne = relativeToAbsolutePosition(-30, -1, -3)
        val cornerTwo = relativeToAbsolutePosition(30, 11, 79)
        iterateOverRegion(cornerOne, cornerTwo) {
            if (world.getBlockState(it).block == Blocks.WATER) {
                world.setBlockAndUpdate(it, Blocks.AIR.defaultBlockState())
            }
        }
    }

    override fun isOver(): Boolean {
        return ticksUntilEnd <= 0 && ticksUntilWaterGone <= 0
    }

    override fun serializeNBT(): CompoundNBT {
        val nbt = super.serializeNBT()

        nbt.putInt(NBT_TICKS_UNTIL_END, ticksUntilEnd)
        nbt.putInt(NBT_TICKS_UNTIL_WATER_GONE, ticksUntilWaterGone)

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        super.deserializeNBT(nbt)
        ticksUntilEnd = nbt.getInt(NBT_TICKS_UNTIL_END)
        ticksUntilWaterGone = nbt.getInt(NBT_TICKS_UNTIL_WATER_GONE)
    }

    companion object {
        private const val NBT_TICKS_UNTIL_END = "ticks_until_end"
        private const val NBT_TICKS_UNTIL_WATER_GONE = "ticks_until_water_gone"

        private const val MIN_EVENT_TIME_SEC = 15
        private const val MAX_EVENT_TIME_SEC = 25
        private const val TIME_TO_MAKE_WATER_DISAPPEAR = 20 * 20 // 25 sec
    }
}