package com.davidm1a2.afraidofthedark.common.item.void

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredHoeItem
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraftforge.common.Tags
import kotlin.random.Random

class VoidHoeItem : AOTDResearchRequiredHoeItem("void_hoe", ModToolMaterials.VOID, -35, 0.0f, ModResearches.AN_UNSETTLING_MATERIAL, Properties()) {
    override fun inventoryTick(itemStack: ItemStack, world: Level, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        VoidCommons.processItem(itemStack, world)
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val result = super.useOn(context)
        if (result.consumesAction()) {
            val level = context.level
            if (!level.isClientSide) {
                val seedItem = Tags.Items.SEEDS.values.randomOrNull() ?: Items.WHEAT_SEEDS
                // 32-64 seeds for free
                val seedItemStack = ItemStack(seedItem, Random.nextInt(32) + 32)
                level.addFreshEntity(
                    ItemEntity(
                        level,
                        context.clickLocation.x,
                        context.clickLocation.y,
                        context.clickLocation.z,
                        seedItemStack
                    )
                )
            }
        }
        return result
    }


    override fun appendHoverText(itemStack: ItemStack, world: Level?, tooltip: MutableList<Component>, iTooltipFlag: TooltipFlag) {
        super.appendHoverText(itemStack, world, tooltip, iTooltipFlag)

        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(requiredResearch)) {
            tooltip.add(TranslatableComponent("tooltip.afraidofthedark.void_tool.drop_seeds"))
            tooltip.add(TranslatableComponent("tooltip.afraidofthedark.void_tool.autorepair"))
        }
    }
}