package com.davidm1a2.afraidofthedark.client.tileEntity.spellCraftingTable

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.Model
import net.minecraft.client.renderer.RenderType


class SpellCraftingTableModel internal constructor() : Model(RenderType::entityCutout) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val tableTop: MCAModelRenderer

    init {
        tableTop = MCAModelRenderer(0, 28, 256f, 64f)
        tableTop.addBox(0.0f, -4.0f, 0.0f, 32f, 4f, 32f)
        tableTop.setInitialRotationPoint(-16.0f, -4.0f, -16.0f)
        tableTop.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["TableTop"] = tableTop

        val leg2 = MCAModelRenderer(176, 36, 256f, 64f)
        leg2.addBox(4.0f, 0.0f, 24.0f, 4f, 24f, 4f)
        leg2.setInitialRotationPoint(0.0f, -28.0f, 0.0f)
        leg2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Leg2"] = leg2
        tableTop.addChild(leg2)

        val leg1 = MCAModelRenderer(160, 36, 256f, 64f)
        leg1.addBox(0.0f, 0.0f, 0.0f, 4f, 24f, 4f)
        leg1.setInitialRotationPoint(4.0f, -28.0f, 4.0f)
        leg1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Leg1"] = leg1
        tableTop.addChild(leg1)

        val leg3 = MCAModelRenderer(144, 36, 256f, 64f)
        leg3.addBox(24.0f, 0.0f, 4.0f, 4f, 24f, 4f)
        leg3.setInitialRotationPoint(0.0f, -28.0f, 0.0f)
        leg3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Leg3"] = leg3
        tableTop.addChild(leg3)

        val leg4 = MCAModelRenderer(128, 36, 256f, 64f)
        leg4.addBox(0.0f, 0.0f, 0.0f, 4f, 24f, 4f)
        leg4.setInitialRotationPoint(24.0f, -28.0f, 24.0f)
        leg4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Leg4"] = leg4
        tableTop.addChild(leg4)

        val inkVialBase = MCAModelRenderer(24, 21, 256f, 64f)
        inkVialBase.addBox(0.0f, 0.0f, 0.0f, 4f, 3f, 4f)
        inkVialBase.setInitialRotationPoint(3.0f, 0.0f, 24.0f)
        inkVialBase.setInitialRotationQuaternion(Quaternion(0.0f, -0.26723838f, 0.0f, 0.96363044f))
        parts["InkVialBase"] = inkVialBase
        tableTop.addChild(inkVialBase)

        val spell = MCAModelRenderer(127, 10, 256f, 64f)
        spell.addBox(0.0f, 0.0f, 0.0f, 20f, 1f, 12f)
        spell.setInitialRotationPoint(10.0f, -0.5f, 18.0f)
        spell.setInitialRotationQuaternion(Quaternion(0.0f, 0.02617695f, 0.0f, 0.99965733f))
        parts["Spell"] = spell
        tableTop.addChild(spell)

        val components = MCAModelRenderer(127, 23, 256f, 64f)
        components.addBox(0.0f, 0.0f, 0.0f, 20f, 1f, 12f)
        components.setInitialRotationPoint(10.0f, -0.5f, 2.0f)
        components.setInitialRotationQuaternion(Quaternion(0.0f, -0.02617695f, 0.0f, 0.99965733f))
        parts["Components"] = components
        tableTop.addChild(components)

        val candleBase = MCAModelRenderer(0, 21, 256f, 64f)
        candleBase.addBox(0.0f, 0.0f, 0.0f, 6f, 1f, 6f)
        candleBase.setInitialRotationPoint(1.0f, 0.0f, 5.0f)
        candleBase.setInitialRotationQuaternion(Quaternion(0.0f, 0.21643962f, 0.0f, 0.976296f))
        parts["CandleBase"] = candleBase
        tableTop.addChild(candleBase)

        val quill = MCAModelRenderer(24, 16, 256f, 64f)
        quill.addBox(0.0f, 0.0f, 0.0f, 1f, 4f, 1f)
        quill.setInitialRotationPoint(1.5f, 3.0f, 1.5f)
        quill.setInitialRotationQuaternion(Quaternion(0.18223552f, 0.0f, 0.0f, 0.9832549f))
        parts["Quill"] = quill
        inkVialBase.addChild(quill)

        val candleStand = MCAModelRenderer(0, 16, 256f, 64f)
        candleStand.addBox(0.0f, 0.0f, 0.0f, 2f, 3f, 2f)
        candleStand.setInitialRotationPoint(2.0f, 1.0f, 2.0f)
        candleStand.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["CandleStand"] = candleStand
        candleBase.addChild(candleStand)

        val candle = MCAModelRenderer(0, 12, 256f, 64f)
        candle.addBox(0.0f, 0.0f, 0.0f, 1f, 3f, 1f)
        candle.setInitialRotationPoint(2.5f, 4.0f, 2.5f)
        candle.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Candle"] = candle
        candleBase.addChild(candle)
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
        tableTop.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }
}