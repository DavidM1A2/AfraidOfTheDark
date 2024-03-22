package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.bolt.BoltEntity
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.ArrowItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

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
    override fun createArrow(world: Level, itemStack: ItemStack, shooter: LivingEntity): AbstractArrow {
        val bolt = createBolt(world)
        val hasResearch = requiredResearch?.let { (shooter as? Player)?.getResearch()?.isResearched(it) ?: false } ?: true
        bolt.initUsingShooter(shooter, hasResearch)
        return bolt
    }

    abstract fun createBolt(world: Level): BoltEntity

    override fun appendHoverText(itemStack: ItemStack, world: Level?, tooltips: MutableList<Component>, flag: TooltipFlag) {
        tooltips.add(TranslatableComponent("tooltip.afraidofthedark.bolt.requires_crossbow"))
    }
}