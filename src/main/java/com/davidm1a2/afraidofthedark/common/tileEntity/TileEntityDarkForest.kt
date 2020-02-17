package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModPotions
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.PotionTypes
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionUtils
import net.minecraft.util.math.AxisAlignedBB

/**
 * Tile entity for the dark forest block that makes players drowsy
 *
 * @constructor sets the block type of the tile entity
 */
class TileEntityDarkForest : AOTDTickingTileEntity(ModBlocks.DARK_FOREST)
{
    /**
     * Update gets called every tick
     */
    override fun update()
    {
        super.update()
        // Server side processing only
        if (!world.isRemote)
        {
            // If we've existed for a multiple of 60 ticks perform a check for nearby players
            if (ticksExisted % TICKS_INBETWEEN_CHECKS == 0L)
            {
                // Grab all nearby players
                for (entityPlayer in world.getEntitiesWithinAABB(
                        EntityPlayer::class.java,
                        AxisAlignedBB(
                                pos.x.toDouble(),
                                pos.y.toDouble(),
                                pos.z.toDouble(),
                                (pos.x + 1).toDouble(),
                                (pos.y + 1).toDouble(),
                                (pos.z + 1).toDouble()
                        ).grow(CHECK_RANGE.toDouble())
                ))
                {
                    // Grab their research
                    val playerResearch = entityPlayer.getResearch()

                    // If the player can research dark forest unlock it and sync that research
                    if (playerResearch.canResearch(ModResearches.DARK_FOREST))
                    {
                        playerResearch.setResearch(ModResearches.DARK_FOREST, true)
                        playerResearch.sync(entityPlayer, true)
                    }

                    // If the player has dark forest research unlocked add the sleeping potion effect and exchange water
                    // bottles with sleeping potion bottles.
                    if (playerResearch.isResearched(ModResearches.DARK_FOREST))
                    {
                        // 6 seconds of sleeping potion effect
                        entityPlayer.addPotionEffect(PotionEffect(ModPotions.SLEEPING_POTION, 120, 0, true, false))
                        // Replace all water bottles with sleeping potions
                        for (i in entityPlayer.inventory.mainInventory.indices)
                        {
                            // Grab the stack in the current slot
                            val itemStack = entityPlayer.inventory.getStackInSlot(i)

                            // If it's a potion with type water unlock the sleeping potion research and replace water bottles with sleeping potions
                            if (PotionUtils.getPotionFromItem(itemStack) === PotionTypes.WATER)
                            {
                                if (playerResearch.canResearch(ModResearches.SLEEPING_POTION))
                                {
                                    playerResearch.setResearch(ModResearches.SLEEPING_POTION, true)
                                    playerResearch.sync(entityPlayer, true)
                                }
                                entityPlayer.inventory.setInventorySlotContents(i, ItemStack(ModItems.SLEEPING_POTION, itemStack.count))
                            }
                        }
                    }
                }
            }
        }
    }

    companion object
    {
        // The ticks inbetween checks for nearby players
        private const val TICKS_INBETWEEN_CHECKS = 60
        // The range that players get drowsy in blocks
        private const val CHECK_RANGE = 14
    }
}