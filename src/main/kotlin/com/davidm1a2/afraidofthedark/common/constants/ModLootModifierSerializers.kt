package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.loot.lootmodifier.AutosmeltLootModifierSerializer
import com.davidm1a2.afraidofthedark.common.loot.lootmodifier.DoubleDropsLootModifierSerializer
import com.davidm1a2.afraidofthedark.common.loot.lootmodifier.NoDropsLootModifierSerializer

object ModLootModifierSerializers {
    val AUTOSMELT = AutosmeltLootModifierSerializer()
    val DOUBLE_DROPS = DoubleDropsLootModifierSerializer()
    val NO_DROPS = NoDropsLootModifierSerializer()

    val LIST = arrayOf(
        AUTOSMELT,
        DOUBLE_DROPS,
        NO_DROPS
    )
}