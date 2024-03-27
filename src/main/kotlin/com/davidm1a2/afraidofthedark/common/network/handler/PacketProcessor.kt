package com.davidm1a2.afraidofthedark.common.network.handler

import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.fmllegacy.network.NetworkEvent

interface PacketProcessor<T> {
    fun encode(msg: T, buf: FriendlyByteBuf)

    fun decode(buf: FriendlyByteBuf): T

    fun process(msg: T, ctx: NetworkEvent.Context)

    fun processAsync(): Boolean {
        return true
    }
}