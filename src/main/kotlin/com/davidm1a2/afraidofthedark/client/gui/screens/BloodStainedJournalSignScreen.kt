package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TextFieldPane
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.item.JournalItem
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color

/**
 * Class used to create a blood stained journal signing UI
 */
class BloodStainedJournalSignScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.blood_stained_journal_sign")) {
    private val nameSignField: TextFieldPane

    init {
        // Add padding to our root pane
        contentPane.padding = Spacing(0.08)

        // Add a background image to the background panel
        val background = ImagePane(
            ResourceLocation("afraidofthedark:textures/gui/journal_sign/blood_stained_journal.png"),
            ImagePane.DispMode.FIT_TO_PARENT
        )
        background.gravity = Gravity.CENTER
        contentPane.add(background)

        this.nameSignField = TextFieldPane(
            prefSize = Dimensions(0.6, 0.15),
            offset = Position(0.0, -0.1),
            font = ClientData.getOrCreate(45f)
        )
        this.nameSignField.setTextColor(Color(255, 0, 0))
        this.nameSignField.gravity = Gravity.CENTER
        background.add(this.nameSignField)

        // Add the sign button
        val signButton = ButtonPane(
            icon = ImagePane("afraidofthedark:textures/gui/journal_sign/sign_button.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/journal_sign/sign_button_hovered.png"),
            prefSize = Dimensions(0.5, 0.1),
            offset = Position(0.0, 0.1),
            font = ClientData.getOrCreate(55f)
        )
        signButton.setText("Sign")
        signButton.setTextColor(Color(255, 0, 0))
        signButton.setTextAlignment(TextAlignment.ALIGN_CENTER)
        signButton.gravity = Gravity.CENTER

        // When we click the sign button either start the mod or tell the user they messed up
        signButton.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    val playerBasics = entityPlayer.getBasics()
                    val playerResearch = entityPlayer.getResearch()
                    if (nameSignField.getText() == entityPlayer.gameProfile.name) {
                        // if the name is correct start the mod
                        if (!playerBasics.startedAOTD) {
                            // We now started the mod

                            // Set that we started the mod and perform a client -> server sync
                            playerBasics.startedAOTD = true
                            playerBasics.syncStartedAOTD(entityPlayer)
                            playerResearch.setResearchAndAlert(
                                ModResearches.AN_UNBREAKABLE_COVENANT,
                                true,
                                entityPlayer
                            )
                            playerResearch.setResearchAndAlert(ModResearches.CROSSBOW, true, entityPlayer)
                            playerResearch.sync(entityPlayer, false)

                            // Set the journal to have a new owner name
                            val mainHand = entityPlayer.heldItemMainhand
                            val offHand = entityPlayer.heldItemOffhand
                            // We must check both off hand and main hand since the journal could be in either hand
                            if (mainHand.item is JournalItem) {
                                (mainHand.item as JournalItem).setOwner(mainHand, entityPlayer.gameProfile.name)
                            } else if (offHand.item is JournalItem) {
                                (offHand.item as JournalItem).setOwner(offHand, entityPlayer.gameProfile.name)
                            }

                            // Play the sign animation and chat message
                            entityPlayer.playSound(ModSounds.JOURNAL_SIGN, 4.0F, 1.0F)
                            entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.journal.sign.successful"))
                        }
                    } else {
                        // Test if the user has not yet started AOTD
                        if (!playerBasics.startedAOTD) {
                            // If he has not started then print out a message that the name was wrong
                            entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.journal.sign.unsuccessful"))
                            onClose()
                        }
                    }
                    onClose()
                }
                it.consume()
            }
        }

        background.add(signButton)
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
        return !this.nameSignField.isFocused
    }
}