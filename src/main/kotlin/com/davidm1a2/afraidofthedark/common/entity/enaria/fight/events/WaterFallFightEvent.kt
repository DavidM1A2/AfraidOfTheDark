package com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events

import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.EnariaFight
import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import kotlin.random.Random

class WaterFallFightEvent(fight: EnariaFight) : EnariaFightEvent(fight, EnariaFightEvents.WaterFall) {
    private var ticksUntilEnd: Int = 0

    override fun start() {
        ticksUntilEnd = Random.nextInt(20 * MIN_EVENT_TIME_SEC, 20 * MAX_EVENT_TIME_SEC)
        val world = fight.enaria.world
        iterateOverRegion(relativeToAbsolutePosition(-30, 11, -3), relativeToAbsolutePosition(30, 11, 79)) {
            if (world.getBlockState(it).isAir(world, it)) {
                world.setBlockState(it, Blocks.WATER.defaultState)
            }
        }
    }

    override fun forceStop() {
        ticksUntilEnd = 0
        clearArenaWater()
    }

    override fun tick() {
        ticksUntilEnd = ticksUntilEnd - 1

        if (ticksUntilEnd == 0) {
            clearArenaWater()
        }
    }

    private fun clearArenaWater() {
        val world = fight.enaria.world
        iterateOverRegion(relativeToAbsolutePosition(-30, 11, -3), relativeToAbsolutePosition(30, 11, 79)) {
            if (world.getBlockState(it).block == Blocks.WATER) {
                world.setBlockState(it, Blocks.AIR.defaultState)
            }
        }
    }

    override fun isOver(): Boolean {
        return ticksUntilEnd <= 0
    }

    override fun serializeNBT(): CompoundNBT {
        val nbt = super.serializeNBT()

        nbt.putInt(NBT_TICKS_UNTIL_END, ticksUntilEnd)

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        super.deserializeNBT(nbt)
        ticksUntilEnd = nbt.getInt(NBT_TICKS_UNTIL_END)
    }

    companion object {
        private const val NBT_TICKS_UNTIL_END = "ticks_until_end"

        private const val MIN_EVENT_TIME_SEC = 30
        private const val MAX_EVENT_TIME_SEC = 40
    }
}