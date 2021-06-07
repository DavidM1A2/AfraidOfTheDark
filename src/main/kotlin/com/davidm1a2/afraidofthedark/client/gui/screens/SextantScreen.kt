package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDScreen
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.AbsoluteDimensions
import com.davidm1a2.afraidofthedark.client.gui.standardControls.Button
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TextFieldPane
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.ProcessSextantInputPacket
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color

/**
 * Gui screen that represents the sextant GUI
 *
 * @constructor initializes the GUI
 * @property angle The text field containing the meteor's drop angle
 * @property latitude The text field containing the meteor's latitude
 * @property longitude The text field containing the meteor's longitude
 */
class SextantScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.sextant")) {
    private val angle: TextFieldPane
    private val latitude: TextFieldPane
    private val longitude: TextFieldPane

    init {
        // The gui will be 256x256
        val guiSize = 256.0

        // Background panel holds all the gui items
        val background = StackPane(AbsoluteDimensions(guiSize, guiSize), scissorEnabled = false)

        // Add an image to the background of the sextant texture
        val backgroundImage = ImagePane("afraidofthedark:textures/gui/telescope/sextant.png", ImagePane.DispMode.STRETCH)
        background.add(backgroundImage)

        // Grab the font for the text fields
        val textFieldFont = ClientData.getOrCreate(45f)

        // Initialize fields
        angle = TextFieldPane(prefSize = AbsoluteDimensions(120.0, 30.0), font = textFieldFont)
        latitude = TextFieldPane(prefSize = AbsoluteDimensions(120.0, 30.0), font = textFieldFont)
        longitude = TextFieldPane(prefSize = AbsoluteDimensions(120.0, 30.0), font = textFieldFont)

        // All fields are white and contain ghost text based on what they represent
        angle.setTextColor(Color(255, 255, 255))
        angle.setGhostText("Angle")
        background.add(angle)
        latitude.setTextColor(Color(255, 255, 255))
        latitude.setGhostText("Latitude")
        background.add(latitude)
        longitude.setTextColor(Color(255, 255, 255))
        longitude.setGhostText("Longitude")
        background.add(longitude)

        // Create a calculate button that performs the math and returns drop location coordinates
        val confirm = Button(
            icon = ImagePane("afraidofthedark:textures/gui/journal_sign/sign_button.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/journal_sign/sign_button_hovered.png"),
            prefSize = AbsoluteDimensions(120.0, 20.0),
            font = ClientData.getOrCreate(40f)
        )
        // Text just says calculate
        confirm.setText("Calculate")
        // Center the text
        confirm.setTextAlignment(TextAlignment.ALIGN_CENTER)
        // When clicked tell the server to validate the numbers and create a meteor if possible
        confirm.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                // Ensure this button was the one clicked
                if (it.source.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
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
            }
        }
        background.add(confirm)
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