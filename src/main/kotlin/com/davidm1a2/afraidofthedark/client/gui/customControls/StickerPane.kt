package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.util.ResourceLocation

/**
 * Advanced control that displays a journal page sticker
 */
class StickerPane(prefSize: Dimensions) : AOTDPane(prefSize = prefSize) {
    private val stickerBox: ImagePane

    init {
        stickerBox = ImagePane(displayMode = ImagePane.DispMode.FIT_TO_PARENT)
        stickerBox.margins = Spacing(0.08, 0.08, 0.1, 0.1)
        add(stickerBox)
        val topStickerBoxOverlay = ImagePane(STICKER_OVERLAY_TEXTURE, ImagePane.DispMode.FIT_TO_PARENT)
        add(topStickerBoxOverlay)
    }

    fun setSticker(resourceLocation: ResourceLocation?) {
        stickerBox.updateImageTexture(resourceLocation)
        isVisible = resourceLocation != null
    }

    /**
     * Called to draw the control, just draws all of its children
     */
    override fun draw(matrixStack: MatrixStack) {
        if (this.isVisible) {
            super.draw(matrixStack)
        }
    }

    companion object {
        private val STICKER_OVERLAY_TEXTURE = ResourceLocation(Constants.MOD_ID, "textures/gui/arcane_journal_page/sticker_overlay.png")
    }
}
