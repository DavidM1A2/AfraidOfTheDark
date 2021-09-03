package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.spell.Spell

class SpellPacket(
    internal val spell: Spell,
    internal val keybind: String?
)