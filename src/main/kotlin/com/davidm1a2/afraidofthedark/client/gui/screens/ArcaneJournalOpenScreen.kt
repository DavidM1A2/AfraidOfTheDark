package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.FontCache
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.capabilities.hasStartedAOTD
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.item.ArcaneJournalItem
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.resources.ResourceLocation

/**
 * Class used to create a journal opening UI
 */
class ArcaneJournalOpenScreen : AOTDScreen(TranslatableComponent("screen.afraidofthedark.arcane_journal_open")) {
    init {
        // Add padding to our root pane
        contentPane.padding = Spacing(0.08)

        // Add a background image to the background panel
        val backgroundPane = StackPane(prefSize = Dimensions(256.0, 256.0, false))
        backgroundPane.gravity = Gravity.CENTER

        val backgroundImage = ImagePane(
            ResourceLocation("afraidofthedark:textures/gui/arcane_journal_open/background.png"),
            ImagePane.DispMode.FIT_TO_PARENT
        )
        backgroundImage.gravity = Gravity.CENTER
        backgroundPane.add(backgroundImage)

        // Add the open button
        val openButton = ButtonPane(
            icon = ImagePane("afraidofthedark:textures/gui/arcane_journal_open/open_button.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/arcane_journal_open/open_button_hovered.png"),
            prefSize = Dimensions(0.1, 0.1),
            offset = Position(0.38, 0.0),
            font = FontCache.getOrCreate(55f)
        )
        openButton.gravity = Gravity.BOTTOM_CENTER
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
                        if (mainHand.item is ArcaneJournalItem) {
                            (mainHand.item as ArcaneJournalItem).setOwner(mainHand, entityPlayer.gameProfile.name)
                        } else if (offHand.item is ArcaneJournalItem) {
                            (offHand.item as ArcaneJournalItem).setOwner(offHand, entityPlayer.gameProfile.name)
                        }

                        // Play the open animation and chat message
                        entityPlayer.playSound(ModSounds.ARCANE_JOURNAL_OPEN, 4.0F, 1.0F)
                        entityPlayer.sendMessage(TranslatableComponent("message.afraidofthedark.arcane_journal.read"))
                    }
                    onClose()
                }
                it.consume()
            }
        }
        backgroundPane.add(openButton)

        contentPane.add(backgroundPane)
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