package com.davidm1a2.afraidofthedark.common.registry

import com.davidm1a2.afraidofthedark.common.utility.ResourceUtil
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import net.minecraft.resources.ResourceLocation
import java.io.InputStreamReader

object JsonCodecLoader {
    private val GSON = GsonBuilder().disableHtmlEscaping().create()

    fun <T> load(resource: ResourceLocation, codec: Codec<T>): T {
        val element = InputStreamReader(ResourceUtil.readServerResource(resource).buffered()).use {
            GSON.fromJson(it, JsonElement::class.java)
        }
        return codec.decode(JsonOps.INSTANCE, element)
            .get()
            .orThrow()
            .first
    }
}