package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.events.IIntChangeListener
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import net.minecraft.util.ResourceLocation
import java.awt.Image
import kotlin.math.min

class DropdownPane(val font: TrueTypeFont, val values: List<String>, private val defaultIndex: Int = -1) : StackPane() {
    var selectedId = defaultIndex
        private set
    var selectedString = values.getOrElse(defaultIndex) { "" }
        private set
    var expanded = false
        private set

    private val background = ImagePane(ResourceLocation("afraidofthedark:textures/gui/text_field_background.png"), ImagePane.DispMode.STRETCH)
    private val downBtn = ButtonPane(ImagePane(ResourceLocation("afraidofthedark:textures/gui/spell_editor/dropdown_down.png"), ImagePane.DispMode.STRETCH))
    private val upBtn = ButtonPane(ImagePane(ResourceLocation("afraidofthedark:textures/gui/spell_editor/dropdown_up.png"), ImagePane.DispMode.STRETCH))
    private val overlayPane = OverlayPane(this)
    private val mainLabel = LabelComponent(font, Dimensions(1.0, 1.0))
    private val optionsBkg = ImagePane("afraidofthedark:textures/gui/spell_editor/drop_down_background.png")
    private val optionsScissor = StackPane()
    private val options = ListPane(ListPane.ExpandDirection.DOWN)

    private var listener : IIntChangeListener = IIntChangeListener { _: Int, _: Int -> }

    init {
        background.prefSize = Dimensions(1.0, 1.0)
        background.padding = Spacing(0.1)
        val btnContainer = RatioPane(1, 1)
        btnContainer.gravity = Gravity.CENTER_RIGHT
        downBtn.addOnClick {
            expand()
        }
        btnContainer.add(downBtn)
        upBtn.addOnClick {
            collapse()
        }
        btnContainer.add(upBtn)
        upBtn.isVisible = false
        mainLabel.text = selectedString
        overlayPane.add(optionsBkg)
        optionsBkg.add(optionsScissor)
        optionsScissor.add(options)
        overlayPane.isVisible = false

        this.add(background)
        background.add(mainLabel)
        this.add(btnContainer)
        this.add(overlayPane)
    }

    override fun invalidate() {
        super.invalidate()
        if (expanded) {
            optionsBkg.offset = Position(this.x.toDouble(), this.y + this.height.toDouble(), false)
            val fitHeight = min(this.height * values.size, AOTDGuiUtility.getWindowHeightInMCCoords() - this.height - this.y)
            optionsBkg.prefSize = Dimensions(this.width.toDouble(), fitHeight.toDouble(), false)
            options.getChildren().forEach { options.remove(it) }
            for (i in values.indices) {
                options.add(
                    ButtonPane(null, null, true, padding = Spacing(0.1), prefSize = Dimensions(this.width.toDouble(), this.height.toDouble(), false), font = font).apply {
                        setText(values[i])
                        addOnClick {
                            val oldVal = selectedId
                            selectedId = i
                            selectedString = values[i]
                            mainLabel.text = selectedString
                            listener.apply(oldVal, selectedId)
                            collapse()
                        }
                    }
                )
            }
            overlayPane.isVisible = true
        } else {
            overlayPane.isVisible = false
        }
        super.invalidate()
    }

    fun expand() {
        this.expanded = true
        downBtn.isVisible = false
        upBtn.isVisible = true
        this.invalidate()
    }

    fun collapse() {
        this.expanded = false
        upBtn.isVisible = false
        downBtn.isVisible = true
        this.invalidate()
    }

    fun setSelected(index: Int) {
        if (index == -1) {
            selectedId = -1
            selectedString = ""
            mainLabel.text = selectedString
        }
        if (index in values.indices) {
            selectedId = index
            selectedString = values[index]
            mainLabel.text = selectedString
        }
    }

    fun setChangeListener(listener: IIntChangeListener) {
        this.listener = listener
    }
}