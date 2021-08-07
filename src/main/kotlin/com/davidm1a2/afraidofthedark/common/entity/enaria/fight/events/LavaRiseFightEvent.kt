package com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events

import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.EnariaFight
import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import kotlin.random.Random

class LavaRiseFightEvent(fight: EnariaFight) : EnariaFightEvent(fight, EnariaFightEvents.LavaRise) {
    private var currentLavaLevel: Int = 0
    private var ticksUntilNextLevel: Int = 0
    private var ticksUntilEnd: Int = 0

    override fun start() {
        currentLavaLevel = 0
        ticksUntilNextLevel = 1
        ticksUntilEnd = Random.nextInt(20 * MIN_EVENT_TIME_SEC, 20 * MAX_EVENT_TIME_SEC)
    }

    override fun forceStop() {
        while (currentLavaLevel > 0) {
            lowerLavaLevel()
            currentLavaLevel = currentLavaLevel - 1
        }
        lowerLavaLevel()
        ticksUntilEnd = 0
    }

    override fun tick() {
        // Lava is risen and we're waiting for the event to finish
        if (ticksUntilEnd > 0 && currentLavaLevel == MAX_LAVA_LEVEL) {
            ticksUntilEnd = ticksUntilEnd - 1
            return
        }

        // Lava should be going up or down
        ticksUntilNextLevel = ticksUntilNextLevel - 1
        if (ticksUntilNextLevel == 0) {
            ticksUntilNextLevel = TICKS_BETWEEN_LEVELS
            if (ticksUntilEnd > 0 && currentLavaLevel < MAX_LAVA_LEVEL) {
                // Raise the lava level
                raiseLavaLevel()
                currentLavaLevel = currentLavaLevel + 1
            } else if (ticksUntilEnd == 0 && currentLavaLevel >= 0) {
                // Lower the lava level
                currentLavaLevel = currentLavaLevel - 1
                lowerLavaLevel()
            }
        }
    }

    override fun isOver(): Boolean {
        return ticksUntilEnd <= 0 && currentLavaLevel == 0
    }

    private fun raiseLavaLevel() {
        val world = fight.enaria.level
        iterateOverRegion(relativeToAbsolutePosition(-30, -1 + currentLavaLevel, -3), relativeToAbsolutePosition(30, -1 + currentLavaLevel, 79)) {
            if (world.isEmptyBlock(it)) {
                world.setBlockAndUpdate(it, Blocks.LAVA.defaultBlockState())
            }
        }
    }

    private fun lowerLavaLevel() {
        val world = fight.enaria.level
        iterateOverRegion(relativeToAbsolutePosition(-30, -1 + currentLavaLevel, -3), relativeToAbsolutePosition(30, -1 + currentLavaLevel, 79)) {
            if (world.getBlockState(it).block == Blocks.LAVA) {
                world.setBlockAndUpdate(it, Blocks.AIR.defaultBlockState())
            }
        }
    }

    override fun serializeNBT(): CompoundNBT {
        val nbt = super.serializeNBT()

        nbt.putInt(NBT_CURRENT_LAVA_LEVEL, currentLavaLevel)
        nbt.putInt(NBT_TICKS_UNTIL_NEXT_LEVEL, ticksUntilNextLevel)
        nbt.putInt(NBT_TICKS_UNTIL_END, ticksUntilEnd)

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        super.deserializeNBT(nbt)

        currentLavaLevel = nbt.getInt(NBT_CURRENT_LAVA_LEVEL)
        ticksUntilNextLevel = nbt.getInt(NBT_TICKS_UNTIL_NEXT_LEVEL)
        ticksUntilEnd = nbt.getInt(NBT_TICKS_UNTIL_END)
    }

    companion object {
        private const val NBT_CURRENT_LAVA_LEVEL = "current_lava_level"
        private const val NBT_TICKS_UNTIL_NEXT_LEVEL = "ticks_until_next_level"
        private const val NBT_TICKS_UNTIL_END = "ticks_until_end"

        private const val MAX_LAVA_LEVEL = 3
        private const val TICKS_BETWEEN_LEVELS = 60
        private const val MIN_EVENT_TIME_SEC = 30
        private const val MAX_EVENT_TIME_SEC = 40
    }
}