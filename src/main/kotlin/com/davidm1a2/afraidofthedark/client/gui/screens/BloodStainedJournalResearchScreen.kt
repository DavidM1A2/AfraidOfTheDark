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
    private val scrollBackground: AOTDGuiImage
    private val researchTreeBase: ScrollPane
    private val nodeConnectorControllerVertical = SpriteSheetController(500, 20, 15, 45, true, true)
    private val nodeConnectorControllerHorizontal = SpriteSheetController(500, 20, 45, 15, true, false)
    private val researchNodeMouseMoveListener: (AOTDMouseMoveEvent) -> Unit
    private val researchNodeMouseListener: (AOTDMouseEvent) -> Unit

    init {

        // Create the research tree panel that will hold all the research nodes
        // The base panel that contains all researches
        researchTreeBase = ScrollPane(2.0, 2.0)

        scrollBackground = AOTDGuiImage(
            ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/background.png"),
            AOTDImageDispMode.STRETCH
        )

        // The border around the research
        val backgroundBorder = AOTDGuiImage(
            ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/frame.png"),
            AOTDImageDispMode.STRETCH
        )

        researchTreeBase.add(scrollBackground)
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
                if (it.source.isVisible && researchTreeBase.intersects(it.source)) {
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

        // Now that we have all connectors added add each research node on top to ensure correct z-layer order
        ModRegistries.RESEARCH.values
            // Only add the node if we know if the previous research or the current research
            .filter { playerResearch.isResearched(it) || playerResearch.canResearch(it) }
            .forEach { addConnector(it); addResearchButton(it) }

        // Add our sprite sheet controllers
        addSpriteSheetController(nodeConnectorControllerHorizontal)
        addSpriteSheetController(nodeConnectorControllerVertical)
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
        val xPos = (research.xPosition * DISTANCE_BETWEEN_RESEARCHES / researchTreeBase.scrollWidthRatio).roundToInt()
        val yPos = (research.zPosition * DISTANCE_BETWEEN_RESEARCHES / researchTreeBase.scrollHeightRatio).roundToInt()
        val xPosPrev = (previous.xPosition * DISTANCE_BETWEEN_RESEARCHES / researchTreeBase.scrollWidthRatio).roundToInt()
        val yPosPrev = (previous.zPosition * DISTANCE_BETWEEN_RESEARCHES / researchTreeBase.scrollHeightRatio).roundToInt()

        researchTreeBase.add(ResearchConnector(Position(xPos, yPos), Position(xPosPrev - xPos, yPosPrev - yPos)))
    }

    /**
     * Adds a research button for a given research
     *
     * @param research The research to add a button for
     */
    private fun addResearchButton(research: Research) {
        val xPos = (research.xPosition * DISTANCE_BETWEEN_RESEARCHES / researchTreeBase.scrollWidthRatio).roundToInt()
        val yPos = (research.zPosition * DISTANCE_BETWEEN_RESEARCHES / researchTreeBase.scrollHeightRatio).roundToInt()
        val width = (RESEARCH_WIDTH / researchTreeBase.scrollWidthRatio).roundToInt()
        val height = (RESEARCH_HEIGHT / researchTreeBase.scrollHeightRatio).roundToInt()
        // Create the research button
        val researchNode = AOTDGuiResearchNodeButton(width, height, xPos, yPos, research)
        // Add our pre-build listeners to this node
        researchNode.addMouseListener(researchNodeMouseListener)
        researchNode.addMouseMoveListener(researchNodeMouseMoveListener)
        // Add the node to our tree
        researchTreeBase.add(researchNode)
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
        // The background and frame texture is 1024x1024
        private const val BACKGROUND_HEIGHT = 1024
        private const val BACKGROUND_WIDTH = 1024

        // The research texture is 64x64
        private const val RESEARCH_HEIGHT = 64
        private const val RESEARCH_WIDTH = 64

        // Distance between research icon nodes is 75px
        private const val DISTANCE_BETWEEN_RESEARCHES = 75
    }
}