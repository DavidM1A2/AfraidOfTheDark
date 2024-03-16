package com.davidm1a2.afraidofthedark.client.dimension

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraftforge.client.ISkyRenderHandler

/**
 * Class that renders the nightmare 'sky' texture
 */
class NightmareSkyRenderer : ISkyRenderHandler {
    override fun render(ticks: Int, partialTicks: Float, matrixStack: PoseStack?, world: ClientLevel?, mc: Minecraft?) {
        // Empty for now, might have a skybox later
    }
}