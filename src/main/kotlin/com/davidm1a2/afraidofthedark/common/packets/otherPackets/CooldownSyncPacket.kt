package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemWithPerItemCooldown

class CooldownSyncPacket(
    internal val timeServer: Long,
    internal val itemToSync: AOTDItemWithPerItemCooldown
)