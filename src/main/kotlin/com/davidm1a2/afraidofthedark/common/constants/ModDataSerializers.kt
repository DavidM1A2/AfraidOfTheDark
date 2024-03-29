package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.network.dataserializer.AABBDataSerializer
import com.davidm1a2.afraidofthedark.common.network.dataserializer.ColorDataSerializer
import com.davidm1a2.afraidofthedark.common.network.dataserializer.FrostPhoenixStanceDataSerializer
import com.davidm1a2.afraidofthedark.common.network.dataserializer.SpellDataSerializer
import com.davidm1a2.afraidofthedark.common.network.dataserializer.Vector3dDataSerializer

object ModDataSerializers {
    val SPELL = SpellDataSerializer()
    val AABB = AABBDataSerializer()
    val VECTOR3D = Vector3dDataSerializer()
    val COLOR = ColorDataSerializer()
    val FROST_PHOENIX_STANCE = FrostPhoenixStanceDataSerializer()

    val LIST = listOf(SPELL, AABB, VECTOR3D, COLOR, FROST_PHOENIX_STANCE)
}