package com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.EnariaFight
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.Blocks

class RegenerateRoomFightEvent(fight: EnariaFight) : EnariaFightEvent(fight, EnariaFightEvents.RegenerateRoom) {
    private var currentLayer: Int = 0
    private var ticksExisted: Int = 0

    override fun start() {
        currentLayer = ModSchematics.ENARIA_LAIR.getHeight().toInt()
        ticksExisted = 0
    }

    override fun tick() {
        // Refresh a layer every 20 ticks
        if (ticksExisted % 20 == 0) {
            currentLayer = currentLayer - 1
            val y = -2 + currentLayer
            val width = ModSchematics.ENARIA_LAIR.getWidth()
            val length = ModSchematics.ENARIA_LAIR.getLength()
            for (x in -30..30) {
                for (z in -3..79) {
                    val worldPos = relativeToAbsolutePosition(x, y, z)
                    val expectedBlockState = ModSchematics.ENARIA_LAIR.getBlocks()[(x + 30) + (y + 2) * length * width + (z + 3) * width].rotate(fight.rotation)
                    val expectedBlock = expectedBlockState.block
                    if (expectedBlock == Blocks.STRUCTURE_VOID) {
                        fight.enaria.level.setBlockAndUpdate(worldPos, Blocks.CAVE_AIR.defaultBlockState())
                    } else if (!expectedBlockState.hasBlockEntity() && expectedBlock != Blocks.AIR) {
                        fight.enaria.level.setBlockAndUpdate(worldPos, expectedBlockState)
                    }
                }
            }
        }
        ticksExisted = ticksExisted + 1
    }

    override fun forceStop() {
        // NOOP, nothing to cleanup
    }

    override fun isOver(): Boolean {
        return currentLayer == 0
    }

    override fun serializeNBT(): CompoundTag {
        val nbt = super.serializeNBT()

        nbt.putInt(NBT_CURRENT_LAYER, currentLayer)
        nbt.putInt(NBT_TICKS_EXISTED, ticksExisted)

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        super.deserializeNBT(nbt)

        currentLayer = nbt.getInt(NBT_CURRENT_LAYER)
        ticksExisted = nbt.getInt(NBT_TICKS_EXISTED)
    }

    companion object {
        private const val NBT_CURRENT_LAYER = "current_layer"
        private const val NBT_TICKS_EXISTED = "ticks_existed"
    }
}