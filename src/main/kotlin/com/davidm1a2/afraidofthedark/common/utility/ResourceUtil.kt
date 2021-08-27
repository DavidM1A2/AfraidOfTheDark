package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.util.ResourceLocation
import java.io.InputStream

/**
 * Class that helps load resources from server side as well as client side
 */
object ResourceUtil {
    fun readClientResource(resourceLocation: ResourceLocation): InputStream {
        return ResourceUtil::class.java.getResourceAsStream("/assets/${resourceLocation.namespace}/${resourceLocation.path}")
            ?: throw IllegalArgumentException("Resource location $resourceLocation doesn't exist")
    }

    fun readServerResource(resourceLocation: ResourceLocation): InputStream {
        return ResourceUtil::class.java.getResourceAsStream("/data/${resourceLocation.namespace}/${resourceLocation.path}")
            ?: throw IllegalArgumentException("Resource location $resourceLocation doesn't exist")
    }
}