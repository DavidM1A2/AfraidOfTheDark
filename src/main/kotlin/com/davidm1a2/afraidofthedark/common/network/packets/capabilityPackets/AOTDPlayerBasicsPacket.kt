package com.davidm1a2.afraidofthedark.common.network.packets.capabilityPackets

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.nbt.CompoundNBT

class AOTDPlayerBasicsPacket(internal val data: CompoundNBT) {
    constructor(playerBasics: IAOTDPlayerBasics) : this(
        ModCapabilities.PLAYER_BASICS.storage.writeNBT(
            ModCapabilities.PLAYER_BASICS,
            playerBasics,
            null
        ) as CompoundNBT
    )
}