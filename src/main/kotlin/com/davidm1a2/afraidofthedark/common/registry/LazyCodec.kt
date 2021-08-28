package com.davidm1a2.afraidofthedark.common.registry

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import org.apache.logging.log4j.LogManager

fun <C> Codec<C>.lazy(): Codec<Lazy<C>> {
    return LazyCodec(this)
}

private class LazyCodec<C>(private val baseCodec: Codec<C>) : Codec<Lazy<C>> {
    override fun <T> encode(input: Lazy<C>, ops: DynamicOps<T>, prefix: T): DataResult<T> {
        return baseCodec.encode(input.value, ops, prefix)
    }

    override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Lazy<C>, T>> {
        return DataResult.success(
            Pair.of(
                lazy {
                    baseCodec.parse(ops, input).getOrThrow(false) {
                        LOG.error("Failed to lazily parse a codec: $it")
                        LOG.error("Input was:\n$input")
                    }
                },
                input
            )
        )
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}