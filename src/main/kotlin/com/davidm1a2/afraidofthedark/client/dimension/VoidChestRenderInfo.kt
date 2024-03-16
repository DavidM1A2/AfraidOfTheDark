package com.davidm1a2.afraidofthedark.client.dimension

import net.minecraft.client.renderer.DimensionSpecialEffects
import net.minecraft.world.phys.Vec3
import net.minecraftforge.client.ISkyRenderHandler

class VoidChestRenderInfo : DimensionSpecialEffects(255f, false, SkyType.NORMAL, false, false) {
    private val skyRenderer = VoidChestSkyRenderer()

    override fun getBrightnessDependentFogColor(biomeFogColor: Vec3, daylight: Float): Vec3 {
        return biomeFogColor
    }

    override fun isFoggyAt(x: Int, z: Int): Boolean {
        return false
    }

    override fun getSunriseColor(timeOfDay: Float, partialTicks: Float): FloatArray {
        return floatArrayOf(0f, 0f, 0f, 0f)
    }

    override fun getSkyRenderHandler(): ISkyRenderHandler {
        return skyRenderer
    }
}