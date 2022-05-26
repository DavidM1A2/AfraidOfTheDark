package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource

class UpdateSelectedPowerSourcePacket(
    internal val selectedPowerSource: SpellPowerSource<*>
)