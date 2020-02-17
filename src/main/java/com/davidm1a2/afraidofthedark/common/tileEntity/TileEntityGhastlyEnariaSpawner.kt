package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityGhastlyEnaria
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.util.math.AxisAlignedBB

/**
 * Class that represents a ghastly enaria tile entity
 *
 * @constructor just sets the block type
 */
class TileEntityGhastlyEnariaSpawner : AOTDTickingTileEntity(ModBlocks.ENARIA_SPAWNER)
{
    /**
     * Update gets called every tick
     */
    override fun update()
    {
        super.update()
        // Server side only processing
        if (!world.isRemote)
        {
            // Only check every 100 ticks
            if (ticksExisted % 100 == 0L)
            {
                // Find all nearby enaria entities
                val distanceBetweenIslands = AfraidOfTheDark.INSTANCE.configurationHandler.blocksBetweenIslands / 2
                val enariaEntities = world.getEntitiesWithinAABB(
                        EntityGhastlyEnaria::class.java,
                        AxisAlignedBB(getPos(), getPos().up()).grow(distanceBetweenIslands.toDouble(), distanceBetweenIslands.toDouble(), distanceBetweenIslands.toDouble())
                )

                // True if enaria is alive, false otherwise
                val enariaAlive = enariaEntities.any { !it.isDead }

                // If she's not alive, spawn her
                if (!enariaAlive)
                {
                    // Spawn her at ground level to start
                    val enariaSpawn = EntityGhastlyEnaria(world)
                    enariaSpawn.forceSpawn = true
                    enariaSpawn.setPositionAndRotation(getPos().x + 0.5, getPos().y + 10.2, getPos().z + 0.5, world.rand.nextFloat(), 0f)
                    world.spawnEntity(enariaSpawn)
                }
            }
        }
    }
}