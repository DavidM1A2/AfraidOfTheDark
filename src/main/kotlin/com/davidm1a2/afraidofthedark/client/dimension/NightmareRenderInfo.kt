package com.davidm1a2.afraidofthedark.client.dimension

import net.minecraft.client.world.DimensionRenderInfo
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.client.ISkyRenderHandler

class NightmareRenderInfo : DimensionRenderInfo(255f, false, FogType.NORMAL, false, false) {
    private val skyRenderer = NightmareSkyRenderer()

    override fun getBrightnessDependentFogColor(biomeFogColor: Vector3d, daylight: Float): Vector3d {
        return biomeFogColor
    }

    override fun isFoggyAt(x: Int, z: Int): Boolean {
        return true
    }

    override fun getSunriseColor(timeOfDay: Float, partialTicks: Float): FloatArray {
        return floatArrayOf(0f, 0f, 0f, 0f)
    }

    override fun getSkyRenderHandler(): ISkyRenderHandler {
        return skyRenderer
    }
}