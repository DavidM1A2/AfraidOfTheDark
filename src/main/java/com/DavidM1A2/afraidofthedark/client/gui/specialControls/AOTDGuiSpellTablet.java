package com.DavidM1A2.afraidofthedark.client.gui.specialControls;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDKeyListener;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.*;
import com.DavidM1A2.afraidofthedark.client.settings.ClientData;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModSounds;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.SpellStage;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the tablet used in the spell crafting gui on the left
 */
public class AOTDGuiSpellTablet extends AOTDGuiContainer
{
    // The text field containing the spell's name
    private final AOTDGuiTextField spellName;
    // The scroll panel containing the spell's stages
    private final AOTDGuiScrollPanel spellStagePanel;
    // The spell being edited
    private final Spell spell;
    // A list of spell stages that this spell has
    private final List<AOTDGuiSpellStage> spellStages = new ArrayList<>();
    // The spell's power source
    private final AOTDGuiSpellPowerSourceSlot powerSource;
    // A label to show the spell's cost
    private final AOTDGuiLabel spellCost;

    // Listeners for buttons
    private Runnable onHelp;

    /**
     * Constructor initializes this component's dimensions
     *
     * @param x      The x position of the tablet
     * @param y      The y position of the tablet
     * @param width  The width of the tablet
     * @param height The height of the tablet
     */
    public AOTDGuiSpellTablet(int x, int y, int width, int height, Spell spell)
    {
        super(x, y, width, height);
        this.spell = spell;

        // A base panel to contain all tablet gui controls
        AOTDGuiPanel tablet = new AOTDGuiPanel(0, 0, width, height, false);

        // Create the background image
        AOTDGuiImage backgroundImage = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spell_editor/tablet_background.png");
        tablet.add(backgroundImage);

        // Setup the spell name label
        this.spellName = new AOTDGuiTextField(60, 30, 85, 25, ClientData.getInstance().getTargaMSHandFontSized(36f));
        this.spellName.setGhostText("Spell Name");
        // When we type into this slot set the spell name
        this.spellName.addKeyListener(new AOTDKeyListener()
        {
            @Override
            public void keyTyped(AOTDKeyEvent event)
            {
                AOTDGuiSpellTablet.this.spell.setName(spellName.getText());
            }
        });
        tablet.add(spellName);

        // Add a scroll bar on the left to scroll through spell stages
        AOTDGuiScrollBar scrollBar = new AOTDGuiScrollBar(10, 75, 15, 170);
        tablet.add(scrollBar);

        // Add the background for the spell stages
        AOTDGuiImage spellStageBackground = new AOTDGuiImage(30, 55, 120, 170, "afraidofthedark:textures/gui/spell_editor/spell_stage_panel_background.png");
        tablet.add(spellStageBackground);

        // Add the spell stage container scroll panel
        this.spellStagePanel = new AOTDGuiScrollPanel(30, 55, 120, 170, true, scrollBar);
        tablet.add(spellStagePanel);

        // Create a save spell button
        AOTDGuiButton saveButton = new AOTDGuiButton(152, 105, 20, 20, null, "afraidofthedark:textures/gui/spell_editor/save.png", "afraidofthedark:textures/gui/spell_editor/save_hovered.png");
        saveButton.setHoverText("Save Spell");
        saveButton.addMouseListener(new AOTDMouseListener()
        {
            @Override
            public void mousePressed(AOTDMouseEvent event)
            {
                // When we press the save spell button and it's hovered save the spell and send changes to server
                if (event.getSource().isVisible() && event.getSource().isHovered() && event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Left)
                {
                    // Grab the player's spell manager
                    IAOTDPlayerSpellManager spellManager = entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null);
                    // Clone the spell so we don't modify it further
                    Spell spellClone = new Spell(AOTDGuiSpellTablet.this.spell.serializeNBT());
                    // Update the spell
                    spellManager.addOrUpdateSpell(spellClone);
                    // Sync the spell server side
                    spellManager.sync(entityPlayer, spellClone);
                    // Tell the player the save was successful
                    entityPlayer.sendMessage(new TextComponentTranslation("aotd.spell.save_successful", spellClone.getName()));
                }
            }

            @Override
            public void mouseEntered(AOTDMouseEvent event)
            {
                // When hovering the button play the hover sound
                if (event.getSource().isHovered() && event.getSource().isVisible())
                {
                    entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f);
                }
            }
        });
        tablet.add(saveButton);

        // Create a close UI and don't save button
        AOTDGuiButton closeButton = new AOTDGuiButton(152, 130, 20, 20, null, "afraidofthedark:textures/gui/spell_editor/delete.png", "afraidofthedark:textures/gui/spell_editor/delete_hovered.png");
        closeButton.setHoverText("Exit without saving");
        // When we click the close button show the spell list
        closeButton.addMouseListener(new AOTDMouseListener()
        {
            @Override
            public void mousePressed(AOTDMouseEvent event)
            {
                // Ensure the button is visible and hovered
                if (closeButton.isVisible() && closeButton.isHovered() && event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Left)
                {
                    // Open the list gui without saving
                    entityPlayer.openGui(AfraidOfTheDark.INSTANCE, AOTDGuiHandler.SPELL_LIST_ID, entityPlayer.world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
                }
            }

            @Override
            public void mouseEntered(AOTDMouseEvent event)
            {
                // Play the hover sound effect if the button is visible
                if (closeButton.isHovered() && closeButton.isVisible())
                {
                    entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f);
                }
            }
        });
        tablet.add(closeButton);

        // Create a help button
        AOTDGuiButton helpButton = new AOTDGuiButton(152, 180, 20, 20, null, "afraidofthedark:textures/gui/spell_editor/question.png", "afraidofthedark:textures/gui/spell_editor/question_hovered.png");
        helpButton.setHoverText("Help");
        // When pressing help execute our on help runnable
        helpButton.addMouseListener(new AOTDMouseListener()
        {
            @Override
            public void mousePressed(AOTDMouseEvent event)
            {
                // Ensure the button is visible, hovered, and the callback is non-null
                if (helpButton.isVisible() && helpButton.isHovered() && event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Left)
                {
                    if (onHelp != null)
                    {
                        onHelp.run();
                    }
                }
            }

            @Override
            public void mouseEntered(AOTDMouseEvent event)
            {
                // Play the hover sound effect if the button is visible
                if (event.getSource().isHovered() && event.getSource().isVisible())
                {
                    entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f);
                }
            }
        });
        tablet.add(helpButton);

        // Create the power source spell slot
        this.powerSource = new AOTDGuiSpellPowerSourceSlot(152, 155, 20, 20, null);
        this.powerSource.setComponentInstance(this.spell.getPowerSource());
        tablet.add(powerSource);

        // Add the spell cost label
        this.spellCost = new AOTDGuiLabel(30, 225, 120, 20, ClientData.getInstance().getTargaMSHandFontSized(32f));
        tablet.add(spellCost);

        // Update all the gui components from our spell
        this.refresh();

        this.add(tablet);
    }

    /**
     * Refreshes the state of the tablet based on the current spell
     */
    private void refresh()
    {
        // Update the spell cost label
        this.spellCost.setText("Cost: " + Math.round(this.spell.getCost()));
        // Update the power source instance
        this.powerSource.setComponentInstance(this.spell.getPowerSource());
        // Update the spell's name
        this.spellName.setText(this.spell.getName());
        // Remove all existing spell stages
        while (!this.spellStages.isEmpty())
        {
            this.removeLastGuiSpellStage();
        }
        // Add one gui spell stage for each stage
        this.spell.getSpellStages().forEach(this::addGuiSpellStage);
        // Refresh each gui stage
        this.spellStages.forEach(AOTDGuiSpellStage::refresh);
        // Update the amount of scroll based on the number of spell stages that exist
        this.updateScrollOffset();
    }

    /**
     * Adds a gui spell stage to the UI
     *
     * @param spellStage The stage to create a UI component for
     */
    private void addGuiSpellStage(SpellStage spellStage)
    {
        // Create the GUI spell stage
        AOTDGuiSpellStage nextSpellStage = new AOTDGuiSpellStage(5, (5 + this.spellStages.size() * 35), 110, 45, spellStage);
        // If we click add then add a spell stage and refresh this UI component
        nextSpellStage.setOnAdd(() ->
        {
            this.spell.getSpellStages().add(new SpellStage());
            this.refresh();
        });
        // If we click remove then the last spell stage and refresh this UI component
        nextSpellStage.setOnRemove(() ->
        {
            this.spell.getSpellStages().remove(this.spell.getSpellStages().size() - 1);
            this.refresh();
        });
        // Add the spell stage to the panel
        this.spellStagePanel.add(nextSpellStage);
        //Add the spell stage to the stage list
        this.spellStages.add(nextSpellStage);
        // If only 1 spell stage exists hide the delete button
        if (this.spellStages.size() == 1)
        {
            nextSpellStage.hideMinus();
        }
        // If more than one spell stage exist hide their +/- buttons
        else
        {
            for (int i = 0; i < this.spellStages.size() - 1; i++)
            {
                AOTDGuiSpellStage otherSpellStage = this.spellStages.get(i);
                otherSpellStage.hideMinus();
                otherSpellStage.hidePlus();
            }
        }
    }

    /**
     * Removes the last GUI spell stage from the list
     */
    private void removeLastGuiSpellStage()
    {
        // If there is a spell stage to remove...
        if (!this.spellStages.isEmpty())
        {
            // Grab the last spell stage in the list to remove
            AOTDGuiSpellStage lastStage = this.spellStages.get(this.spellStages.size() - 1);
            // Remove the stage from the panel and list of stages
            this.spellStagePanel.remove(lastStage);
            this.spellStages.remove(lastStage);

            // If any spell stages remain update the last one
            if (!this.spellStages.isEmpty())
            {
                // Grab the new last spell stage
                AOTDGuiSpellStage secondToLastStage = this.spellStages.get(this.spellStages.size() - 1);
                // Always show the + button
                secondToLastStage.showPlus();
                // Show the minus if this isn't the last spell stage
                if (this.spellStages.size() != 1)
                {
                    secondToLastStage.showMinus();
                }
            }
        }
    }

    /**
     * Updates the scroll offset of the panel based on the number of spell stages
     */
    private void updateScrollOffset()
    {
        this.spellStagePanel.setMaximumOffset(this.spellStages.size() > 4 ? (this.spellStages.size() - 4) * 35 : 0);
    }

    /**
     * Sets the code that should be executed when help is pressed
     *
     * @param onHelp The code to run when help is pressed
     */
    public void setOnHelp(Runnable onHelp)
    {
        this.onHelp = onHelp;
    }

    /**
     * @return True if the inventory key should currently close the UI, false otherwise
     */
    public boolean inventoryKeyClosesUI()
    {
        return !this.spellName.isFocused();
    }
}
