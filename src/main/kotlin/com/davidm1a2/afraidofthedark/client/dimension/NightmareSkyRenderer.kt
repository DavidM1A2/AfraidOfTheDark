package com.davidm1a2.afraidofthedark.client.dimension

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.world.ClientWorld
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.ISkyRenderHandler

/**
 * Class that renders the nightmare 'sky' texture
 */
class NightmareSkyRenderer : ISkyRenderHandler {
    @OnlyIn(Dist.CLIENT)
    override fun render(ticks: Int, partialTicks: Float, matrixStack: MatrixStack, world: ClientWorld, mc: Minecraft) {
        // Empty for now, might have a skybox later
    }
}