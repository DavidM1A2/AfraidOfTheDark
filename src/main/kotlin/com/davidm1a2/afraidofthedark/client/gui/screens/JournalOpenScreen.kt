package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.capabilities.hasStartedAOTD
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.item.JournalItem
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color

/**
 * Class used to create a journal opening UI
 */
class JournalOpenScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.journal_open")) {
    init {
        // Add padding to our root pane
        contentPane.padding = Spacing(0.08)

        // Add a background image to the background panel
        val background = ImagePane(
            ResourceLocation("afraidofthedark:textures/gui/journal_open/journal.png"),
            ImagePane.DispMode.FIT_TO_PARENT
        )
        background.gravity = Gravity.CENTER
        contentPane.add(background)

        // Add the open button
        val openButton = ButtonPane(
            icon = ImagePane("afraidofthedark:textures/gui/journal_open/open_button.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/journal_open/open_button_hovered.png"),
            prefSize = Dimensions(0.5, 0.1),
            offset = Position(0.0, 0.1),
            font = ClientData.getOrCreate(55f)
        )
        openButton.setText("Open")
        openButton.setTextColor(Color(105, 0, 108))
        openButton.setTextAlignment(TextAlignment.ALIGN_CENTER)
        openButton.gravity = Gravity.CENTER

        // When we click the open button either start the mod or tell the user they messed up
        openButton.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    val playerBasics = entityPlayer.getBasics()
                    // if the name is correct start the mod
                    if (!entityPlayer.hasStartedAOTD()) {
                        // We now started the mod

                        // Set that we started the mod
                        playerBasics.startAOTD(entityPlayer)

                        // Set the journal to have a new owner name
                        val mainHand = entityPlayer.mainHandItem
                        val offHand = entityPlayer.mainHandItem
                        // We must check both off hand and main hand since the journal could be in either hand
                        if (mainHand.item is JournalItem) {
                            (mainHand.item as JournalItem).setOwner(mainHand, entityPlayer.gameProfile.name)
                        } else if (offHand.item is JournalItem) {
                            (offHand.item as JournalItem).setOwner(offHand, entityPlayer.gameProfile.name)
                        }

                        // Play the open animation and chat message
                        entityPlayer.playSound(ModSounds.JOURNAL_OPEN, 4.0F, 1.0F)
                        entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.journal.read"), entityPlayer.uuid)
                    }
                    onClose()
                }
                it.consume()
            }
        }

        background.add(openButton)
    }

    /**
     * @return True because we want the background to be a gray
     */
    override fun drawGradientBackground(): Boolean {
        return true
    }

    /**
     * @return False since we can't use e to close the GUI screen
     */
    override fun inventoryToCloseGuiScreen(): Boolean {
        return true
    }
}
