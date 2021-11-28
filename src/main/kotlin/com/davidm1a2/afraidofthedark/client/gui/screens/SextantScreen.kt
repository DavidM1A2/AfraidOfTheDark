package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.FontCache
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TextFieldPane
import com.davidm1a2.afraidofthedark.common.network.packets.other.ProcessSextantInputPacket
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
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
        background.padding = Spacing(0.1)

        // Grab the font for the text fields
        val textFieldFont = FontCache.getOrCreate(45f)

        // Initialize fields
        angle = TextFieldPane(prefSize = Dimensions(0.4, 0.16), font = textFieldFont)
        latitude = TextFieldPane(prefSize = Dimensions(0.4, 0.16), font = textFieldFont)
        longitude = TextFieldPane(prefSize = Dimensions(0.4, 0.16), font = textFieldFont)

        // All fields are white and contain ghost text based on what they represent
        angle.setTextColor(Color(255, 255, 255))
        angle.setGhostText("Angle")
        angle.offset = Position(0.1, 0.1)
        background.add(angle)
        latitude.setTextColor(Color(255, 255, 255))
        latitude.setGhostText("Latitude")
        latitude.offset = Position(0.1, 0.3)
        background.add(latitude)
        longitude.setTextColor(Color(255, 255, 255))
        longitude.setGhostText("Longitude")
        longitude.offset = Position(0.1, 0.5)
        background.add(longitude)

        // Create a calculate button that performs the math and returns drop location coordinates
        val confirm = ButtonPane(
            icon = ImagePane("afraidofthedark:textures/gui/arcane_journal_open/open_button.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/arcane_journal_open/open_button_hovered.png"),
            gravity = Gravity.BOTTOM_CENTER,
            prefSize = Dimensions(0.5, 0.1),
            font = FontCache.getOrCreate(40f)
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
        background.gravity = Gravity.CENTER
        contentPane.padding = Spacing(0.125)
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