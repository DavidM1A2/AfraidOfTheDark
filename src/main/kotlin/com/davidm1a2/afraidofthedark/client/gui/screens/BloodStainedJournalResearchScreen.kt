package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.*
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiResearchNodeButton
import com.davidm1a2.afraidofthedark.client.gui.specialControls.ResearchConnector
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color

/**
 * The research GUI used by the blood stained journal to show what has been unlocked and what has not been unlocked
 */
class BloodStainedJournalResearchScreen(private val isCheatSheet: Boolean) :
    AOTDScreen(TranslationTextComponent("screen.afraidofthedark.blood_stained_journal_research")) {

    private val researchTreeBase: ScrollPane = ScrollPane(2.0, 2.0)
    private val scrollBackground: ImagePane = ImagePane(ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/background.png"))
    private val backgroundBorder = ImagePane(ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/frame.png"))

    init {
        // Add our panes to the root
        contentPane.add(researchTreeBase)
        contentPane.add(backgroundBorder)

        // Add a background image
        researchTreeBase.add(scrollBackground)

        // If this is a cheat sheet add a label on top to make that clear
        if (isCheatSheet) {
            // Put the label on top and set the color to white
            val lblCheatSheet = AOTDGuiLabel(ClientData.getOrCreate(32f), RelativeDimensions(1.0, 0.08), AOTDGuiGravity.TOP_CENTER)
            lblCheatSheet.textAlignment = TextAlignment.ALIGN_CENTER
            lblCheatSheet.textColor = Color(255, 255, 255)
            lblCheatSheet.text = "Cheat sheet - select researches to unlock them"
            backgroundBorder.add(lblCheatSheet)
        }
    }

    override fun invalidate() {
        super.invalidate()

        // Remove everything but the background
        researchTreeBase.getChildren().filter { it != scrollBackground }.forEach { researchTreeBase.remove(it) }

        // Add padding to keep the GUI square
        val windowWidth = AOTDGuiUtility.getWindowWidthInMCCoords()
        val windowHeight = AOTDGuiUtility.getWindowHeightInMCCoords()
        val guiSize = if (windowWidth < windowHeight) windowWidth*3/4 else windowHeight*3/4
        val horizontalPadding = (windowWidth - guiSize)/2
        val verticalPadding = (windowHeight - guiSize)/2
        contentPane.padding = AOTDGuiSpacing(verticalPadding, verticalPadding, horizontalPadding, horizontalPadding)

        // Update research
        val playerResearch = entityPlayer.getResearch()

        // Add all connectors for visible researches
        ModRegistries.RESEARCH.values
                // Only add the node if we know if the previous research or the current research
                .filter { playerResearch.isResearched(it) || playerResearch.canResearch(it) }
                .forEach { addConnector(it) }

        // Now that we have all connectors added add each research node on top to ensure correct z-layer order
        ModRegistries.RESEARCH.values
                // Only add the node if we know if the previous research or the current research
                .filter { playerResearch.isResearched(it) || playerResearch.canResearch(it) }
                .forEach { addResearchButton(it) }

        // Make sure the scroll pane hasn't gone out of bounds
        researchTreeBase.checkOutOfBounds()
    }

    private fun addConnector(research: Research) {
        val previous = research.preRequisite ?: return
        val pos = RelativePosition(research.xPosition / 10.0, research.zPosition / 10.0)
        val prevPos = RelativePosition(previous.xPosition / 10.0, previous.zPosition / 10.0)
        researchTreeBase.add(ResearchConnector(prevPos, pos))
    }

    private fun addResearchButton(research: Research) {
        val pos = RelativePosition(research.xPosition / TREE_WIDTH, research.zPosition / TREE_HEIGHT)
        val dim = RelativeDimensions(RESEARCH_WIDTH, RESEARCH_HEIGHT)
        val researchNode = AOTDGuiResearchNodeButton(dim, pos, research, isCheatSheet, this)
        researchTreeBase.add(researchNode)
    }

    override fun inventoryToCloseGuiScreen() = true

    override fun drawGradientBackground() = true

    companion object {
        // The research texture is 64x64
        private const val RESEARCH_HEIGHT = 0.08
        private const val RESEARCH_WIDTH = 0.08

        // Display 10x10 of researches
        private const val TREE_WIDTH = 10.0
        private const val TREE_HEIGHT = 10.0
    }
}