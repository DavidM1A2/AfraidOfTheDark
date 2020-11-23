package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.entity.enaria.GhastlyEnariaEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.util.math.AxisAlignedBB

/**
 * Class that represents a ghastly enaria tile entity
 *
 * @constructor just sets the block type
 */
class GhastlyEnariaSpawnerTileEntity : AOTDTickingTileEntity(ModTileEntities.GHASTLY_ENARIA_SPAWNER) {
    /**
     * Update gets called every tick
     */
    override fun tick() {
        super.tick()
        // Server side only processing
        if (world?.isRemote == false) {
            // Only check every 100 ticks
            if (ticksExisted % 100 == 0L) {
                // Find all nearby enaria entities
                val distanceBetweenIslands = Constants.DISTANCE_BETWEEN_ISLANDS / 2
                val enariaEntities = world!!.getEntitiesWithinAABB(
                    GhastlyEnariaEntity::class.java,
                    AxisAlignedBB(getPos(), getPos().up()).grow(
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
                    val enariaSpawn = GhastlyEnariaEntity(ModEntities.GHASTLY_ENARIA, world!!)
                    enariaSpawn.forceSpawn = true
                    enariaSpawn.setPositionAndRotation(
                        getPos().x + 0.5,
                        getPos().y + 10.2,
                        getPos().z + 0.5,
                        world!!.rand.nextFloat(),
                        0f
                    )
                    world!!.addEntity(enariaSpawn)
                }
            }
        }
    }
}