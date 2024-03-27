package com.davidm1a2.afraidofthedark.common.network.dataserializer

import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.FrostPhoenixStance
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.syncher.EntityDataSerializer

class FrostPhoenixStanceDataSerializer : EntityDataSerializer<FrostPhoenixStance> {
    override fun write(packetBuffer: FriendlyByteBuf, stance: FrostPhoenixStance) {
        packetBuffer.writeEnum(stance)
    }

    override fun read(packetBuffer: FriendlyByteBuf): FrostPhoenixStance {
        return packetBuffer.readEnum(FrostPhoenixStance::class.java)
    }

    override fun copy(stance: FrostPhoenixStance): FrostPhoenixStance {
        return stance
    }
}