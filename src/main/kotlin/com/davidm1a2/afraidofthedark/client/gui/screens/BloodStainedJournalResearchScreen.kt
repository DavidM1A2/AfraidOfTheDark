package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.base.*
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiResearchNodeButton
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiLabel
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiSpriteSheetImage
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color

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
    AOTDScreenClickAndDragable(TranslationTextComponent("screen.afraidofthedark.blood_stained_journal_research")) {
    private val scrollBackground: AOTDGuiImage
    private val researchTreeBase: AOTDGuiPanel
    private val nodeConnectorControllerVertical = SpriteSheetController(500, 20, 15, 45, true, true)
    private val nodeConnectorControllerHorizontal = SpriteSheetController(500, 20, 45, 15, true, false)
    private val researchNodeMouseMoveListener: (AOTDMouseMoveEvent) -> Unit
    private val researchNodeMouseListener: (AOTDMouseEvent) -> Unit

    init {

        // Create the research tree panel that will hold all the research nodes
        // The base panel that contains all researches
        researchTreeBase = AOTDGuiPanel(BACKGROUND_WIDTH, BACKGROUND_HEIGHT, gravity = AOTDGuiGravity.CENTER)
        scrollPane.add(researchTreeBase)
        scrollBackground = AOTDGuiImage(
            ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/background.png"),
            AOTDImageDispMode.FIT_TO_SIZE,
            BACKGROUND_WIDTH,
            BACKGROUND_HEIGHT
        )

        // The border around the research
        val backgroundBorder = AOTDGuiImage(
            ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/frame.png"),
            AOTDImageDispMode.FIT_TO_PARENT,
            BACKGROUND_WIDTH,
            BACKGROUND_HEIGHT
        )

        researchTreeBase.add(scrollBackground)
        contentPane.add(backgroundBorder)

        // If this is a cheat sheet add a label on top to make that clear
        if (isCheatSheet) {
            // Put the label on top and set the color to white
            val lblCheatSheet = AOTDGuiLabel(BACKGROUND_WIDTH, 20, ClientData.getOrCreate(32f), AOTDGuiGravity.TOP_CENTER)
            lblCheatSheet.textAlignment = TextAlignment.ALIGN_CENTER
            lblCheatSheet.textColor = Color(255, 255, 255)
            lblCheatSheet.text = "Cheat sheet - select researches to unlock them"
            researchTreeBase.add(lblCheatSheet)
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

        // Go over all known researches and add a connector for each that has a known pre-requisite
        /*ModRegistries.RESEARCH.values
            // We can only draw connectors if we have a pre-requisite
            .filter { it.preRequisite != null }
            // Only add the connectors if we know if the previous research or the current research
            .filter { playerResearch.isResearched(it) || playerResearch.canResearch(it) }
            .forEach { addConnector(it) }
        // Now that we have all connectors added add each research node on top to ensure correct z-layer order
        ModRegistries.RESEARCH.values
            // Only add the node if we know if the previous research or the current research
            .filter { playerResearch.isResearched(it) || playerResearch.canResearch(it) }
            .forEach { addResearchButton(it) }

        // Add our sprite sheet controllers
        addSpriteSheetController(nodeConnectorControllerHorizontal)
        addSpriteSheetController(nodeConnectorControllerVertical)*/
    }

    /**
     * Adds a connector from this research's pre-requisite to itself
     *
     * @param research The research to add a connector to
     */
    private fun addConnector(research: Research) {
        // Compute the button's X and Y position
        val xPos = BACKGROUND_WIDTH / 2 - 16 + DISTANCE_BETWEEN_RESEARCHES * research.xPosition
        val yPos = BACKGROUND_HEIGHT - 50 - DISTANCE_BETWEEN_RESEARCHES * research.zPosition
        // Grab the prerequisite research
        val previous = research.preRequisite
        if (previous != null) {
            // Depending on where the research is in relation to its previous research create an arrow
            when {
                research.xPosition < previous.xPosition -> {
                    researchTreeBase.add(
                        AOTDGuiSpriteSheetImage(
                            xPos + 26,
                            yPos + 9,
                            54,
                            14,
                            ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/horizontal_connector.png"),
                            nodeConnectorControllerHorizontal
                        )
                    )
                }
                research.xPosition > previous.xPosition -> {
                    researchTreeBase.add(
                        AOTDGuiSpriteSheetImage(
                            xPos - 50,
                            yPos + 9,
                            54,
                            14,
                            ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/horizontal_connector.png"),
                            nodeConnectorControllerHorizontal
                        )
                    )
                }
                research.zPosition > previous.zPosition -> {
                    researchTreeBase.add(
                        AOTDGuiSpriteSheetImage(
                            xPos + 9,
                            yPos + 30,
                            14,
                            46,
                            ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/vertical_connector.png"),
                            nodeConnectorControllerVertical
                        )
                    )
                }
                research.zPosition < previous.zPosition -> {
                    researchTreeBase.add(
                        AOTDGuiSpriteSheetImage(
                            xPos + 9,
                            yPos - 46,
                            14,
                            46,
                            ResourceLocation("afraidofthedark:textures/gui/journal_tech_tree/vertical_connector.png"),
                            nodeConnectorControllerVertical
                        )
                    )
                }
            }
        }
    }

    /**
     * Adds a research button for a given research
     *
     * @param research The research to add a button for
     */
    private fun addResearchButton(research: Research) {
        // Create the research button
        val researchNode = AOTDGuiResearchNodeButton(RESEARCH_WIDTH, RESEARCH_HEIGHT, research)
        // Add our pre-build listeners to this node
        researchNode.addMouseListener(researchNodeMouseListener)
        researchNode.addMouseMoveListener(researchNodeMouseMoveListener)
        // Add the node to our tree
        researchTreeBase.add(researchNode)
    }

    /**
     * We can use this to test if the gui has scrolled out of bounds or not
     */
    override fun checkOutOfBounds() {
        val backgroundWiggleRoom = (scrollBackground.width - scrollBackground.width) / 2
        this.contentPane.xOffset = guiOffsetX.coerceIn(-backgroundWiggleRoom, backgroundWiggleRoom)
        guiOffsetY = guiOffsetY.coerceIn(-scrollBackground.height + scrollBackground.height, 0)
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