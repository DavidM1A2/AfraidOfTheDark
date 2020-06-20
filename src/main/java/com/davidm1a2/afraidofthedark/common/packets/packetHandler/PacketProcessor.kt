package com.davidm1a2.afraidofthedark.common.packets.packetHandler

import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkEvent

interface PacketProcessor<T> {
    fun encode(msg: T, buf: PacketBuffer)

    fun decode(buf: PacketBuffer): T

    fun process(msg: T, ctx: NetworkEvent.Context)

    fun processAsync(): Boolean {
        return true
    }
}