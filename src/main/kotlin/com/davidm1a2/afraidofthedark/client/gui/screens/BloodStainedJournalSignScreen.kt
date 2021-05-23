package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDScreen
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import com.davidm1a2.afraidofthedark.client.gui.standardControls.Button
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiTextField
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.item.JournalItem
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvents
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color

/**
 * Class used to create a blood stained journal signing UI
 *
 * @constructor adds any required components to the sign UI
 * @property nameSignField The text field that you sign your name in
 */
class BloodStainedJournalSignScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.blood_stained_journal_sign")) {
    private val nameSignField: AOTDGuiTextField

    init {
        // Add padding to our root pane
        contentPane.padding = RelativeSpacing(0.08)

        // Add a background image to the background panel
        val background = ImagePane(
            ResourceLocation("afraidofthedark:textures/gui/journal_sign/blood_stained_journal.png"),
            ImagePane.DispMode.FIT_TO_PARENT
        )
        background.gravity = GuiGravity.CENTER
        contentPane.add(background)

        this.nameSignField = AOTDGuiTextField(
            prefSize = RelativeDimensions(0.6, 0.15),
            offset = RelativePosition(0.0, -0.1),
            font = ClientData.getOrCreate(45f)
        )
        this.nameSignField.setTextColor(Color(255, 0, 0))
        this.nameSignField.gravity = GuiGravity.CENTER
        background.add(this.nameSignField)

        // Add the sign button
        val signButton = Button(
            prefSize = RelativeDimensions(0.5, 0.1),
            offset = RelativePosition(0.0, 0.1),
            icon = ImagePane("afraidofthedark:textures/gui/journal_sign/sign_button.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/journal_sign/sign_button_hovered.png"),
            font = ClientData.getOrCreate(55f)
        )
        signButton.setText("Sign")
        signButton.setTextColor(Color(255, 0, 0))
        signButton.setTextAlignment(TextAlignment.ALIGN_CENTER)
        signButton.gravity = GuiGravity.CENTER

        // When we click the sign button either start the mod or tell the user they messed up
        signButton.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    entityPlayer.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f)
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
        // If we hover the sign button play a button hover sound
        signButton.addMouseMoveListener {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                entityPlayer.playSound(ModSounds.BUTTON_HOVER, 0.1f, 0.8f)
            }
        }
        // When we type a character play a type sound
        this.nameSignField.addKeyListener {
            if (it.eventType == AOTDKeyEvent.KeyEventType.Type) {
                if (nameSignField.isFocused) {
                    entityPlayer.playSound(ModSounds.KEY_TYPED, 0.4f, 0.8f)
                    it.consume()
                }
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