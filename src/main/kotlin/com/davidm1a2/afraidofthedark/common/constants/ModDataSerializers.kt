package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.network.dataserializer.AABBDataSerializer
import com.davidm1a2.afraidofthedark.common.network.dataserializer.SpellDataSerializer

object ModDataSerializers {
    val SPELL = SpellDataSerializer()
    val AABB = AABBDataSerializer()

    val LIST = listOf(SPELL, AABB)
}