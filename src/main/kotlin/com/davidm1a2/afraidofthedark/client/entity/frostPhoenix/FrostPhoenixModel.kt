package com.davidm1a2.afraidofthedark.client.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.FrostPhoenixEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.RenderType


class FrostPhoenixModel internal constructor() : EntityModel<FrostPhoenixEntity>(RenderType::entityTranslucent) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val phoenixBreast: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        phoenixBreast = MCAModelRenderer(0, 42, 128f, 64f)
        phoenixBreast.addBox(-5.0f, 0.0f, -4.0f, 10f, 14f, 8f)
        phoenixBreast.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        phoenixBreast.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.034899496f, 0.99939084f))
        parts["phoenixBreast"] = phoenixBreast

        val phoenixNeck1 = MCAModelRenderer(64, 52, 128f, 64f)
        phoenixNeck1.addBox(-3.0f, 0.0f, -3.0f, 6f, 6f, 6f)
        phoenixNeck1.setInitialRotationPoint(0.0f, 13.0f, 0.0f)
        phoenixNeck1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0784591f, 0.9969173f))
        parts["phoenixNeck1"] = phoenixNeck1
        phoenixBreast.addChild(phoenixNeck1)

        val phoenixStomach = MCAModelRenderer(36, 50, 128f, 64f)
        phoenixStomach.addBox(-4.0f, -8.0f, -3.0f, 8f, 8f, 6f)
        phoenixStomach.setInitialRotationPoint(0.0f, 2.0f, 0.0f)
        phoenixStomach.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, -0.25881904f, 0.9659258f))
        parts["phoenixStomach"] = phoenixStomach
        phoenixBreast.addChild(phoenixStomach)

        val phoenixLUpperWing = MCAModelRenderer(0, 19, 128f, 64f)
        phoenixLUpperWing.addBox(-0.5f, -1.0f, -1.0f, 1f, 7f, 12f)
        phoenixLUpperWing.setInitialRotationPoint(0.0f, 11.0f, -4.0f)
        phoenixLUpperWing.setInitialRotationQuaternion(Quaternion(0.49999997F, -0.68301266F, 0.1830127F, 0.49999994F))
        parts["phoenixLUpperWing"] = phoenixLUpperWing
        phoenixBreast.addChild(phoenixLUpperWing)

        val phoenixRUpperWing = MCAModelRenderer(0, 0, 128f, 64f)
        phoenixRUpperWing.addBox(-0.5f, -1.0f, -11.0f, 1f, 7f, 12f)
        phoenixRUpperWing.setInitialRotationPoint(0.0f, 11.0f, 4.0f)
        phoenixRUpperWing.setInitialRotationQuaternion(Quaternion(-0.49999997F, 0.68301266F, 0.1830127F, 0.49999994F))
        parts["phoenixRUpperWing"] = phoenixRUpperWing
        phoenixBreast.addChild(phoenixRUpperWing)

        val phoenixNeck2 = MCAModelRenderer(108, 54, 128f, 64f)
        phoenixNeck2.addBox(-2.0f, 0.0f, -2.0f, 4f, 6f, 4f)
        phoenixNeck2.setInitialRotationPoint(0.0f, 5.0f, 0.0f)
        phoenixNeck2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f))
        parts["phoenixNeck2"] = phoenixNeck2
        phoenixNeck1.addChild(phoenixNeck2)

        val phoenixLLeg = MCAModelRenderer(30, 0, 128f, 64f)
        phoenixLLeg.addBox(-1.0f, -7.0f, -1.0f, 2f, 7f, 2f)
        phoenixLLeg.setInitialRotationPoint(-2.0f, -6.0f, -3.5f)
        phoenixLLeg.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.42261827f, 0.90630776f))
        parts["phoenixLLeg"] = phoenixLLeg
        phoenixStomach.addChild(phoenixLLeg)

        val phoenixRLeg = MCAModelRenderer(22, 0, 128f, 64f)
        phoenixRLeg.addBox(-1.0f, -7.0f, -1.0f, 2f, 7f, 2f)
        phoenixRLeg.setInitialRotationPoint(-2.0f, -6.0f, 3.5f)
        phoenixRLeg.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.42261827f, 0.90630776f))
        parts["phoenixRLeg"] = phoenixRLeg
        phoenixStomach.addChild(phoenixRLeg)

        val phoenixTail = MCAModelRenderer(105, 15, 128f, 64f)
        phoenixTail.addBox(0.0f, -0.5f, -1.5f, 8f, 1f, 3f)
        phoenixTail.setInitialRotationPoint(-3.0f, -7.5f, 0.0f)
        phoenixTail.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, -0.99904823f, 0.043619335f))
        parts["phoenixTail"] = phoenixTail
        phoenixStomach.addChild(phoenixTail)

        val phoenixLLowerWing = MCAModelRenderer(26, 19, 128f, 64f)
        phoenixLLowerWing.addBox(0.0f, 0.0f, -1.5f, 1f, 6f, 13f)
        phoenixLLowerWing.setInitialRotationPoint(0.0f, -1.0f, 7.5f)
        phoenixLLowerWing.setInitialRotationQuaternion(Quaternion(0.976296F, 0.0F, 0.0F, 0.21643965F))
        parts["phoenixLLowerWing"] = phoenixLLowerWing
        phoenixLUpperWing.addChild(phoenixLLowerWing)

        val phoenixRLowerWing = MCAModelRenderer(26, 0, 128f, 64f)
        phoenixRLowerWing.addBox(0.0f, -6.0f, -1.5f, 1f, 6f, 13f)
        phoenixRLowerWing.setInitialRotationPoint(0.0f, -1.0f, -7.5f)
        phoenixRLowerWing.setInitialRotationQuaternion(Quaternion(0.21643962F, 0.0F, 0.0F, 0.976296F))
        parts["phoenixRLowerWing"] = phoenixRLowerWing
        phoenixRUpperWing.addChild(phoenixRLowerWing)

        val phoenixNeck3 = MCAModelRenderer(14, 0, 128f, 64f)
        phoenixNeck3.addBox(-1.0f, 0.0f, -1.0f, 2f, 8f, 2f)
        phoenixNeck3.setInitialRotationPoint(0.0f, 5.0f, 0.0f)
        phoenixNeck3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, -0.06104854f, 0.9981348f))
        parts["phoenixNeck3"] = phoenixNeck3
        phoenixNeck2.addChild(phoenixNeck3)

        val phoenixLClaw = MCAModelRenderer(0, 6, 128f, 64f)
        phoenixLClaw.addBox(-2.0f, -2.0f, -1.0f, 4f, 2f, 2f)
        phoenixLClaw.setInitialRotationPoint(0.0f, -6.0f, 0.0f)
        phoenixLClaw.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, -0.21643962f, 0.976296f))
        parts["phoenixLClaw"] = phoenixLClaw
        phoenixLLeg.addChild(phoenixLClaw)

        val phoenixRClaw = MCAModelRenderer(14, 38, 128f, 64f)
        phoenixRClaw.addBox(-2.0f, -2.0f, -1.0f, 4f, 2f, 2f)
        phoenixRClaw.setInitialRotationPoint(0.0f, -6.0f, 0.0f)
        phoenixRClaw.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, -0.21643962f, 0.976296f))
        parts["phoenixRClaw"] = phoenixRClaw
        phoenixRLeg.addChild(phoenixRClaw)

        val phoenixTailFeather1 = MCAModelRenderer(96, 0, 128f, 64f)
        phoenixTailFeather1.addBox(0.0f, -0.5f, -2.0f, 12f, 1f, 4f)
        phoenixTailFeather1.setInitialRotationPoint(7.0f, 0.0f, 0.0f)
        phoenixTailFeather1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.1305262f, 0.9914449f))
        parts["phoenixTailFeather1"] = phoenixTailFeather1
        phoenixTail.addChild(phoenixTailFeather1)

        val phoenixTailFeather2 = MCAModelRenderer(100, 5, 128f, 64f)
        phoenixTailFeather2.addBox(0.0f, 0.0f, 0.0f, 10f, 1f, 4f)
        phoenixTailFeather2.setInitialRotationPoint(7.0f, 0.0f, 0.0f)
        phoenixTailFeather2.setInitialRotationQuaternion(Quaternion(-0.17545304f, -0.32327512f, 0.12401432f, 0.921591f))
        parts["phoenixTailFeather2"] = phoenixTailFeather2
        phoenixTail.addChild(phoenixTailFeather2)

        val phoenixTailFeather3 = MCAModelRenderer(100, 10, 128f, 64f)
        phoenixTailFeather3.addBox(0.0f, -1.0f, 0.0f, 10f, 1f, 4f)
        phoenixTailFeather3.setInitialRotationPoint(7.0f, 0.0f, 0.0f)
        phoenixTailFeather3.setInitialRotationQuaternion(Quaternion(-0.91668236f, -0.115509465f, 0.32641065f, 0.19951737f))
        parts["phoenixTailFeather3"] = phoenixTailFeather3
        phoenixTail.addChild(phoenixTailFeather3)

        val phoenixLWingtip = MCAModelRenderer(54, 0, 128f, 64f)
        phoenixLWingtip.addBox(0.5f, -1.0f, -0.5f, 1f, 4f, 14f)
        phoenixLWingtip.setInitialRotationPoint(0.0f, 1.0f, 11.5f)
        phoenixLWingtip.setInitialRotationQuaternion(Quaternion(-0.9063078F, 0.0F, 0.0F, 0.42261824F))
        parts["phoenixLWingtip"] = phoenixLWingtip
        phoenixLLowerWing.addChild(phoenixLWingtip)

        val phoenixRWingtip = MCAModelRenderer(54, 18, 128f, 64f)
        phoenixRWingtip.addBox(0.5f, -3.0f, -0.5f, 1f, 4f, 14f)
        phoenixRWingtip.setInitialRotationPoint(0.0f, -1.0f, 11.5f)
        phoenixRWingtip.setInitialRotationQuaternion(Quaternion(0.9063078F, 0.0F, 0.0F, 0.42261824F))
        parts["phoenixRWingtip"] = phoenixRWingtip
        phoenixRLowerWing.addChild(phoenixRWingtip)

        val phoenixHead1 = MCAModelRenderer(88, 57, 128f, 64f)
        phoenixHead1.addBox(-1.0f, -1.0f, -2.0f, 6f, 3f, 4f)
        phoenixHead1.setInitialRotationPoint(0.0f, 8.0f, 0.0f)
        phoenixHead1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, -0.043619387f, 0.99904823f))
        parts["phoenixHead1"] = phoenixHead1
        phoenixNeck3.addChild(phoenixHead1)

        val phoenixCrest = MCAModelRenderer(0, 38, 128f, 64f)
        phoenixCrest.addBox(-2.0f, -2.0f, -0.5f, 6f, 3f, 1f)
        phoenixCrest.setInitialRotationPoint(0.0f, 3.0f, 0.0f)
        phoenixCrest.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, -0.17364818f, 0.9848077f))
        parts["phoenixCrest"] = phoenixCrest
        phoenixHead1.addChild(phoenixCrest)

        val phoenixUpperBeak1 = MCAModelRenderer(0, 3, 128f, 64f)
        phoenixUpperBeak1.addBox(0.0f, 0.0f, -1.0f, 4f, 1f, 2f)
        phoenixUpperBeak1.setInitialRotationPoint(4.5f, 0.0f, 0.0f)
        phoenixUpperBeak1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["phoenixUpperBeak1"] = phoenixUpperBeak1
        phoenixHead1.addChild(phoenixUpperBeak1)

        val phoenixLowerBeak = MCAModelRenderer(0, 0, 128f, 64f)
        phoenixLowerBeak.addBox(0.0f, 0.0f, -1.0f, 3f, 1f, 2f)
        phoenixLowerBeak.setInitialRotationPoint(4.5f, -0.5f, 0.0f)
        phoenixLowerBeak.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["phoenixLowerBeak"] = phoenixLowerBeak
        phoenixHead1.addChild(phoenixLowerBeak)

        val phoenixUpperBeak2 = MCAModelRenderer(0, 10, 128f, 64f)
        phoenixUpperBeak2.addBox(0.0f, 0.0f, -0.5f, 3f, 1f, 1f)
        phoenixUpperBeak2.setInitialRotationPoint(0.0f, 1.0f, 0.0f)
        phoenixUpperBeak2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, -0.043619387f, 0.99904823f))
        parts["phoenixUpperBeak2"] = phoenixUpperBeak2
        phoenixUpperBeak1.addChild(phoenixUpperBeak2)
    }

    override fun renderToBuffer(
        matrixStack: PoseStack,
        vertexBuilder: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        phoenixBreast.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun setupAnim(
        entity: FrostPhoenixEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        (entity as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)
    }
}