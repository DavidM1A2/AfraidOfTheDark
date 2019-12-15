package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.util.ResourceLocation
import java.io.InputStream

/**
 * Class that helps load resources from server side as well as client side
 */
object ResourceUtil
{
    /**
     * Returns a resource location as an input stream, this is much better than Minecraft.getMinecraft().getResourceManager() because that is only client side
     *
     * @param resourceLocation The resource location to read
     * @return The input stream to read from
     */
    fun getInputStream(resourceLocation: ResourceLocation): InputStream
    {
        return ResourceUtil::class.java.getResourceAsStream("/assets/${resourceLocation.resourceDomain}/${resourceLocation.resourcePath}")
    }
}