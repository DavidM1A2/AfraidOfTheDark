package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.spell.Spell
import java.util.*

class SpellPacket(
    internal val spell: Spell,
    internal val networkId: UUID,
    internal val keybind: String?
)