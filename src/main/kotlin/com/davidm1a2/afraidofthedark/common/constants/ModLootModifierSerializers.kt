package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.loot.lootmodifier.AutosmeltLootModifierSerializer

object ModLootModifierSerializers {
    val AUTOSMELT = AutosmeltLootModifierSerializer()

    val LIST = arrayOf(
        AUTOSMELT
    )
}