package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.client.entity.LateEntityRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.culling.ClippingHelper
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class ClientLateRenderHandler {
    @SubscribeEvent
    fun renderWorldLastEvent(event: RenderWorldLastEvent) {
        val matrixStack = event.matrixStack
        val partialTicks = event.partialTicks
        val projectionMatrix = event.projectionMatrix

        val mc = Minecraft.getInstance()
        val rendererManager = mc.entityRenderDispatcher
        val renderTypeBuffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().builder)

        val cameraPos = mc.gameRenderer.mainCamera.position
        val clippingHandler = ClippingHelper(matrixStack.last().pose(), projectionMatrix).apply {
            prepare(cameraPos.x, cameraPos.y, cameraPos.z)
        }

        matrixStack.pushPose()
        matrixStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z)
        matrixStack.scale(1f, 1f, 1f)
        mc.level?.entitiesForRendering()?.forEach {
            val renderer = rendererManager.getRenderer(it)
            if (renderer is LateEntityRenderer) {
                if (renderer.shouldRender(it, clippingHandler, cameraPos.x, cameraPos.y, cameraPos.z)) {
                    matrixStack.pushPose()
                    val renderPosition = it.getPosition(partialTicks)
                    matrixStack.translate(renderPosition.x, renderPosition.y, renderPosition.z)
                    val packedLight = rendererManager.getPackedLightCoords(it, partialTicks)
                    renderer.render(it, it.xRot, partialTicks, matrixStack, renderTypeBuffer, packedLight)
                    matrixStack.popPose()
                }
            }
        }
        matrixStack.popPose()

        renderTypeBuffer.endBatch()
    }
}
