package com.davidm1a2.afraidofthedark.client.dimension

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.IRenderHandler

/**
 * Class that renders the nightmare 'sky' texture
 */
class NightmareSkyRenderer : IRenderHandler {
    /**
     * Called to render the sky
     *
     * @param partialTicks The number of partial ticks since the last tick
     * @param world        The world to render in
     * @param mc           The minecraft instance
     */
    @OnlyIn(Dist.CLIENT)
    override fun render(partialTicks: Float, world: WorldClient, mc: Minecraft) {
        // Empty for now, might have a skybox later
    }
}