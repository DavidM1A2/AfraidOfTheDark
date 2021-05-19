package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.client.gui.layout.AbsoluteDimensions
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import javax.imageio.ImageIO

object TextureDimensionsCache {
    val cache: LoadingCache<ResourceLocation, AbsoluteDimensions> = CacheBuilder.newBuilder().build(CacheLoader.from<ResourceLocation, AbsoluteDimensions> {
        val image = ImageIO.read(Minecraft.getInstance().resourceManager.getResource(it).inputStream)
        AbsoluteDimensions(image.width.toDouble(), image.height.toDouble())
    })
}