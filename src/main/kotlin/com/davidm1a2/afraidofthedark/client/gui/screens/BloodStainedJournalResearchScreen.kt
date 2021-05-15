package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.*
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiResearchNodeButton
import com.davidm1a2.afraidofthedark.client.gui.specialControls.ResearchConnector
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color
import kotlin.math.roundToInt

/**
 * The research GUI used by the blood stained journal to show what has been unlocked and what has not been unlocked
 *
 * @constructor initializes the entire GUI
 * @param isCheatSheet True if this GUI should be a cheat sheet, or false otherwise
 * @property scrollBackground The GUI scroll background
 * @property researchNodes The panel that contains all research nodes
 * @property researchConnectors The panel that contains all research node connectors
 * @property nodeConnectorControllerVertical Sprite sheet controller for vertical arrows
 * @property nodeConnectorControllerHorizontal Sprite sheet controller for horizontal arrows
 * @property researchNodeMouseMoveListener Listener used by all research tree nodes
 * @property researchNodeMouseListener Listener used by all research tree nodes
 */
class BloodStainedJournalResearchScreen(isCheatSheet: Boolean) :
    AOTDScreen(TranslationTextComponent("screen.afraidofthedark.blood_stained_journal_research")) {
    private val researchTreeBase: ScrollPane = ScrollPane(2.0, 2.0)
    private val researchTree: StackPane = StackPane(padding = AOTDGuiSpacing(50))
    private val researchNodeMouseMoveListener: (AOTDMouseMoveEvent) -> Unit
    private val researchNodeMouseListener: (AOTDMouseEvent) -> Unit
    private val scrollBackground: ImagePane = ImagePane(
            ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/background.png"),
            ImagePane.DispMode.STRETCH
    )

    init {

        // The border around the research
        val backgroundBorder = ImagePane(
            ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/frame.png"),
            ImagePane.DispMode.STRETCH
        )

        researchTreeBase.add(scrollBackground)
        researchTreeBase.add(researchTree)
        contentPane.add(researchTreeBase)
        contentPane.add(backgroundBorder)

        val windowWidth = AOTDGuiUtility.getWindowWidthInMCCoords()
        val windowHeight = AOTDGuiUtility.getWindowHeightInMCCoords()
        val guiSize: Int
        guiSize = if (windowWidth < windowHeight) windowWidth*3/4 else windowHeight*3/4
        val horizontalPadding = (windowWidth - guiSize)/2
        val verticalPadding = (windowHeight - guiSize)/2
        contentPane.padding = AOTDGuiSpacing(verticalPadding, verticalPadding, horizontalPadding, horizontalPadding)

        // If this is a cheat sheet add a label on top to make that clear
        if (isCheatSheet) {
            // Put the label on top and set the color to white
            val lblCheatSheet = AOTDGuiLabel(ClientData.getOrCreate(32f), AOTDGuiGravity.TOP_CENTER)
            lblCheatSheet.textAlignment = TextAlignment.ALIGN_CENTER
            lblCheatSheet.textColor = Color(255, 255, 255)
            lblCheatSheet.text = "Cheat sheet - select researches to unlock them"
            contentPane.add(lblCheatSheet)
        }

        // Grab the player's research to be used later...
        val playerResearch = entityPlayer.getResearch()
        // Create two node listeners that controls the behavior of selected research nodes
        researchNodeMouseMoveListener = {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                // If the node is visible then play the hover sound since we moved our mouse over it
                if (it.source.isVisible && it.source.inBounds) {
                    entityPlayer.playSound(ModSounds.BUTTON_HOVER, 0.7f, 1.9f)
                }
            }
        }

        researchNodeMouseListener = {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                // Ensure the clicked button is a ResearchNodeButton and the button used clicked is left
                if (it.source.isHovered && it.source is AOTDGuiResearchNodeButton && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    val current = it.source as AOTDGuiResearchNodeButton
                    // Only allow clicking the button if it's within the container
                    if (researchTreeBase.intersects(current)) {
                        val research = current.research
                        // If this isn't a cheat sheet open the research page
                        if (!isCheatSheet) {
                            // If the research is researched show the page UI, otherwise show the pre-page UI
                            if (playerResearch.isResearched(research)) {
                                Minecraft.getInstance().displayGuiScreen(
                                    BloodStainedJournalPageScreen(
                                        I18n.format(research.getUnlocalizedText()),
                                        I18n.format(research.getUnlocalizedName()),
                                        research.researchedRecipes
                                    )
                                )
                            } else if (research.preRequisite != null && playerResearch.isResearched(research.preRequisite)) {
                                Minecraft.getInstance().displayGuiScreen(
                                    BloodStainedJournalPageScreen(
                                        I18n.format(research.getUnlocalizedPreText()),
                                        "???",
                                        research.preResearchedRecipes
                                    )
                                )
                            }
                        } else {
                            // If this research can be researched unlock it
                            if (playerResearch.canResearch(research)) {
                                playerResearch.setResearchAndAlert(research, true, entityPlayer)
                                playerResearch.sync(entityPlayer, false)
                            }
                            // Add a connector to any new researches that are available
                            for (possibleResearch in ModRegistries.RESEARCH.values) {
                                if (possibleResearch.preRequisite == research) {
                                    addConnector(possibleResearch)
                                    addResearchButton(possibleResearch)
                                }
                            }
                        }
                    }
                }
            }
        }

        ModRegistries.RESEARCH.values
            // Only add the node if we know if the previous research or the current research
            .filter { playerResearch.isResearched(it) || playerResearch.canResearch(it) }
            .forEach { addConnector(it) }
        // Now that we have all connectors added add each research node on top to ensure correct z-layer order
        ModRegistries.RESEARCH.values
            // Only add the node if we know if the previous research or the current research
            .filter { playerResearch.isResearched(it) || playerResearch.canResearch(it) }
            .forEach { addResearchButton(it) }
    }

    /**
     * Adds a connector from this research's pre-requisite to itself
     *
     * @param research The research to add a connector to
     */
    private fun addConnector(research: Research) {
        // Get the prerequisite research
        val previous = research.preRequisite ?: return
        // Compute the researches' X and Y position
        val pos = RelativePosition(research.xPosition / 10.0, research.zPosition / 10.0)
        val prevPos = RelativePosition(previous.xPosition / 10.0, previous.zPosition / 10.0)

        researchTree.add(ResearchConnector(prevPos, pos))
    }

    /**
     * Adds a research button for a given research
     *
     * @param research The research to add a button for
     */
    private fun addResearchButton(research: Research) {
        val pos = RelativePosition(research.xPosition / TREE_WIDTH, research.zPosition / TREE_HEIGHT)
        val dim = RelativeDimensions(RESEARCH_WIDTH, RESEARCH_HEIGHT)
        // Create the research button
        val researchNode = AOTDGuiResearchNodeButton(dim, pos, research)
        // Add our pre-build listeners to this node
        researchNode.addMouseListener(researchNodeMouseListener)
        researchNode.addMouseMoveListener(researchNodeMouseMoveListener)
        // Add the node to our tree
        researchTree.add(researchNode)
    }

    /**
     * @return True since we can use the inventory screen to close the GUI
     */
    override fun inventoryToCloseGuiScreen(): Boolean {
        return true
    }

    /**
     * @return True since we want to draw the gradient background
     */
    override fun drawGradientBackground(): Boolean {
        return true
    }

    companion object {
        // The research texture is 64x64
        private const val RESEARCH_HEIGHT = 0.08
        private const val RESEARCH_WIDTH = 0.08

        // Display 10x10 of researches
        private const val TREE_WIDTH = 10.0
        private const val TREE_HEIGHT = 10.0
    }
}