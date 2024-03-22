package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModEffects
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDZoneTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.potion.EffectInstance
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.world.level.block.state.BlockState
import kotlin.random.Random

/**
 * Tile entity for the dark forest block that makes players drowsy
 *
 * @constructor sets the block type of the tile entity
 */
class DarkForestTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDZoneTileEntity(ModTileEntities.DARK_FOREST) {
    override val zone: AxisAlignedBB by lazy {
        AxisAlignedBB(
            blockPos.x.toDouble(),
            blockPos.y.toDouble(),
            blockPos.z.toDouble(),
            (blockPos.x + 1).toDouble(),
            (blockPos.y + 1).toDouble(),
            (blockPos.z + 1).toDouble()
        ).inflate(CHECK_RANGE.toDouble())
    }

    override fun playerInZone(player: Player) {
        // 6 seconds of sleeping potion effect
        player.addEffect(EffectInstance(ModEffects.SLEEPING, 120, 0, true, false))
    }

    override fun tick() {
        super.tick()
        // Server side processing only
        if (level?.isClientSide == false) {
            spawnCultistTome()
        }
    }

    private fun spawnCultistTome() {
        if (ticksExisted % TICKS_BETWEEN_CULTIST_TOME_CHECKS == 0L) {
            // Spawn a cultist tome
            val tomeX = blockPos.x + Random.nextDouble(-TOME_SPAWN_RANGE, TOME_SPAWN_RANGE)
            val tomeY = blockPos.y - 7.0
            val tomeZ = blockPos.z + Random.nextDouble(-TOME_SPAWN_RANGE, TOME_SPAWN_RANGE)

            val tome = ItemEntity(level!!, tomeX, tomeY, tomeZ, ItemStack(ModItems.CULTIST_TOME))
            level!!.addFreshEntity(tome)
        }
    }

    companion object {
        // The ticks in between cultist tome checks (5 min)
        private const val TICKS_BETWEEN_CULTIST_TOME_CHECKS = 20 * 5 * 60

        // The range that players get drowsy in blocks
        private const val CHECK_RANGE = 14

        // The range from the dark forest block we can spawn cultist tomes at
        private const val TOME_SPAWN_RANGE = 7.5
    }
}