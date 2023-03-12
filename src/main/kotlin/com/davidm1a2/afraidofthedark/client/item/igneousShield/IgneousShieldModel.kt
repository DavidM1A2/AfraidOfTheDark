import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.Model
import net.minecraft.client.renderer.model.ModelRenderer


class IgneousShieldModel : Model(RenderType::entitySolid) {
    private val bbMain: ModelRenderer
    private val handleR1: ModelRenderer
    private val rightEdgeR1: ModelRenderer
    private val leftEdgeR1: ModelRenderer
    private val rightCenterR1: ModelRenderer
    private val leftCenterR1: ModelRenderer

    init {
        texWidth = 64
        texHeight = 64
        bbMain = ModelRenderer(this)
        bbMain.setPos(0.0f, 24.0f, 0.0f)
        bbMain.texOffs(0, 10).addBox(-1.0f, -34.0f, -3.0f, 2.0f, 20.0f, 2.0f, 0.0f, false)
        handleR1 = ModelRenderer(this)
        handleR1.setPos(0.0f, -24.0f, -2.0f)
        bbMain.addChild(handleR1)
        setRotationAngle(handleR1, 0.0f, 0.0f, 1.5708f)
        handleR1.texOffs(0, 0).addBox(-3.0f, -2.0f, 1.0f, 6.0f, 4.0f, 6.0f, 0.0f, false)
        rightEdgeR1 = ModelRenderer(this)
        rightEdgeR1.setPos(0.0f, -24.0f, -2.0f)
        bbMain.addChild(rightEdgeR1)
        setRotationAngle(rightEdgeR1, 0.0f, 0.0f, -0.0873f)
        rightEdgeR1.texOffs(8, 10).addBox(-4.0f, -10.0f, 0.0f, 2.0f, 20.0f, 1.0f, 0.0f, false)
        leftEdgeR1 = ModelRenderer(this)
        leftEdgeR1.setPos(0.0f, -24.0f, -2.0f)
        bbMain.addChild(leftEdgeR1)
        setRotationAngle(leftEdgeR1, 0.0f, 0.0f, 0.0873f)
        leftEdgeR1.texOffs(14, 10).addBox(2.0f, -10.0f, 0.0f, 2.0f, 20.0f, 1.0f, 0.0f, false)
        rightCenterR1 = ModelRenderer(this)
        rightCenterR1.setPos(0.0f, -24.0f, -2.0f)
        bbMain.addChild(rightCenterR1)
        setRotationAngle(rightCenterR1, 0.0f, 0.0f, -0.0436f)
        rightCenterR1.texOffs(20, 10).addBox(-3.0f, -9.0f, -0.5f, 3.0f, 18.0f, 1.0f, 0.0f, false)
        leftCenterR1 = ModelRenderer(this)
        leftCenterR1.setPos(0.0f, -24.0f, -2.0f)
        bbMain.addChild(leftCenterR1)
        setRotationAngle(leftCenterR1, 0.0f, 0.0f, 0.0436f)
        leftCenterR1.texOffs(28, 0).addBox(0.0f, -9.0f, -0.5f, 3.0f, 18.0f, 1.0f, 0.0f, false)
    }

    override fun renderToBuffer(
        matrixStack: MatrixStack,
        buffer: IVertexBuilder,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        bbMain.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
    }

    fun setRotationAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
        modelRenderer.xRot = x
        modelRenderer.yRot = y
        modelRenderer.zRot = z
    }
}