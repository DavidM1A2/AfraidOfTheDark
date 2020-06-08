package com.davidm1a2.afraidofthedark.common.item.telescope

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

/**
 * Mastercrafted telescope item used to track meteors. Has an accuracy of 50 blocks
 */
class ItemMastercraftedTelescope : ItemTelescopeBase(5, "mastercrafted_telescope") {
    /**
     * Called when the item is being held
     *
     * @param stack The itemstack
     * @param worldIn The world the item is in
     * @param entityIn The entity holding the telescope
     * @param itemSlot The slot the item is in
     * @param isSelected True if the item is hovered, false otherwise
     */
    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        // Every 1 second check if this item is in a player's inventory, if so unlock the research
        if (!worldIn.isRemote && entityIn.ticksExisted % 20 == 0) {
            if (entityIn is EntityPlayer) {
                val research = entityIn.getResearch()
                if (research.canResearch(ModResearches.ELVOVRAS)) {
                    research.setResearch(ModResearches.ELVOVRAS, true)
                    research.sync(entityIn, true)
                }
            }
        }
    }

    /**
     * Gets the required research to use this item
     *
     * @return A research
     */
    override fun getRequiredResearch(): Research {
        return ModResearches.ELVOVRAS
    }
}