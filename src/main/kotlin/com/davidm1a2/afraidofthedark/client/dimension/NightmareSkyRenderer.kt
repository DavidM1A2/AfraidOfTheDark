package com.davidm1a2.afraidofthedark.client.dimension

import net.minecraft.client.Minecraft
import net.minecraft.client.world.ClientWorld
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
     * @param ticks        The number of ticks that have happened
     * @param partialTicks The number of partial ticks since the last tick
     * @param world        The world to render in
     * @param mc           The minecraft instance
     */
    @OnlyIn(Dist.CLIENT)
    override fun render(ticks: Int, partialTicks: Float, world: ClientWorld, mc: Minecraft) {
        // Empty for now, might have a skybox later
    }
}