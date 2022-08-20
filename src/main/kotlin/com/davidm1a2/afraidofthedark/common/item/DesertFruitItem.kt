package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellInnateData
import com.davidm1a2.afraidofthedark.common.capabilities.hasStartedAOTD
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.item.core.AOTDFoodItem
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge

/**
 * Class representing desert fruit
 *
 * @constructor initializes the item name
 */
class DesertFruitItem : AOTDFoodItem("desert_fruit", 6, 2.0f, Properties()) {
    override fun finishUsingItem(itemStack: ItemStack, world: World, livingEntity: LivingEntity): ItemStack {
        // Upon eating the fruit we get max innate vitae! Don't sync server->client since this function gets called on both sides
        if (livingEntity is PlayerEntity) {
            if (livingEntity.hasStartedAOTD()) {
                val innateData = livingEntity.getSpellInnateData()
                innateData.vitae = innateData.getMaxVitae(livingEntity)
                MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(livingEntity, ModResearches.PRICKLY_SWEETS))
            }
        }
        return super.finishUsingItem(itemStack, world, livingEntity)
    }
}