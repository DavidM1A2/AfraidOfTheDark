package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.customControls.ResearchConnector
import com.davidm1a2.afraidofthedark.client.gui.customControls.ResearchNode
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.LabelComponent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.RatioPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ScrollPane
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color

/**
 * The research GUI used by the journal to show what has been unlocked and what has not been unlocked
 */
class JournalResearchScreen(private val isCheatSheet: Boolean) :
    AOTDScreen(TranslationTextComponent("screen.afraidofthedark.journal_research")) {

    private val researchTreeBase: ScrollPane = ScrollPane(2.0, 2.0, scrollOffset)
    private val scrollBackground: ImagePane = ImagePane(ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/background.png"))
    private val backgroundBorder = ImagePane(ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/frame.png"), ImagePane.DispMode.FIT_TO_PARENT)
    private val ratioPane = RatioPane(1, 1)

    init {
        // Add our panes to the root
        ratioPane.add(researchTreeBase)
        contentPane.add(ratioPane)
        contentPane.add(backgroundBorder)
        contentPane.padding = Spacing(0.125)
        ratioPane.gravity = Gravity.CENTER
        backgroundBorder.gravity = Gravity.CENTER

        // Add a background image
        researchTreeBase.add(scrollBackground)

        // If this is a cheat sheet add a label on top to make that clear
        if (isCheatSheet) {
            // Put the label on top and set the color to white
            val lblCheatSheet = LabelComponent(ClientData.getOrCreate(32f), Dimensions(1.0, 0.08))
            lblCheatSheet.textAlignment = TextAlignment.ALIGN_CENTER
            lblCheatSheet.textColor = Color(255, 255, 255)
            lblCheatSheet.text = "Cheat sheet - select researches to unlock them"
            backgroundBorder.add(lblCheatSheet)
        }

        // Add all connectors for visible researches
        ModRegistries.RESEARCH.values.forEach { addConnector(it) }

        // Now that we have all connectors added add each research node on top to ensure correct z-layer order
        ModRegistries.RESEARCH.values.forEach { addResearchButton(it) }
    }

    override fun invalidate() {
        // Make sure the scroll pane hasn't gone out of bounds
        researchTreeBase.checkOutOfBounds()
        super.invalidate()
    }

    private fun addConnector(research: Research) {
        val previous = research.preRequisite ?: return
        val pos = Position(research.xPosition / TREE_WIDTH, (research.yPosition - 4) / TREE_HEIGHT)
        val prevPos = Position(previous.xPosition / TREE_WIDTH, (previous.yPosition - 4) / TREE_HEIGHT)
        researchTreeBase.add(ResearchConnector(prevPos, pos, research))
    }

    private fun addResearchButton(research: Research) {
        val pos = Position(research.xPosition / TREE_WIDTH, (research.yPosition - 4) / TREE_HEIGHT)
        val dim = Dimensions(RESEARCH_WIDTH, RESEARCH_HEIGHT)
        val researchNode = ResearchNode(dim, pos, research, isCheatSheet)
        researchTreeBase.add(researchNode)
    }

    override fun inventoryToCloseGuiScreen() = true

    override fun drawGradientBackground() = true

    override fun removed() {
        scrollOffset = researchTreeBase.getCurrentOffset().getRelative(researchTreeBase)
        super.removed()
    }

    companion object {
        // The research texture is 64x64
        private const val RESEARCH_HEIGHT = 0.06
        private const val RESEARCH_WIDTH = 0.06

        // Display 10x10 of researches
        private const val TREE_WIDTH = 12.0
        private const val TREE_HEIGHT = 12.0

        // The stored scroll pane offset
        private var scrollOffset = Position(-0.25, -0.25)
    }
}