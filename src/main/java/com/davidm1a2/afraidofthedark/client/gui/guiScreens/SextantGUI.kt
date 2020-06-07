package com.davidm1a2.afraidofthedark.client.gui.guiScreens

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiScreen
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiButton
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiTextField
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.ProcessSextantInput
import net.minecraft.util.text.TextComponentTranslation
import org.lwjgl.util.Color

/**
 * Gui screen that represents the sextant GUI
 *
 * @constructor initializes the GUI
 * @property angle The text field containing the meteor's drop angle
 * @property latitude The text field containing the meteor's latitude
 * @property longitude The text field containing the meteor's longitude
 */
class SextantGUI : AOTDGuiScreen() {
    private val angle: AOTDGuiTextField
    private val latitude: AOTDGuiTextField
    private val longitude: AOTDGuiTextField

    init {
        // The gui will be 256x256
        val guiSize = 256

        // Background panel holds all the gui items
        val background =
            AOTDGuiPanel((Constants.GUI_WIDTH - guiSize) / 2, (Constants.GUI_HEIGHT - guiSize) / 2, 256, 256, false)

        // Add an image to the background of the sextant texture
        val backgroundImage = AOTDGuiImage(0, 0, guiSize, guiSize, "afraidofthedark:textures/gui/telescope/sextant.png")
        background.add(backgroundImage)

        // Grab the font for the text fields
        val textFieldFont = ClientData.getOrCreate(45f)

        // Initialize fields
        angle = AOTDGuiTextField(15, 108, 120, 30, textFieldFont)
        latitude = AOTDGuiTextField(15, 140, 120, 30, textFieldFont)
        longitude = AOTDGuiTextField(15, 172, 120, 30, textFieldFont)

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
        val confirm = AOTDGuiButton(
            15,
            204,
            120,
            20,
            "afraidofthedark:textures/gui/journal_sign/sign_button.png",
            "afraidofthedark:textures/gui/journal_sign/sign_button_hovered.png",
            ClientData.getOrCreate(40f)
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
                        entityPlayer.sendMessage(TextComponentTranslation(LocalizationConstants.Sextant.FIELD_EMPTY))
                    }

                    // If any field is invalid send the player an error, otherwise send the info to the server
                    try {
                        AfraidOfTheDark.INSTANCE.packetHandler.sendToServer(
                            ProcessSextantInput(
                                dropAngleText.toInt(),
                                latitudeText.toInt(),
                                longitudeText.toInt()
                            )
                        )
                    } catch (e: NumberFormatException) {
                        entityPlayer.sendMessage(TextComponentTranslation(LocalizationConstants.Sextant.INVALID_VALUE))
                    }
                    entityPlayer.closeScreen()
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