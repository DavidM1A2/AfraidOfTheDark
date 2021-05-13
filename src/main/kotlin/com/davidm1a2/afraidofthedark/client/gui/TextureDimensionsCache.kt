package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.client.gui.base.Dimensions
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import javax.imageio.ImageIO

object TextureDimensionsCache {
    val cache: LoadingCache<ResourceLocation, Dimensions<Int>> = CacheBuilder.newBuilder().build(CacheLoader.from<ResourceLocation, Dimensions<Int>> {
        val image = ImageIO.read(Minecraft.getInstance().resourceManager.getResource(it).inputStream)
        Dimensions(image.width, image.height)
    })
}