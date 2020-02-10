package com.davidm1a2.afraidofthedark.client.gui.guiScreens

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiClickAndDragable
import com.davidm1a2.afraidofthedark.client.gui.base.SpriteSheetController
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
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
import com.davidm1a2.afraidofthedark.common.utility.openGui
import net.minecraft.util.ResourceLocation
import org.lwjgl.util.Color

/**
 * The research GUI used by the blood stained journal to show what has been unlocked and what has not been unlocked
 *
 * @constructor initializes the entire GUI
 * @param isCheatSheet True if this GUI should be a cheat sheet, or false otherwise
 * @property scrollBackground The GUI scroll background
 * @property researchTree The panel that contains all research nodes
 * @property nodeConnectorControllerVertical Sprite sheet controller for vertical arrows
 * @property nodeConnectorControllerHorizontal Sprite sheet controller for horizontal arrows
 * @property researchNodeMouseMoveListener Listener used by all research tree nodes
 * @property researchNodeMouseListener Listener used by all research tree nodes
 */
class BloodStainedJournalResearchGUI(isCheatSheet: Boolean) : AOTDGuiClickAndDragable()
{
    private val scrollBackground: AOTDGuiImage
    private val researchTree: AOTDGuiPanel
    private val nodeConnectorControllerVertical = SpriteSheetController(500, 20, 60, 180, true, true)
    private val nodeConnectorControllerHorizontal = SpriteSheetController(500, 20, 180, 60, true, false)
    private val researchNodeMouseMoveListener: (AOTDMouseMoveEvent) -> Unit
    private val researchNodeMouseListener: (AOTDMouseEvent) -> Unit

    init
    {
        // Calculate the various positions of GUI elements on the screen
        val xPosScroll = (Constants.GUI_WIDTH - BACKGROUND_WIDTH) / 2
        val yPosScroll = (Constants.GUI_HEIGHT - BACKGROUND_HEIGHT) / 2

        // Recall our previous GUI offsets from the last time we had the GUI open, this helps remember where we left off in the UI
        guiOffsetX = lastGuiOffsetX
        guiOffsetY = lastGuiOffsetY

        // Create the research tree panel that will hold all the research nodes
        // The base panel that contains all researches
        val researchTreeBase = AOTDGuiPanel(xPosScroll, yPosScroll, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, true)
        researchTree = AOTDGuiPanel(-guiOffsetX, -guiOffsetY, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, false)
        contentPane.add(researchTreeBase)
        scrollBackground = AOTDGuiImage(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, "afraidofthedark:textures/gui/journal_tech_tree/background.png", 1024, 1024)
        scrollBackground.u = guiOffsetX + (scrollBackground.getMaxTextureWidth() - scrollBackground.getWidth()) / 2
        scrollBackground.v = guiOffsetY + (scrollBackground.getMaxTextureHeight() - scrollBackground.getHeight())

        // The border around the research
        val backgroundBorder = AOTDGuiImage(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, "afraidofthedark:textures/gui/journal_tech_tree/frame.png")
        researchTreeBase.add(scrollBackground)
        researchTreeBase.add(researchTree)
        researchTreeBase.add(backgroundBorder)

        // If this is a cheat sheet add a label on top to make that clear
        if (isCheatSheet)
        {
            // Put the label on top and set the color to white
            val lblCheatSheet = AOTDGuiLabel(15, 20, BACKGROUND_WIDTH - 30, 20, ClientData.getOrCreate(32f))
            lblCheatSheet.textAlignment = TextAlignment.ALIGN_CENTER
            lblCheatSheet.textColor = Color(255, 255, 255)
            lblCheatSheet.text = "Cheat sheet - select researches to unlock them"
            researchTreeBase.add(lblCheatSheet)
        }

        // Grab the player's research to be used later...
        val playerResearch = entityPlayer.getResearch()
        // Create two node listeners that controls the behavior of selected research nodes
        researchNodeMouseMoveListener =
            {
                if (it.eventType == AOTDMouseMoveEvent.EventType.Enter)
                {
                    // If the node is visible then play the hover sound since we moved our mouse over it
                    if (it.source.isVisible && researchTreeBase.intersects(it.source))
                    {
                        entityPlayer.playSound(ModSounds.BUTTON_HOVER, 0.7f, 1.9f)
                    }
                }
            }
        researchNodeMouseListener =
            {
                if (it.eventType == AOTDMouseEvent.EventType.Click)
                {
                    // Ensure the clicked button is a ResearchNodeButton and the button used clicked is left
                    if (it.source.isHovered && it.source is AOTDGuiResearchNodeButton && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                    {
                        val current = it.source as AOTDGuiResearchNodeButton
                        // Only allow clicking the button if it's within the container
                        if (researchTreeBase.intersects(current))
                        {
                            // If this isn't a cheat sheet open the research page
                            if (!isCheatSheet)
                            {
                                // Store the selected research
                                ClientData.lastSelectedResearch = current.research
                                // If the research is researched show the page UI, otherwise show the pre-page UI
                                if (playerResearch.isResearched(current.research))
                                {
                                    entityPlayer.openGui(AOTDGuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID)
                                }
                                else if (current.research.preRequisite != null && playerResearch.isResearched(current.research.preRequisite))
                                {
                                    entityPlayer.openGui(AOTDGuiHandler.BLOOD_STAINED_JOURNAL_PAGE_PRE_ID)
                                }
                            }
                            else
                            {
                                // If this research can be researched unlock it
                                if (playerResearch.canResearch(current.research))
                                {
                                    playerResearch.setResearchAndAlert(current.research, true, entityPlayer)
                                    playerResearch.sync(entityPlayer, false)
                                }
                            }
                        }
                    }
                }
            }

        // Go over all known researches and add a connector for each that has a known pre-requisite
        ModRegistries.RESEARCH.valuesCollection.stream()
            // We can only draw connectors if we have a pre-requisite
            .filter { it.preRequisite != null }
            // Only add the connectors if we know if the previous research or the current research
            .filter { playerResearch.isResearched(it) || playerResearch.canResearch(it) }
            .forEach { addConnector(it) }
        // Now that we have all connectors added add each research node on top to ensure correct z-layer order
        ModRegistries.RESEARCH.valuesCollection.stream()
            // Only add the node if we know if the previous research or the current research
            .filter { playerResearch.isResearched(it) || playerResearch.canResearch(it) }
            .forEach { addResearchButton(it) }

        // Add our sprite sheet controllers
        addSpriteSheetController(nodeConnectorControllerHorizontal)
        addSpriteSheetController(nodeConnectorControllerVertical)
    }

    /**
     * Adds a connector from this research's pre-requisite to itself
     *
     * @param research The research to add a connector to
     */
    private fun addConnector(research: Research)
    {
        // Compute the button's X and Y position
        val xPos = BACKGROUND_WIDTH / 2 - 16 + DISTANCE_BETWEEN_RESEARCHES * research.xPosition
        val yPos = BACKGROUND_HEIGHT - 50 - DISTANCE_BETWEEN_RESEARCHES * research.zPosition
        // Grab the prerequisite research
        val previous = research.preRequisite
        if (previous != null)
        {
            // Depending on where the research is in relation to its previous research create an arrow
            when
            {
                research.xPosition < previous.xPosition ->
                {
                    researchTree.add(
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
                research.xPosition > previous.xPosition ->
                {
                    researchTree.add(
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
                research.zPosition > previous.zPosition ->
                {
                    researchTree.add(
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
                research.zPosition < previous.zPosition ->
                {
                    researchTree.add(
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
    private fun addResearchButton(research: Research)
    {
        // Compute the button's X and Y position
        val xPos = BACKGROUND_WIDTH / 2 - 16 + DISTANCE_BETWEEN_RESEARCHES * research.xPosition
        val yPos = BACKGROUND_HEIGHT - 50 - DISTANCE_BETWEEN_RESEARCHES * research.zPosition
        // Create the research button
        val researchNode = AOTDGuiResearchNodeButton(xPos, yPos, research)
        // Add our pre-build listeners to this node
        researchNode.addMouseListener(researchNodeMouseListener)
        researchNode.addMouseMoveListener(researchNodeMouseMoveListener)
        // Add the node to our tree
        researchTree.add(researchNode)
    }

    /**
     * Called when we drag the mouse
     *
     * @param mouseX            The mouse X position
     * @param mouseY            The mouse Y position
     * @param lastButtonClicked The last button clicked
     * @param timeBetweenClicks The time between the last click
     */
    override fun mouseClickMove(mouseX: Int, mouseY: Int, lastButtonClicked: Int, timeBetweenClicks: Long)
    {
        // Call the super method
        super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks)
        // Update the research tree's X and Y coordinates
        researchTree.setX(-guiOffsetX + researchTree.parent!!.getX())
        researchTree.setY(-guiOffsetY + researchTree.parent!!.getY())
        // Set the scroll background U and V
        scrollBackground.u = guiOffsetX + (scrollBackground.getMaxTextureWidth() - scrollBackground.getWidth()) / 2
        scrollBackground.v = guiOffsetY + (scrollBackground.getMaxTextureHeight() - scrollBackground.getHeight())
    }

    /**
     * We can use this to test if the gui has scrolled out of bounds or not
     */
    override fun checkOutOfBounds()
    {
        val backgroundWiggleRoom = (scrollBackground.getMaxTextureWidth() - scrollBackground.getWidth()) / 2
        guiOffsetX = guiOffsetX.coerceIn(-backgroundWiggleRoom, backgroundWiggleRoom)
        guiOffsetY = guiOffsetY.coerceIn(-scrollBackground.getMaxTextureHeight() + scrollBackground.getHeight(), 0)
    }

    /**
     * Called when the screen is unloaded. Used set the last known gui offsets
     */
    override fun onGuiClosed()
    {
        lastGuiOffsetX = guiOffsetX
        lastGuiOffsetY = guiOffsetY
        super.onGuiClosed()
    }

    /**
     * @return True since we can use the inventory screen to close the GUI
     */
    override fun inventoryToCloseGuiScreen(): Boolean
    {
        return true
    }

    /**
     * @return True since we want to draw the gradient background
     */
    override fun drawGradientBackground(): Boolean
    {
        return true
    }

    companion object
    {
        // Set the size of the UI to always be 256x256
        private const val BACKGROUND_HEIGHT = 256
        private const val BACKGROUND_WIDTH = 256
        // Distance between research icon nodes is 75px
        private const val DISTANCE_BETWEEN_RESEARCHES = 75
        // Static fields that will keep track of where the offset was the last time the UI was open
        private var lastGuiOffsetX = 0
        private var lastGuiOffsetY = 0
    }
}