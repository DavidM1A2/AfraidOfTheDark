package com.DavidM1A2.afraidofthedark.client.gui.guiScreens;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiScreen;
import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDKeyListener;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiButton;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiTextField;
import com.DavidM1A2.afraidofthedark.client.settings.ClientData;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.constants.ModSounds;
import com.DavidM1A2.afraidofthedark.common.item.ItemJournal;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.util.Color;

/**
 * Class used to create a blood stained journal signing UI
 */
public class BloodStainedJournalSignGUI extends AOTDGuiScreen
{
    // The text field that you sign your name in
    private final AOTDGuiTextField nameSignField;

    /**
     * Constructor adds any required components to the sign UI
     */
    public BloodStainedJournalSignGUI()
    {
        super();

        final int GUI_SIZE = 256;
        // Setup the background panel that holds all of our controls
        AOTDGuiPanel backgroundPanel = new AOTDGuiPanel(
                (Constants.GUI_WIDTH - GUI_SIZE) / 2,
                (Constants.GUI_HEIGHT - GUI_SIZE) / 2,
                GUI_SIZE,
                GUI_SIZE,
                false);

        // Add a background image to the background panel
        final int BACKGROUND_IMAGE_SIZE = 220;
        AOTDGuiImage backgroundImage = new AOTDGuiImage(
                (GUI_SIZE - BACKGROUND_IMAGE_SIZE) / 2,
                0,
                BACKGROUND_IMAGE_SIZE,
                BACKGROUND_IMAGE_SIZE,
                "afraidofthedark:textures/gui/journal_sign/blood_stained_journal.png");
        backgroundPanel.add(backgroundImage);

        this.nameSignField = new AOTDGuiTextField(
                45,
                90,
                160,
                30,
                ClientData.getInstance().getTargaMSHandFontSized(45f));
        this.nameSignField.setTextColor(new Color(255, 0, 0));
        backgroundPanel.add(this.nameSignField);

        // Add the sign button
        final int SIGN_BUTTON_WIDTH = 100;
        final int SIGN_BUTTON_HEIGHT = 25;
        AOTDGuiButton signButton = new AOTDGuiButton(
                GUI_SIZE / 2 - SIGN_BUTTON_WIDTH / 2,
                GUI_SIZE - 30,
                SIGN_BUTTON_WIDTH,
                SIGN_BUTTON_HEIGHT,
                ClientData.getInstance().getTargaMSHandFontSized(55f),
                "afraidofthedark:textures/gui/journal_sign/sign_button.png",
                "afraidofthedark:textures/gui/journal_sign/sign_button_hovered.png");
        signButton.setText("Sign");
        signButton.setTextColor(new Color(255, 0, 0));
        signButton.setTextAlignment(TextAlignment.ALIGN_CENTER);
        // When we click the sign button either start the mod or tell the user they messed up
        signButton.addMouseListener(new AOTDMouseListener()
        {
            @Override
            public void mouseClicked(AOTDMouseEvent event)
            {
                if (event.getSource().isHovered() && event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Left)
                {
                    entityPlayer.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    IAOTDPlayerBasics playerBasics = entityPlayer.getCapability(ModCapabilities.PLAYER_BASICS, null);
                    IAOTDPlayerResearch playerResearch = entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
                    if (BloodStainedJournalSignGUI.this.nameSignField.getText().equals(entityPlayer.getDisplayName().getUnformattedText()))
                    {
                        // if the name is correct start the mod
                        if (!playerBasics.getStartedAOTD())
                        {
                            // We now started the mod

                            // Set that we started the mod and perform a client -> server sync
                            playerBasics.setStartedAOTD(true);
                            playerBasics.syncStartedAOTD(entityPlayer);
                            playerResearch.setResearchAndAlert(ModResearches.AN_UNBREAKABLE_COVENANT, true, entityPlayer);
                            playerResearch.setResearchAndAlert(ModResearches.CROSSBOW, true, entityPlayer);
                            playerResearch.sync(entityPlayer, false);

                            // Set the journal to have a new owner name
                            ItemStack mainHand = entityPlayer.getHeldItemMainhand();
                            ItemStack offHand = entityPlayer.getHeldItemOffhand();
                            // We must check both off hand and main hand since the journal could be in either hand
                            if (mainHand.getItem() instanceof ItemJournal)
                            {
                                ((ItemJournal) mainHand.getItem()).setOwner(mainHand, entityPlayer.getDisplayName().getUnformattedText());
                            }
                            else if (offHand.getItem() instanceof ItemJournal)
                            {
                                ((ItemJournal) offHand.getItem()).setOwner(offHand, entityPlayer.getDisplayName().getUnformattedText());
                            }

                            // Play the sign animation and chat message
                            entityPlayer.playSound(ModSounds.JOURNAL_SIGN, 4.0F, 1.0F);
                            entityPlayer.sendMessage(new TextComponentTranslation("aotd.journal.sign.successful"));
                        }
                    }
                    else
                    {
                        // Test if the user has not yet started AOTD
                        if (!playerBasics.getStartedAOTD())
                        {
                            // If he has not started then print out a message that the name was wrong
                            entityPlayer.sendMessage(new TextComponentTranslation("aotd.journal.sign.unsuccessful"));
                            entityPlayer.closeScreen();
                        }
                    }
                    entityPlayer.closeScreen();
                }
                event.consume();
            }

            @Override
            public void mouseEntered(AOTDMouseEvent event)
            {
                entityPlayer.playSound(ModSounds.BUTTON_HOVER, 0.1f, 0.8f);
            }
        });
        // When we type a character play a type sound
        this.nameSignField.addKeyListener(new AOTDKeyListener()
        {
            @Override
            public void keyTyped(AOTDKeyEvent event)
            {
                if (BloodStainedJournalSignGUI.this.nameSignField.isFocused())
                {
                    entityPlayer.playSound(ModSounds.KEY_TYPED, 0.4f, 0.8f);
                    event.consume();
                }
            }
        });
        backgroundPanel.add(signButton);

        // Add the background panel to the content pane
        this.getContentPane().add(backgroundPanel);
    }

    /**
     * @return True because we want the background to be a gray
     */
    @Override
    public boolean drawGradientBackground()
    {
        return true;
    }

    /**
     * @return False since we can't use e to close the GUI screen
     */
    @Override
    public boolean inventoryToCloseGuiScreen()
    {
        return !this.nameSignField.isFocused();
    }
}
