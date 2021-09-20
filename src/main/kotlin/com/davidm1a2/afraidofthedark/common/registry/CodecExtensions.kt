package com.davidm1a2.afraidofthedark.common.registry

import com.mojang.serialization.Codec
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry
import java.util.*

/**
 * Creates a codec which only deserializes its contents when needed
 */
fun <C> Codec<C>.lazy(): Codec<Lazy<C>> {
    return LazyCodec(this)
}

/**
 * Creates a resource location codec that points to a forge registry
 */
fun <V : IForgeRegistryEntry<V>> IForgeRegistry<V>.codec(): Codec<V> {
    return ResourceLocation.CODEC.xmap(
        { this.getValue(it) ?: throw IllegalStateException("Could not find item $it in registry ${this.registryName}") },
        { it.registryName!! }
    )
}

/**
 * Utility function to make an Optional.empty() or Optional(Lazy(T)). This is used because
 * forge uses a lot of optionals, and we want to wrap T in a lazy
 */
fun <T> T?.toLazyOptional(): Optional<Lazy<T>> {
    return Optional.ofNullable(this?.let { lazy { it } })
}

/**
 * Utility function to turn an Optional<Lazy<T>> into a Lazy<T?>. It's needed so we can go from
 * java optionals to kotlin nullable types easily.
 */
fun <T> Optional<Lazy<T>>.getOrNull(): Lazy<T?> {
    return if (this.isPresent) {
        get()
    } else {
        lazy { null }
    }
}