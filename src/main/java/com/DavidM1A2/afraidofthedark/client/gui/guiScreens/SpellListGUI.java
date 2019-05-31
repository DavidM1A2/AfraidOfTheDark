package com.DavidM1A2.afraidofthedark.client.gui.guiScreens;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiScreen;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDKeyListener;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.specialControls.AOTDGuiSpell;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.*;
import com.DavidM1A2.afraidofthedark.client.keybindings.KeybindingUtils;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModSounds;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;

import java.util.ArrayList;
import java.util.List;

/**
 * Spell selection/list gui allows players to create spells and keybind them
 */
public class SpellListGUI extends AOTDGuiScreen
{
    // The distance between spell entries in pixels
    private static final int DISTANCE_BETWEEN_SPELLS = 45;

    // The scroll panel that holds the spell list
    private final AOTDGuiScrollPanel scrollPanel;
    // The button used to create more spells
    private final AOTDGuiButton btnCreateSpell;
    // A list of spells to be shown
    private List<AOTDGuiSpell> guiSpells = new ArrayList<>();
    // Cache the player's spell manager
    private IAOTDPlayerSpellManager spellManager = entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null);
    // Flag telling us if we are currently waiting on a keybind to be pressed or not
    private AOTDGuiSpell spellWaitingOnKeybind = null;

    /**
     * Constructor initializes the gui elements
     */
    public SpellListGUI()
    {
        // The gui will be 256x256
        final int GUI_WIDTH = 256;
        final int GUI_HEIGHT = 256;

        // Calculate the x,y base position of the UI
        int xPosSpellList = (Constants.GUI_WIDTH - GUI_WIDTH) / 2;
        int yPosSpellList = (Constants.GUI_HEIGHT - GUI_HEIGHT) / 2;

        // Place the background panel in the center
        AOTDGuiPanel backgroundPanel = new AOTDGuiPanel(xPosSpellList, yPosSpellList, GUI_WIDTH, GUI_HEIGHT, false);

        // Constants for scroll bar width and padding
        final int SCROLL_BAR_WIDTH = 15;
        final int SCROLL_BAR_HORIZONTAL_PADDING = 5;

        // Create a magic mirror background image
        AOTDGuiImage mirrorBackgroundImage = new AOTDGuiImage(0, 0, GUI_WIDTH - SCROLL_BAR_WIDTH - SCROLL_BAR_HORIZONTAL_PADDING * 2, GUI_HEIGHT, "afraidofthedark:textures/gui/spell_list/spell_list_background.png");
        backgroundPanel.add(mirrorBackgroundImage);

        // Compute the scroll bar's x and y position
        int scrollBarX = mirrorBackgroundImage.getWidth() + SCROLL_BAR_HORIZONTAL_PADDING;
        int scrollBarY = 0;
        // Create the scroll bar
        AOTDGuiScrollBar scrollBar = new AOTDGuiScrollBar(
                scrollBarX,
                scrollBarY,
                GUI_WIDTH - mirrorBackgroundImage.getWidth() - SCROLL_BAR_HORIZONTAL_PADDING * 2,
                GUI_HEIGHT,
                "afraidofthedark:textures/gui/spell_list/scroll_bar.png",
                "afraidofthedark:textures/gui/spell_list/scroll_bar_handle.png",
                "afraidofthedark:textures/gui/spell_list/scroll_bar_handle_hovered.png");

        // Create the scroll panel to add spells to, position it centered on the background image
        scrollPanel = new AOTDGuiScrollPanel(28, 8, 175, 238, true, scrollBar);
        // Start with a max offset of 0
        scrollPanel.setMaximumOffset(0);
        // Add the panel the the background and the scroll bar
        backgroundPanel.add(scrollPanel);
        backgroundPanel.add(scrollBar);

        // When we press a key test if we are waiting for a keybind, if so set the spell's keybind
        this.getContentPane().addKeyListener(new AOTDKeyListener()
        {
            @Override
            public void keyTyped(AOTDKeyEvent event)
            {
                // If we're waiting on a keybind assign the keybind and update each spell
                if (spellWaitingOnKeybind != null)
                {
                    // Test if the key down is bindable
                    if (KeybindingUtils.keybindableKeyDown())
                    {
                        // Grab the keybind being held
                        String keybind = KeybindingUtils.getCurrentlyHeldKeybind();
                        // Keybind the spell
                        spellManager.keybindSpell(keybind, spellWaitingOnKeybind.getSpell());
                        // Update all gui spell's labels
                        guiSpells.forEach(AOTDGuiSpell::refreshLabels);
                        // We're no longer waiting on a keybind
                        spellWaitingOnKeybind = null;
                    }
                }
            }
        });

        // Add a button to create a new spell, center it under the scrollPanel spell entries
        btnCreateSpell = new AOTDGuiButton(scrollPanel.getWidth() / 2 - 13, 0, 26, 26, null, "afraidofthedark:textures/gui/spell_list/create_spell.png", "afraidofthedark:textures/gui/spell_list/create_spell_hovered.png");
        btnCreateSpell.setHoverText("Create a new spell");
        btnCreateSpell.addMouseListener(new AOTDMouseListener()
        {
            @Override
            public void mousePressed(AOTDMouseEvent event)
            {
                // When we click the button create a new spell
                if (event.getSource().isVisible() && event.getSource().isHovered() && event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Left)
                {
                    // Create a new spell
                    Spell spell = new Spell(entityPlayer);
                    // Add the spell
                    spellManager.addOrUpdateSpell(spell);
                    // Add the UI spell
                    addSpell(spell);
                }
            }

            @Override
            public void mouseEntered(AOTDMouseEvent event)
            {
                // Play the button hover when hovering the add button
                entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f);
            }
        });
        scrollPanel.add(btnCreateSpell);

        // Go over each spell the player has and add a gui spell for it
        spellManager.getSpells().forEach(this::addSpell);

        this.getContentPane().add(backgroundPanel);
    }

    /**
     * Adds a gui spell for a given spell into the list
     *
     * @param spell The spell to create a GUI spell for
     */
    private void addSpell(Spell spell)
    {
        // Create a gui spell for this spell
        AOTDGuiSpell guiSpell = new AOTDGuiSpell(0, this.guiSpells.size() * DISTANCE_BETWEEN_SPELLS, 175, 40, spell);
        // When delete is pressed remove the GUI spell
        guiSpell.setDeleteCallback(() -> removeSpell(guiSpell));
        // When keybind is pressed update our variable to let us know we're waiting on a keybind, or clear the keybind
        guiSpell.setKeybindCallback(() ->
        {
            // No keybind exists, expect one now
            if (spellManager.getKeybindingForSpell(spell) == null)
            {
                spellWaitingOnKeybind = guiSpell;
            }
            // Keybind exists, so unbind the spell and refresh labels
            else
            {
                spellManager.unbindSpell(spell);
                guiSpell.refreshLabels();
            }
        });
        // Add the gui spell to the panel
        this.scrollPanel.add(guiSpell);
        // Add the gui spell to the list of spells for later use
        this.guiSpells.add(guiSpell);
        // Move our create spell button down
        this.btnCreateSpell.setY(this.btnCreateSpell.getY() + DISTANCE_BETWEEN_SPELLS);
        // Update our scroll panel offset
        this.refreshScrollPanelOffset();
    }

    /**
     * Removes a gui spell from the list and also removes the spell from the spell manager
     *
     * @param spell The spell to remove
     */
    private void removeSpell(AOTDGuiSpell spell)
    {
        // Find the index of this gui spell
        int index = this.guiSpells.indexOf(spell);
        // Remove the spell at the index
        this.guiSpells.remove(index);
        // Remove the spell from the scroll panel
        this.scrollPanel.remove(spell);
        // Remove the spell from the spell manager
        this.spellManager.deleteSpell(spell.getSpell());
        // Go over all spells after this one and move them up one slot
        for (int i = index; i < this.guiSpells.size(); i++)
        {
            AOTDGuiSpell guiSpell = this.guiSpells.get(i);
            guiSpell.setY(guiSpell.getY() - DISTANCE_BETWEEN_SPELLS);
        }
        // Move our create spell button up
        this.btnCreateSpell.setY(this.btnCreateSpell.getY() - DISTANCE_BETWEEN_SPELLS);
        // Update our scroll panel offset
        this.refreshScrollPanelOffset();
    }

    /**
     * Updates the scroll panel offset based on the gui spells present
     */
    private void refreshScrollPanelOffset()
    {
        // If there's more than 4 spells update the maximum offset so we can scroll over spells
        if (this.guiSpells.size() > 4)
        {
            this.scrollPanel.setMaximumOffset((this.guiSpells.size() - 4) * DISTANCE_BETWEEN_SPELLS);
        }
        // If there's less than 4 spells don't allow scrolling
        else
        {
            this.scrollPanel.setMaximumOffset(0);
        }
    }

    /**
     * When the GUI is closed sync the player's spell manager
     */
    @Override
    public void onGuiClosed()
    {
        spellManager.syncAll(entityPlayer);
    }

    /**
     * @return True, the inventory key closes the screen
     */
    @Override
    public boolean inventoryToCloseGuiScreen()
    {
        return true;
    }

    /**
     * @return True, the screen should have a gradient background
     */
    @Override
    public boolean drawGradientBackground()
    {
        return true;
    }
}
