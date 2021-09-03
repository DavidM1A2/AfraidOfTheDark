package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModEffects
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.PotionUtils
import net.minecraft.potion.Potions
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.common.MinecraftForge
import kotlin.random.Random

/**
 * Tile entity for the dark forest block that makes players drowsy
 *
 * @constructor sets the block type of the tile entity
 */
class DarkForestTileEntity : AOTDTickingTileEntity(ModTileEntities.DARK_FOREST) {
    /**
     * Update gets called every tick
     */
    override fun tick() {
        super.tick()
        // Server side processing only
        if (level?.isClientSide == false) {
            updateNearbyPlayers()
            spawnCultistTome()
        }
    }

    private fun updateNearbyPlayers() {
        // If we've existed for a multiple of 60 ticks perform a check for nearby players
        if (ticksExisted % TICKS_INBETWEEN_PLAYER_CHECKS == 0L) {
            // Grab all nearby players
            for (entityPlayer in level!!.getEntitiesOfClass(
                PlayerEntity::class.java,
                AxisAlignedBB(
                    blockPos.x.toDouble(),
                    blockPos.y.toDouble(),
                    blockPos.z.toDouble(),
                    (blockPos.x + 1).toDouble(),
                    (blockPos.y + 1).toDouble(),
                    (blockPos.z + 1).toDouble()
                ).inflate(CHECK_RANGE.toDouble())
            )) {
                // Grab their research
                val playerResearch = entityPlayer.getResearch()

                // If the player can research dark forest unlock it and sync that research
                MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(entityPlayer, ModResearches.DARK_FOREST))

                // If the player has dark forest research unlocked add the sleeping potion effect and exchange water
                // bottles with sleeping potion bottles.
                if (playerResearch.isResearched(ModResearches.DARK_FOREST)) {
                    // 6 seconds of sleeping potion effect
                    entityPlayer.addEffect(EffectInstance(ModEffects.SLEEPING, 120, 0, true, false))
                    // Replace all water bottles with sleeping potions
                    for (i in entityPlayer.inventory.items.indices) {
                        // Grab the stack in the current slot
                        val itemStack = entityPlayer.inventory.getItem(i)

                        // If it's a potion with type water unlock the sleeping potion research and replace water bottles with sleeping potions
                        if (PotionUtils.getPotion(itemStack) == Potions.WATER) {
                            MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(entityPlayer, ModResearches.SLEEPING_POTION))
                            entityPlayer.inventory.setItem(i, ItemStack(ModItems.SLEEPING_POTION, itemStack.count))
                        }
                    }
                }
            }
        }
    }

    private fun spawnCultistTome() {
        if (ticksExisted % TICKS_INBETWEEN_CULTIST_TOME_CHECKS == 0L) {
            // Spawn a cultist tome
            val tomeX = blockPos.x + Random.nextDouble(-TOME_SPAWN_RANGE, TOME_SPAWN_RANGE)
            val tomeY = blockPos.y - 7.0
            val tomeZ = blockPos.z + Random.nextDouble(-TOME_SPAWN_RANGE, TOME_SPAWN_RANGE)

            val tome = ItemEntity(level!!, tomeX, tomeY, tomeZ, ItemStack(ModItems.CULTIST_TOME))
            level!!.addFreshEntity(tome)
        }
    }

    companion object {
        // The ticks inbetween checks for nearby players
        private const val TICKS_INBETWEEN_PLAYER_CHECKS = 60

        // The ticks inbetween cultist tome checks (5 min)
        private const val TICKS_INBETWEEN_CULTIST_TOME_CHECKS = 20 * 5 * 60

        // The range that players get drowsy in blocks
        private const val CHECK_RANGE = 14

        // The range from the dark forest block we can spawn cultist tomes at
        private const val TOME_SPAWN_RANGE = 7.5
    }
}