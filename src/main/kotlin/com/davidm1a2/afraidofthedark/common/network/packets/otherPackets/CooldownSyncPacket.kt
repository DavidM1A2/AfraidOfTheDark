package com.davidm1a2.afraidofthedark.common.network.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.item.core.AOTDPerItemCooldownItem

class CooldownSyncPacket(
    internal val timeServer: Long,
    internal val itemToSync: AOTDPerItemCooldownItem
)