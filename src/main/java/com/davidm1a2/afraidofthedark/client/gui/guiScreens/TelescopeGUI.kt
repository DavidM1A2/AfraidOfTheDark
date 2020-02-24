package com.davidm1a2.afraidofthedark.client.gui.guiScreens

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiClickAndDragable
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiMeteorButton
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.UpdateWatchedMeteor

/**
 * Gui screen that represents the telescope GUI
 *
 * @constructor initializes the entire UI
 * @property telescopeMeteors The panel that will contain all the meteors on it
 * @property telescopeImage The image that represents the 'sky'
 */
class TelescopeGUI : AOTDGuiClickAndDragable() {
    private val telescopeMeteors: AOTDGuiPanel
    private val telescopeImage: AOTDGuiImage

    init {
        // Calculate the various positions of GUI elements on the screen
        val xPosTelescope = (Constants.GUI_WIDTH - GUI_SIZE) / 2
        val yPosTelescope = (Constants.GUI_HEIGHT - GUI_SIZE) / 2

        // Create a panel that will hold all the UI contents
        val telescope = AOTDGuiPanel(xPosTelescope, yPosTelescope, GUI_SIZE, GUI_SIZE, false)

        // Create a frame that will be the edge of the telescope UI
        val telescopeFrame = AOTDGuiImage(0, 0, GUI_SIZE, GUI_SIZE, "afraidofthedark:textures/gui/telescope/frame.png")

        // Create the panel to hold all the meteors, the size doesnt matter since it is just a base to hold all of our meteor buttons
        telescopeMeteors = AOTDGuiPanel(0, 0, 1, 1, false)

        // Create a clipping panel to hold the meteors so they don't clip outside
        val telescopeMeteorClip =
            AOTDGuiPanel(SIDE_BUFFER, SIDE_BUFFER, GUI_SIZE - SIDE_BUFFER * 2, GUI_SIZE - SIDE_BUFFER * 2, true)

        // Initialize the background star sky image and center the image
        telescopeImage = AOTDGuiImage(
            0,
            0,
            GUI_SIZE - SIDE_BUFFER * 2,
            GUI_SIZE - SIDE_BUFFER * 2,
            "afraidofthedark:textures/gui/telescope/background.png",
            3840,
            2160
        )
        telescopeImage.u = guiOffsetX + (telescopeImage.getMaxTextureWidth() - telescopeImage.getWidth()) / 2
        telescopeImage.v = guiOffsetY + (telescopeImage.getMaxTextureHeight() - telescopeImage.getHeight()) / 2
        // Click listener that gets called when we click a meteor button
        val meteorClickListener =
            { event: AOTDMouseEvent ->
                if (event.eventType == AOTDMouseEvent.EventType.Click) {
                    // Make sure the button clicked was in fact hovered and the click was LMB
                    if (event.source.isHovered && event.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                        // Ensure that the button is visible and not just outside of the visual clip
                        if (telescopeMeteorClip.intersects(event.source)) {
                            // Tell the server we're watching a new meteor. It will update our capability NBT data for us
                            AfraidOfTheDark.INSTANCE.packetHandler.sendToServer(UpdateWatchedMeteor((event.source as AOTDGuiMeteorButton).meteorType))
                            entityPlayer.closeScreen()
                        }
                    }
                }
            }

        // Grab the player's research
        val playerResearch = entityPlayer.getResearch()
        // Grab a list of possible meteors
        val possibleMeteors =
            ModRegistries.METEORS.valuesCollection.filter { playerResearch.isResearched(it.preRequisite) }
        // If we somehow open the GUI without having any known meteors don't show any. This can happen if the telescope is right
        // clicked and the packet to update research from the server hasn't arrived yet
        if (possibleMeteors.isNotEmpty()) {
            // Grab a random object to place meteors
            val random = entityPlayer.rng
            // Create a random number of meteors to generate, let's go with 30-80
            val numberOfMeteors = 30 + random.nextInt(50)
            // Create one button for each meteor
            for (i in 0 until numberOfMeteors) {
                // Create the meteor button based on if astronomy 2 is researched or not
                val meteorButton = AOTDGuiMeteorButton(
                    random.nextInt(telescopeImage.getMaxTextureWidth()) - telescopeImage.getMaxTextureWidth() / 2,
                    random.nextInt(telescopeImage.getMaxTextureHeight()) - telescopeImage.getMaxTextureHeight() / 2,
                    64,
                    64,
                    possibleMeteors[random.nextInt(possibleMeteors.size)]
                )
                // Add a listener
                meteorButton.addMouseListener(meteorClickListener)
                // Add the button
                telescopeMeteors.add(meteorButton)
            }
        }
        // Add all the panels to the content pane
        telescopeMeteorClip.add(telescopeImage)
        telescopeMeteorClip.add(telescopeMeteors)
        telescope.add(telescopeMeteorClip)
        telescope.add(telescopeFrame)
        contentPane.add(telescope)
    }

    /**
     * Called when we drag the mouse
     *
     * @param mouseX            The mouse X position
     * @param mouseY            The mouse Y position
     * @param lastButtonClicked The last button clicked
     * @param timeBetweenClicks The time between the last click
     */
    override fun mouseClickMove(mouseX: Int, mouseY: Int, lastButtonClicked: Int, timeBetweenClicks: Long) {
        // Call super first
        super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks)
        // Move the meteors based on the gui offset
        telescopeMeteors.setX(-guiOffsetX + telescopeMeteors.parent!!.getX())
        telescopeMeteors.setY(-guiOffsetY + telescopeMeteors.parent!!.getY())
        // Update the background image's U/V
        telescopeImage.u = guiOffsetX + (telescopeImage.getMaxTextureWidth() - telescopeImage.getWidth()) / 2
        telescopeImage.v = guiOffsetY + (telescopeImage.getMaxTextureHeight() - telescopeImage.getHeight()) / 2
    }

    /**
     * We can use this to test if the gui has scrolled out of bounds or not
     */
    override fun checkOutOfBounds() {
        guiOffsetX =
            guiOffsetX.coerceIn(-telescopeImage.getMaxTextureWidth() / 2, telescopeImage.getMaxTextureWidth() / 2)
        guiOffsetY =
            guiOffsetY.coerceIn(-telescopeImage.getMaxTextureHeight() / 2, telescopeImage.getMaxTextureHeight() / 2)
    }

    /**
     * @return True since the inventory button closes the UI
     */
    override fun inventoryToCloseGuiScreen(): Boolean {
        return true
    }

    /**
     * @return True since the UI has a gradient background
     */
    override fun drawGradientBackground(): Boolean {
        return true
    }

    companion object {
        // The gui will be 256x256
        private const val GUI_SIZE = 256
        // The amount of buffer to apply to the sides for the fade in to make the telescope look realistic
        private const val SIDE_BUFFER = 22
    }
}