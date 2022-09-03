package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.FontCache
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
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color

/**
 * The research GUI used by the journal to show what has been unlocked and what has not been unlocked
 */
class ArcaneJournalResearchScreen(private val isCheatSheet: Boolean) :
    AOTDScreen(TranslationTextComponent("screen.afraidofthedark.arcane_journal_research")) {

    private val researchTreeBase: ScrollPane = ScrollPane(4.0, 4.0, scrollOffset)
    private val scrollBackground: ImagePane = ImagePane(ResourceLocation("afraidofthedark:textures/gui/arcane_journal_tech_tree/background.png"))
    private val backgroundBorder = ImagePane(ResourceLocation("afraidofthedark:textures/gui/arcane_journal_tech_tree/frame.png"), ImagePane.DispMode.FIT_TO_PARENT)
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
            val lblCheatSheet = LabelComponent(FontCache.getOrCreate(32f), Dimensions(1.0, 0.08))
            lblCheatSheet.textAlignment = TextAlignment.ALIGN_CENTER
            lblCheatSheet.textColor = Color(255, 255, 255)
            lblCheatSheet.text = "Cheat sheet - select researches to unlock them"
            backgroundBorder.add(lblCheatSheet)
        }

        // Add all connectors for visible researches
        ModRegistries.RESEARCH.values.forEach { addConnector(it) }

        // Now that we have all connectors added, add each research node on top to ensure correct z-layer order
        ModRegistries.RESEARCH.values.forEach { addResearchButton(it) }
    }

    override fun update() {
        // Make sure the scroll pane hasn't gone out of bounds
        researchTreeBase.checkOutOfBounds()
        super.update()
    }

    private fun addConnector(research: Research) {
        val previous = research.preRequisite ?: return
        val pos = Position(research.xPosition / 2, research.yPosition / 2)
        val prevPos = Position(previous.xPosition / 2, previous.yPosition / 2)
        researchTreeBase.add(ResearchConnector(prevPos, pos, research))
    }

    private fun addResearchButton(research: Research) {
        val pos = Position(research.xPosition / 2, research.yPosition / 2)
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
        private const val RESEARCH_HEIGHT = 0.03
        private const val RESEARCH_WIDTH = 0.03

        // The stored scroll pane offset
        private var scrollOffset = Position(-0.375, -0.375)
    }
}