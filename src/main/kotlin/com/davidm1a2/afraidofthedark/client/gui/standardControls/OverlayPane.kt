package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.screens.Screen
import java.awt.Point
import java.awt.Rectangle

open class OverlayPane(private val parent: AOTDPane?) : StackPane() {

    override fun drawOverlay(poseStack: PoseStack, screen: Screen) {
        for (child in getChildren()) {
            child.draw(poseStack)    // Draw children's content as an overlay
        }
        for (child in getChildren()) {
            child.drawOverlay(poseStack, screen)    // Draw children's overlays on top of that
        }
        super.drawOverlay(poseStack, screen) // Call our super method
    }

    override fun negotiateDimensions(width: Double, height: Double) {
        // Overwrite dimensions because an overlay pane covers the whole screen
        this.x = 0
        this.y = 0
        this.width = AOTDGuiUtility.getWindowWidthInMCCoords()
        this.height = AOTDGuiUtility.getWindowHeightInMCCoords()
    }

    override fun calcChildrenBounds() {
        // Overwrite dimensions because an overlay pane covers the whole screen
        this.negotiateDimensions(0.0, 0.0)
        // Call the normal calcChildrenBounds function
        super.calcChildrenBounds()
    }

    override fun intersects(point: Point): Boolean = true

    override fun intersects(rectangle: Rectangle): Boolean = true

    open fun destroy() {
        parent?.remove(this)
        this.isVisible = false
    }
}