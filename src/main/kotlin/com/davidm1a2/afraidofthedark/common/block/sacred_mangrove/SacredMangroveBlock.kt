package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDLogBlock
import com.davidm1a2.afraidofthedark.common.block.core.IUseCustomBlockItem
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.ResearchUnlockingBlockItem
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor

/**
 * Class representing a sacred mangrove log block
 *
 * @constructor passes on the name
 */
class SacredMangroveBlock : AOTDLogBlock("sacred_mangrove", Properties.of(Material.WOOD, MaterialColor.WOOD)), IUseCustomBlockItem {
    override fun getBlockItem(): BlockItem {
        return ResearchUnlockingBlockItem(
            ModResearches.SACRED_MANGROVE, this, Item.Properties()
                .tab(Constants.AOTD_CREATIVE_TAB)
        )
    }
}
