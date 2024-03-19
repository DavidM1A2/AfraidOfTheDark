package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.CompoundTag

class AOTDPlayerBasicsPacket(internal val data: CompoundTag) {
    constructor(playerBasics: IAOTDPlayerBasics) : this(
        ModCapabilities.PLAYER_BASICS.storage.writeNBT(
            ModCapabilities.PLAYER_BASICS,
            playerBasics,
            null
        ) as CompoundNBT
    )
}