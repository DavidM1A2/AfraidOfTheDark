package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing

/**
 * Class representing a GUI button to be used by AOTD
 */
class TogglePane(
    bkgIcon: ImagePane,
    bkgIconHovered: ImagePane? = null,
    private val toggleIcon: ImagePane,
    silent: Boolean = false,
    margins: Spacing = Spacing(),
    gravity: Gravity = Gravity.TOP_LEFT,
    hoverTexts: Array<String> = emptyArray(),
    padding: Spacing = Spacing(),
    prefSize: Dimensions = Dimensions(Double.MAX_VALUE, Double.MAX_VALUE),
    offset: Position = Position(0.0, 0.0),
    font: TrueTypeFont? = null
) : ButtonPane(bkgIcon, bkgIconHovered, silent, margins, gravity, hoverTexts, padding, prefSize, offset, font) {
    var toggleListener: (Boolean) -> Unit = {}
    var toggled = false
        set(value) {
            field = value
            toggleIcon.isVisible = field
            toggleListener(field)
        }

    init {
        add(toggleIcon)
        addOnClick {
            toggled = !toggled
        }
    }
}
