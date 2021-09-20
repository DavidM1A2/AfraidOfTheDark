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
     * Gets the required research to use this item
     *
     * @return A research
     */
    override fun getRequiredResearch(): Research {
        return ModResearches.OPTICS
    }
}