package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EntityEnchantedFrog
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.init.Blocks
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import kotlin.random.Random

/**
 * Tile entity for the desert oasis block that spawns frogs
 *
 * @constructor sets the block type of the tile entity
 */
class TileEntityDesertOasis : AOTDTickingTileEntity(ModBlocks.DESERT_OASIS) {
    // The AABB of the desert oasis
    private lateinit var oasisBoundingBox: AxisAlignedBB

    /**
     * Update gets called every tick
     */
    override fun update() {
        super.update()
        // Server side processing only
        if (!world.isRemote) {
            // If we've existed for a multiple of 60 ticks perform a check for nearby players
            if (ticksExisted % TICKS_INBETWEEN_CHECKS == 0L) {
                if (!::oasisBoundingBox.isInitialized) {
                    computeOasisBoundingBox()
                }

                // Get all existing frogs
                val enchantedFrogs = world.getEntitiesWithinAABB(EntityEnchantedFrog::class.java, oasisBoundingBox)
                // Compute the number of frogs to spawn
                val numberOfFrogsToSpawn =
                    (MAX_NUMBER_OF_FROGS - enchantedFrogs.size).coerceIn(0, MAX_NUMBER_OF_FROGS_TO_SPAWN_AT_ONCE)
                spawnFrogs(numberOfFrogsToSpawn)
            }
        }
    }

    /**
     * Updates the oasis bounding box based on position
     */
    private fun computeOasisBoundingBox() {
        oasisBoundingBox = AxisAlignedBB(pos).grow(
            ModSchematics.DESERT_OASIS.getWidth() / 2.0,
            0.0,
            ModSchematics.DESERT_OASIS.getLength() / 2.0
        ).expand(
            0.0,
            // The DesertOasis sits at y=6 (relative to 0,0,0) so don't look up past the bounds of the structure
            ModSchematics.DESERT_OASIS.getHeight().toDouble() - 6,
            0.0
        )
    }

    /**
     * Spawn a certain number of frogs
     *
     * @param number The number to spawn
     */
    private fun spawnFrogs(number: Int) {
        for (ignored in 0 until number) {
            val xPos = Random.nextInt(oasisBoundingBox.minX.toInt(), oasisBoundingBox.maxX.toInt())
            val zPos = Random.nextInt(oasisBoundingBox.minZ.toInt(), oasisBoundingBox.maxZ.toInt())

            // Find the first surface block that the frog may spawn on
            var yPos = 0
            for (y in oasisBoundingBox.maxY.toInt() downTo oasisBoundingBox.minY.toInt()) {
                val currentBlock = world.getBlockState(BlockPos(xPos, y, zPos)).block
                if (!NON_SURFACE_BLOCKS.contains(currentBlock)) {
                    yPos = y
                    break
                }
            }

            // If yPos is 0 we could not find a spot
            if (yPos == 0) {
                continue
            }

            // Make sure the position is loaded
            if (!world.isBlockLoaded(BlockPos(xPos, yPos, zPos))) {
                continue
            }

            // Spawn a frog
            val frog = EntityEnchantedFrog(world)
            // Go y + 1.5 to ensure the frog doesn't spawn in the floor
            frog.setPosition(xPos + 0.5, yPos + 1.5, zPos + 0.5)
            world.spawnEntity(frog)
        }
    }

    companion object {
        // The ticks between updates (60 seconds)
        private const val TICKS_INBETWEEN_CHECKS = 1200

        // The max number of frogs allowed
        private const val MAX_NUMBER_OF_FROGS = 70

        // The max number of frogs to spawn at once
        private const val MAX_NUMBER_OF_FROGS_TO_SPAWN_AT_ONCE = 10

        // Non surface blocks that frogs can't spawn on
        private val NON_SURFACE_BLOCKS = setOf(
            Blocks.AIR,
            ModBlocks.MANGROVE_LEAVES,
            ModBlocks.SACRED_MANGROVE_LEAVES,
            ModBlocks.MANGROVE,
            ModBlocks.SACRED_MANGROVE
        )
    }
}