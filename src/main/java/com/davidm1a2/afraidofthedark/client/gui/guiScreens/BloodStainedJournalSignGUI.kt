package com.davidm1a2.afraidofthedark.client.gui.guiScreens

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiScreen
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiButton
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiTextField
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.item.ItemJournal
import net.minecraft.init.SoundEvents
import net.minecraft.util.text.TextComponentTranslation
import org.lwjgl.util.Color

/**
 * Class used to create a blood stained journal signing UI
 *
 * @constructor adds any required components to the sign UI
 * @property nameSignField The text field that you sign your name in
 */
class BloodStainedJournalSignGUI : AOTDGuiScreen()
{
    private val nameSignField: AOTDGuiTextField

    init
    {
        val guiSize = 256

        // Setup the background panel that holds all of our controls
        val backgroundPanel = AOTDGuiPanel(
            (Constants.GUI_WIDTH - guiSize) / 2,
            (Constants.GUI_HEIGHT - guiSize) / 2,
            guiSize,
            guiSize,
            false
        )

        // Add a background image to the background panel
        val backgroundImageSize = 220
        val backgroundImage = AOTDGuiImage(
            (guiSize - backgroundImageSize) / 2,
            0,
            backgroundImageSize,
            backgroundImageSize,
            "afraidofthedark:textures/gui/journal_sign/blood_stained_journal.png"
        )
        backgroundPanel.add(backgroundImage)

        this.nameSignField = AOTDGuiTextField(
            45,
            90,
            160,
            30,
            ClientData.getTargaMSHandFontSized(45f)
        )
        this.nameSignField.setTextColor(Color(255, 0, 0))
        backgroundPanel.add(this.nameSignField)

        // Add the sign button
        val signButtonWidth = 100
        val signButtonHeight = 25
        val signButton = AOTDGuiButton(
            guiSize / 2 - signButtonWidth / 2,
            guiSize - 30,
            signButtonWidth,
            signButtonHeight,
            "afraidofthedark:textures/gui/journal_sign/sign_button.png",
            "afraidofthedark:textures/gui/journal_sign/sign_button_hovered.png",
            ClientData.getTargaMSHandFontSized(55f)
        )
        signButton.setText("Sign")
        signButton.setTextColor(Color(255, 0, 0))
        signButton.setTextAlignment(TextAlignment.ALIGN_CENTER)

        // When we click the sign button either start the mod or tell the user they messed up
        signButton.addMouseListener()
        {
            if (it.eventType == AOTDMouseEvent.EventType.Click)
            {
                if (it.source.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    entityPlayer.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f)
                    val playerBasics = entityPlayer.getBasics()
                    val playerResearch = entityPlayer.getResearch()
                    if (nameSignField.getText() == entityPlayer.gameProfile.name)
                    {
                        // if the name is correct start the mod
                        if (!playerBasics.startedAOTD)
                        {
                            // We now started the mod

                            // Set that we started the mod and perform a client -> server sync
                            playerBasics.startedAOTD = true
                            playerBasics.syncStartedAOTD(entityPlayer)
                            playerResearch.setResearchAndAlert(ModResearches.AN_UNBREAKABLE_COVENANT, true, entityPlayer)
                            playerResearch.setResearchAndAlert(ModResearches.CROSSBOW, true, entityPlayer)
                            playerResearch.sync(entityPlayer, false)

                            // Set the journal to have a new owner name
                            val mainHand = entityPlayer.heldItemMainhand
                            val offHand = entityPlayer.heldItemOffhand
                            // We must check both off hand and main hand since the journal could be in either hand
                            if (mainHand.item is ItemJournal)
                            {
                                (mainHand.item as ItemJournal).setOwner(mainHand, entityPlayer.gameProfile.name)
                            }
                            else if (offHand.item is ItemJournal)
                            {
                                (offHand.item as ItemJournal).setOwner(offHand, entityPlayer.gameProfile.name)
                            }

                            // Play the sign animation and chat message
                            entityPlayer.playSound(ModSounds.JOURNAL_SIGN, 4.0F, 1.0F)
                            entityPlayer.sendMessage(TextComponentTranslation("aotd.journal.sign.successful"))
                        }
                    }
                    else
                    {
                        // Test if the user has not yet started AOTD
                        if (!playerBasics.startedAOTD)
                        {
                            // If he has not started then print out a message that the name was wrong
                            entityPlayer.sendMessage(TextComponentTranslation("aotd.journal.sign.unsuccessful"))
                            entityPlayer.closeScreen()
                        }
                    }
                    entityPlayer.closeScreen()
                }
                it.consume()
            }
        }
        // If we hover the sign button play a button hover sound
        signButton.addMouseMoveListener()
        {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter)
            {
                entityPlayer.playSound(ModSounds.BUTTON_HOVER, 0.1f, 0.8f)
            }
        }
        // When we type a character play a type sound
        this.nameSignField.addKeyListener()
        {
            if (it.eventType == AOTDKeyEvent.KeyEventType.Type)
            {
                if (nameSignField.isFocused)
                {
                    entityPlayer.playSound(ModSounds.KEY_TYPED, 0.4f, 0.8f)
                    it.consume()
                }
            }
        }
        backgroundPanel.add(signButton)

        // Add the background panel to the content pane
        this.contentPane.add(backgroundPanel)
    }

    /**
     * @return True because we want the background to be a gray
     */
    override fun drawGradientBackground(): Boolean
    {
        return true
    }

    /**
     * @return False since we can't use e to close the GUI screen
     */
    override fun inventoryToCloseGuiScreen(): Boolean
    {
        return !this.nameSignField.isFocused
    }
}