package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.entity.enaria.GhastlyEnariaEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.world.level.block.state.BlockState

/**
 * Class that represents a ghastly enaria tile entity
 *
 * @constructor just sets the block type
 */
class GhastlyEnariaSpawnerTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDTickingTileEntity(ModTileEntities.GHASTLY_ENARIA_SPAWNER) {
    /**
     * Update gets called every tick
     */
    override fun tick() {
        super.tick()
        // Server side only processing
        if (level?.isClientSide == false) {
            // Only check every 100 ticks
            if (ticksExisted % 100 == 0L) {
                // Find all nearby enaria entities
                val distanceBetweenIslands = Constants.DISTANCE_BETWEEN_ISLANDS / 2
                val enariaEntities = level!!.getEntitiesOfClass(
                    GhastlyEnariaEntity::class.java,
                    AxisAlignedBB(blockPos, blockPos.above()).inflate(
                        distanceBetweenIslands.toDouble(),
                        distanceBetweenIslands.toDouble(),
                        distanceBetweenIslands.toDouble()
                    )
                )

                // True if enaria is alive, false otherwise
                val enariaAlive = enariaEntities.any { it.isAlive }

                // If she's not alive, spawn her
                if (!enariaAlive) {
                    // Spawn her at ground level to start
                    val enariaSpawn = GhastlyEnariaEntity(ModEntities.GHASTLY_ENARIA, level!!)
                    enariaSpawn.moveTo(
                        blockPos.x + 0.5,
                        blockPos.y + 10.2,
                        blockPos.z + 0.5,
                        level!!.random.nextFloat(),
                        0f
                    )
                    level!!.addFreshEntity(enariaSpawn)
                }
            }
        }
    }
}