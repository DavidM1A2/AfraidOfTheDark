package com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource

class PowerSourceUpdatePacket(
    internal val powerSourceValues: Map<SpellPowerSource, Double>,
    internal val powerSourceUnlocked: Map<SpellPowerSource, Boolean>
)