package com.davidm1a2.afraidofthedark.client.entity.splinterDrone

import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3f

/**
 * Renderer class for the splinter drone entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class SplinterDroneRenderer(renderManager: EntityRendererManager) :
    MobRenderer<SplinterDroneEntity, SplinterDroneModel>(renderManager, SPLINTER_DRONE_MODEL, MODEL_SHADOW_SIZE) {
    override fun setupRotations(splinterDrone: SplinterDroneEntity, matrixStack: MatrixStack, bob: Float, yOffset: Float, partialTicks: Float) {
        super.setupRotations(splinterDrone, matrixStack, bob, yOffset, partialTicks)
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180f))
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180f))
    }

    override fun getRenderOffset(splinterDrone: SplinterDroneEntity, partialTicks: Float): Vector3d {
        return Vector3d(0.0, MODEL_HEIGHT, 0.0)
    }

    override fun render(
        splinterDrone: SplinterDroneEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        splinterDrone.getAnimationHandler().update()

        super.render(splinterDrone, entityYaw, partialTicks, matrixStack, renderTypeBuffer, packedLight)
    }

    override fun getTextureLocation(entity: SplinterDroneEntity): ResourceLocation {
        return SPLINTER_DRONE_TEXTURE
    }

    companion object {
        // The texture used by the model
        private val SPLINTER_DRONE_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/splinter_drone.png")

        // The splinter drone model
        private val SPLINTER_DRONE_MODEL = SplinterDroneModel()

        // The height of the splinter drone model
        private const val MODEL_HEIGHT = 3.1

        // The size of the shadow of the model
        private const val MODEL_SHADOW_SIZE = 0.3f
    }
}
