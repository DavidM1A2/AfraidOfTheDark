package com.davidm1a2.afraidofthedark.common.network.packets.capability

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasicsCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import net.minecraft.nbt.CompoundTag

class AOTDPlayerBasicsPacket(internal val data: CompoundTag) {
    constructor(playerBasics: IAOTDPlayerBasics) : this(AOTDPlayerBasicsCapabilitySerializer(playerBasics).serializeNBT())
}