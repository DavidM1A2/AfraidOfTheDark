package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDScreen
import com.davidm1a2.afraidofthedark.client.gui.base.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiMeteorButton
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.item.telescope.TelescopeBaseItem
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.UpdateWatchedMeteorPacket
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Image

/**
 * Gui screen that represents the telescope GUI
 *
 * @constructor initializes the entire UI
 * @property telescopeMeteors The panel that will contain all the meteors on it
 * @property telescopeImage The image that represents the 'sky'
 */
class TelescopeScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.telescope")) {
    private val telescopeMeteors: StackPane
    private val telescopeImage: ImagePane

    init {
        // Calculate the various positions of GUI elements on the screen
        val xPosTelescope = (Constants.BASE_GUI_WIDTH - GUI_SIZE) / 2
        val yPosTelescope = (Constants.BASE_GUI_HEIGHT - GUI_SIZE) / 2

        // Create a panel that will hold all the UI contents
        val telescope = StackPane(Dimensions(GUI_SIZE.toDouble(), GUI_SIZE.toDouble()))

        // Create a frame that will be the edge of the telescope UI
        val telescopeFrame = ImagePane("afraidofthedark:textures/gui/telescope/frame.png", ImagePane.DispMode.FIT_TO_PARENT)

        // Create the panel to hold all the meteors, the size doesnt matter since it is just a base to hold all of our meteor buttons
        telescopeMeteors = StackPane()

        // Create a clipping panel to hold the meteors so they don't clip outside
        val telescopeMeteorClip =
            StackPane(scissorEnabled = true)

        // Initialize the background star sky image and center the image
        telescopeImage = ImagePane(
            "afraidofthedark:textures/gui/telescope/background.png",
            ImagePane.DispMode.FIT_TO_PARENT
        )
        // Click listener that gets called when we click a meteor button
        val meteorClickListener = { event: AOTDMouseEvent ->
            if (event.eventType == AOTDMouseEvent.EventType.Click) {
                // Make sure the button clicked was in fact hovered and the click was LMB
                if (event.source.isHovered && event.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    // Ensure that the button is visible and not just outside of the visual clip
                    if (telescopeMeteorClip.intersects(event.source)) {
                        val telescopeItem = entityPlayer.heldItemMainhand.item as? TelescopeBaseItem ?: entityPlayer.heldItemOffhand.item as? TelescopeBaseItem
                        val accuracy = telescopeItem?.accuracy ?: WORST_ACCURACY
                        // Tell the server we're watching a new meteor. It will update our capability NBT data for us
                        AfraidOfTheDark.packetHandler.sendToServer(UpdateWatchedMeteorPacket((event.source as AOTDGuiMeteorButton).meteorType, accuracy))
                        onClose()
                    }
                }
            }
        }

        // Grab the player's research
        val playerResearch = entityPlayer.getResearch()
        // Grab a list of possible meteors
        val possibleMeteors =
            ModRegistries.METEORS.values.filter { playerResearch.isResearched(it.preRequisite) }
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
                    Dimensions(64.0, 64.0),
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

        // The worst telescope accuracy possible
        private val WORST_ACCURACY = ModItems.TELESCOPE.accuracy
    }
}