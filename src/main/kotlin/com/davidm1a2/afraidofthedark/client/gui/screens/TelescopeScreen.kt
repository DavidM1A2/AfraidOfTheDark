package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.RatioPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ScrollPane
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.item.telescope.TelescopeBaseItem
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.UpdateWatchedMeteorPacket
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color
import kotlin.random.Random

/**
 * Gui screen that represents the telescope GUI
 */
class TelescopeScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.telescope")) {

    init {
        // Create a panel that will hold all the UI contents
        val layoutPane = RatioPane(1, 1)
        layoutPane.gravity = Gravity.CENTER
        layoutPane.padding = Spacing(0.09)

        // Create a frame that will be the edge of the telescope UI
        val telescopeFrame = ImagePane("afraidofthedark:textures/gui/telescope/frame.png", ImagePane.DispMode.FIT_TO_PARENT)
        telescopeFrame.gravity = Gravity.CENTER

        // Initialize the background star sky image and center the image
        val telescopeImage = ImagePane(
            "afraidofthedark:textures/gui/telescope/background.png",
            ImagePane.DispMode.STRETCH
        )

        val telescope = ScrollPane(8.0, 6.0, Position(Random.nextDouble(1.0 / 8.0 - 1.0, 0.0), Random.nextDouble(1.0 / 6.0 - 1.0, 0.0)))
        telescope.add(telescopeImage)

        // Grab the player's research
        val playerResearch = entityPlayer.getResearch()
        // Grab a list of possible meteors
        val possibleMeteors =
            ModRegistries.METEORS.values.filter { playerResearch.isResearched(it.preRequisite) }
        // If we somehow open the GUI without having any known meteors don't show any. This can happen if the telescope is right
        // clicked and the packet to update research from the server hasn't arrived yet
        if (possibleMeteors.isNotEmpty()) {
            // Grab a random object to place meteors
            val random = entityPlayer.random
            // Create a random number of meteors to generate
            val numberOfMeteors = 1 + random.nextInt(5)
            // Create one button for each meteor
            for (i in 0 until numberOfMeteors) {
                // Create the meteor button based on if astronomy 2 is researched or not
                val meteorType = possibleMeteors[random.nextInt(possibleMeteors.size)]
                val meteorIcon = ImagePane(meteorType.icon)
                val meteorButton = ButtonPane(
                    icon = meteorIcon,
                    iconHovered = meteorIcon,
                    prefSize = Dimensions(0.009, 0.012),
                    offset = Position(random.nextDouble(), random.nextDouble()),
                    silent = true
                )
                meteorIcon.color = Color(255, 255, 255, random.nextInt(64))
                // Add a listener
                meteorButton.addOnClick {
                    val telescopeItem = entityPlayer.mainHandItem.item as? TelescopeBaseItem ?: entityPlayer.offhandItem.item as? TelescopeBaseItem
                    val accuracy = telescopeItem?.accuracy ?: WORST_ACCURACY
                    // Tell the server we're watching a new meteor. It will update our capability NBT data for us
                    AfraidOfTheDark.packetHandler.sendToServer(UpdateWatchedMeteorPacket(meteorType, accuracy))
                    onClose()
                }
                // Add the button
                telescope.add(meteorButton)
            }
        }
        // Add all the panels to the content pane
        layoutPane.add(telescope)
        contentPane.add(layoutPane)
        contentPane.add(telescopeFrame)
        contentPane.padding = Spacing(0.125)
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
        // The worst telescope accuracy possible
        private val WORST_ACCURACY = ModItems.TELESCOPE.accuracy
    }
}