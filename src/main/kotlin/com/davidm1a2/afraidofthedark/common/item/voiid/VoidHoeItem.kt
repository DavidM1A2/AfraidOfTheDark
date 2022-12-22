package com.davidm1a2.afraidofthedark.common.item.voiid

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredHoeItem
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.item.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUseContext
import net.minecraft.item.Items
import net.minecraft.util.ActionResultType
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraftforge.common.Tags
import kotlin.random.Random

class VoidHoeItem : AOTDResearchRequiredHoeItem("void_hoe", ModToolMaterials.VOID, -35, 0.0f, ModResearches.AN_UNSETTLING_MATERIAL, Properties()) {
    override fun useOn(context: ItemUseContext): ActionResultType {
        val result = super.useOn(context)
        if (result.consumesAction()) {
            val level = context.level
            if (!level.isClientSide) {
                val seedItem = Tags.Items.SEEDS.values.randomOrNull() ?: Items.WHEAT_SEEDS
                // 32-64 seeds for free
                val seedItemStack = ItemStack(seedItem, Random.nextInt(32) + 32)
                level.addFreshEntity(ItemEntity(level, context.clickLocation.x, context.clickLocation.y, context.clickLocation.z, seedItemStack))
            }
        }
        return result
    }


    override fun appendHoverText(itemStack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, iTooltipFlag: ITooltipFlag) {
        super.appendHoverText(itemStack, world, tooltip, iTooltipFlag)

        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(requiredResearch)) {
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.void_tool.drop_seeds"))
        }
    }
}