package com.davidm1a2.afraidofthedark.client.tileEntity.spellCraftingTable

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.Model
import net.minecraft.util.math.vector.Quaternion


class SpellCraftingTableModel internal constructor() : Model(RenderType::entityCutout) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val tableTop: MCAModelRenderer

    init {
        texWidth = 256
        texHeight = 64

        tableTop = MCAModelRenderer(this, 0, 28)
        tableTop.mirror = false
        tableTop.addBox(0.0f, -4.0f, 0.0f, 32f, 4f, 32f)
        tableTop.setInitialRotationPoint(-16.0f, -4.0f, -16.0f)
        tableTop.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        tableTop.setTexSize(256, 64)
        parts["TableTop"] = tableTop

        val leg2 = MCAModelRenderer(this, 176, 36)
        leg2.mirror = false
        leg2.addBox(4.0f, 0.0f, 24.0f, 4f, 24f, 4f)
        leg2.setInitialRotationPoint(0.0f, -28.0f, 0.0f)
        leg2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        leg2.setTexSize(256, 64)
        parts["Leg2"] = leg2
        tableTop.addChild(leg2)

        val leg1 = MCAModelRenderer(this, 160, 36)
        leg1.mirror = false
        leg1.addBox(0.0f, 0.0f, 0.0f, 4f, 24f, 4f)
        leg1.setInitialRotationPoint(4.0f, -28.0f, 4.0f)
        leg1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        leg1.setTexSize(256, 64)
        parts["Leg1"] = leg1
        tableTop.addChild(leg1)

        val leg3 = MCAModelRenderer(this, 144, 36)
        leg3.mirror = false
        leg3.addBox(24.0f, 0.0f, 4.0f, 4f, 24f, 4f)
        leg3.setInitialRotationPoint(0.0f, -28.0f, 0.0f)
        leg3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        leg3.setTexSize(256, 64)
        parts["Leg3"] = leg3
        tableTop.addChild(leg3)

        val leg4 = MCAModelRenderer(this, 128, 36)
        leg4.mirror = false
        leg4.addBox(0.0f, 0.0f, 0.0f, 4f, 24f, 4f)
        leg4.setInitialRotationPoint(24.0f, -28.0f, 24.0f)
        leg4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        leg4.setTexSize(256, 64)
        parts["Leg4"] = leg4
        tableTop.addChild(leg4)

        val inkVialBase = MCAModelRenderer(this, 24, 21)
        inkVialBase.mirror = false
        inkVialBase.addBox(0.0f, 0.0f, 0.0f, 4f, 3f, 4f)
        inkVialBase.setInitialRotationPoint(3.0f, 0.0f, 24.0f)
        inkVialBase.setInitialRotationQuaternion(Quaternion(0.0f, -0.26723838f, 0.0f, 0.96363044f))
        inkVialBase.setTexSize(256, 64)
        parts["InkVialBase"] = inkVialBase
        tableTop.addChild(inkVialBase)

        val spell = MCAModelRenderer(this, 127, 10)
        spell.mirror = false
        spell.addBox(0.0f, 0.0f, 0.0f, 20f, 1f, 12f)
        spell.setInitialRotationPoint(10.0f, -0.5f, 18.0f)
        spell.setInitialRotationQuaternion(Quaternion(0.0f, 0.02617695f, 0.0f, 0.99965733f))
        spell.setTexSize(256, 64)
        parts["Spell"] = spell
        tableTop.addChild(spell)

        val components = MCAModelRenderer(this, 127, 23)
        components.mirror = false
        components.addBox(0.0f, 0.0f, 0.0f, 20f, 1f, 12f)
        components.setInitialRotationPoint(10.0f, -0.5f, 2.0f)
        components.setInitialRotationQuaternion(Quaternion(0.0f, -0.02617695f, 0.0f, 0.99965733f))
        components.setTexSize(256, 64)
        parts["Components"] = components
        tableTop.addChild(components)

        val candleBase = MCAModelRenderer(this, 0, 21)
        candleBase.mirror = false
        candleBase.addBox(0.0f, 0.0f, 0.0f, 6f, 1f, 6f)
        candleBase.setInitialRotationPoint(1.0f, 0.0f, 5.0f)
        candleBase.setInitialRotationQuaternion(Quaternion(0.0f, 0.21643962f, 0.0f, 0.976296f))
        candleBase.setTexSize(256, 64)
        parts["CandleBase"] = candleBase
        tableTop.addChild(candleBase)

        val quill = MCAModelRenderer(this, 24, 16)
        quill.mirror = false
        quill.addBox(0.0f, 0.0f, 0.0f, 1f, 4f, 1f)
        quill.setInitialRotationPoint(1.5f, 3.0f, 1.5f)
        quill.setInitialRotationQuaternion(Quaternion(0.18223552f, 0.0f, 0.0f, 0.9832549f))
        quill.setTexSize(256, 64)
        parts["Quill"] = quill
        inkVialBase.addChild(quill)

        val candleStand = MCAModelRenderer(this, 0, 16)
        candleStand.mirror = false
        candleStand.addBox(0.0f, 0.0f, 0.0f, 2f, 3f, 2f)
        candleStand.setInitialRotationPoint(2.0f, 1.0f, 2.0f)
        candleStand.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        candleStand.setTexSize(256, 64)
        parts["CandleStand"] = candleStand
        candleBase.addChild(candleStand)

        val candle = MCAModelRenderer(this, 0, 12)
        candle.mirror = false
        candle.addBox(0.0f, 0.0f, 0.0f, 1f, 3f, 1f)
        candle.setInitialRotationPoint(2.5f, 4.0f, 2.5f)
        candle.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        candle.setTexSize(256, 64)
        parts["Candle"] = candle
        candleBase.addChild(candle)
    }

    override fun renderToBuffer(
        matrixStack: MatrixStack,
        vertexBuilder: IVertexBuilder,
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