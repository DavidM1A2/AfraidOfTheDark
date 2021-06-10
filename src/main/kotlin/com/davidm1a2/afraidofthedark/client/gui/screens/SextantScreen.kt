package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TextFieldPane
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.ProcessSextantInputPacket
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color

/**
 * Gui screen that represents the sextant GUI
 */
class SextantScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.sextant")) {
    private val angle: TextFieldPane
    private val latitude: TextFieldPane
    private val longitude: TextFieldPane

    init {
        // Add an image to the background of the sextant texture
        val background = ImagePane("afraidofthedark:textures/gui/telescope/sextant.png", ImagePane.DispMode.FIT_TO_PARENT)
        background.padding = RelativeSpacing(0.1)

        // Grab the font for the text fields
        val textFieldFont = ClientData.getOrCreate(45f)

        // Initialize fields
        angle = TextFieldPane(prefSize = RelativeDimensions(0.4, 0.1), font = textFieldFont)
        latitude = TextFieldPane(prefSize = RelativeDimensions(0.4, 0.1), font = textFieldFont)
        longitude = TextFieldPane(prefSize = RelativeDimensions(0.4, 0.1), font = textFieldFont)

        // All fields are white and contain ghost text based on what they represent
        angle.setTextColor(Color(255, 255, 255))
        angle.setGhostText("Angle")
        angle.offset = RelativePosition(0.1, 0.1)
        background.add(angle)
        latitude.setTextColor(Color(255, 255, 255))
        latitude.setGhostText("Latitude")
        latitude.offset = RelativePosition(0.1, 0.25)
        background.add(latitude)
        longitude.setTextColor(Color(255, 255, 255))
        longitude.setGhostText("Longitude")
        longitude.offset = RelativePosition(0.1, 0.4)
        background.add(longitude)

        // Create a calculate button that performs the math and returns drop location coordinates
        val confirm = ButtonPane(
            icon = ImagePane("afraidofthedark:textures/gui/journal_sign/sign_button.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/journal_sign/sign_button_hovered.png"),
            prefSize = RelativeDimensions(0.5, 0.1),
            font = ClientData.getOrCreate(40f),
            gravity = GuiGravity.BOTTOM_CENTER
        )
        // Text just says calculate
        confirm.setText("Calculate")
        // Center the text
        confirm.setTextAlignment(TextAlignment.ALIGN_CENTER)
        // When clicked tell the server to validate the numbers and create a meteor if possible
        confirm.addOnClick {
            // Grab the text fron the text fields
            val dropAngleText = angle.getText()
            val latitudeText = latitude.getText()
            val longitudeText = longitude.getText()
            // If any of the fields are empty print a message
            if (dropAngleText.isEmpty() || latitudeText.isEmpty() || longitudeText.isEmpty()) {
                entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.sextant.process.field_empty"))
            }

            // If any field is invalid send the player an error, otherwise send the info to the server
            try {
                AfraidOfTheDark.packetHandler.sendToServer(
                    ProcessSextantInputPacket(
                        dropAngleText.toInt(),
                        latitudeText.toInt(),
                        longitudeText.toInt()
                    )
                )
            } catch (e: NumberFormatException) {
                entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.sextant.process.invalid_vals"))
            }
            onClose()
        }
        background.add(confirm)
        background.gravity = GuiGravity.CENTER
        contentPane.padding = RelativeSpacing(0.125)
        contentPane.add(background)
    }

    /**
     * @return True if none of the 3 fields are focused, false otherwise
     */
    override fun inventoryToCloseGuiScreen(): Boolean {
        return !angle.isFocused && !latitude.isFocused && !longitude.isFocused
    }

    /**
     * @return True since this UI uses a gradient background
     */
    override fun drawGradientBackground(): Boolean {
        return true
    }
}