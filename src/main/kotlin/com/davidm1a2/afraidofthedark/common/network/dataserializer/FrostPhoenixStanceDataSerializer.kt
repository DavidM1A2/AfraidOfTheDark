package com.davidm1a2.afraidofthedark.common.network.dataserializer

import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.FrostPhoenixStance
import net.minecraft.network.PacketBuffer
import net.minecraft.network.datasync.IDataSerializer
import net.minecraft.network.syncher.EntityDataSerializer

class FrostPhoenixStanceDataSerializer : EntityDataSerializer<FrostPhoenixStance> {
    override fun write(packetBuffer: PacketBuffer, stance: FrostPhoenixStance) {
        packetBuffer.writeEnum(stance)
    }

    override fun read(packetBuffer: PacketBuffer): FrostPhoenixStance {
        return packetBuffer.readEnum(FrostPhoenixStance::class.java)
    }

    override fun copy(stance: FrostPhoenixStance): FrostPhoenixStance {
        return stance
    }
}