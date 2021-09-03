package com.davidm1a2.afraidofthedark.common.item.telescope

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge

/**
 * Mastercrafted telescope item used to track meteors. Has an accuracy of 50 blocks
 */
class MastercraftedTelescopeItem : TelescopeBaseItem(5, "mastercrafted_telescope") {
    /**
     * Called when the item is being held
     *
     * @param stack The itemstack
     * @param worldIn The world the item is in
     * @param entityIn The entity holding the telescope
     * @param itemSlot The slot the item is in
     * @param isSelected True if the item is hovered, false otherwise
     */
    override fun inventoryTick(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        // Every 1 second check if this item is in a player's inventory, if so unlock the research
        if (!worldIn.isClientSide && entityIn.tickCount % 20 == 0) {
            if (entityIn is PlayerEntity) {
                MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(entityIn, ModResearches.ELVOVRAS))
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