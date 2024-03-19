package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.AbstractArrowEntity
import net.minecraft.item.ArrowItem
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraft.world.item.ArrowItem

abstract class AOTDBoltItem(
    name: String,
    properties: Properties,
    val requiredResearch: Research? = null,
    displayInCreative: Boolean = true
) : ArrowItem(properties.apply {
    if (displayInCreative) {
        tab(Constants.AOTD_CREATIVE_TAB)
    }
}) {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }

    /**
     * Called by vanilla to create an arrow entity from a bolt item. Based on research and item set parameters
     */
    override fun createArrow(world: World, itemStack: ItemStack, shooter: LivingEntity): AbstractArrowEntity {
        val bolt = createBolt(world)
        val hasResearch = requiredResearch?.let { (shooter as? PlayerEntity)?.getResearch()?.isResearched(it) ?: false } ?: true
        bolt.initUsingShooter(shooter, hasResearch)
        return bolt
    }

    abstract fun createBolt(world: World): BoltEntity

    override fun appendHoverText(itemStack: ItemStack, world: World?, tooltips: MutableList<ITextComponent>, flag: ITooltipFlag) {
        tooltips.add(TranslationTextComponent("tooltip.afraidofthedark.bolt.requires_crossbow"))
    }
}