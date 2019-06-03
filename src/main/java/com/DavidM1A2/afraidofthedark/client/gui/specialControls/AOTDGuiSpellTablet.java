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
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
    private final List<AOTDGuiSpellStage> uiSpellStages = new ArrayList<>();
    // The spell's power source
    private final AOTDGuiSpellPowerSourceSlot uiPowerSource;
    // A label to show the spell's cost
    private final AOTDGuiLabel spellCost;

    // Listeners for buttons
    private Runnable onHelp;

    // A special supplier that gets the currently selected component
    private final Supplier<AOTDGuiSpellComponentSlot<?, ?>> selectedComponentGetter;
    // A special runnable that tells the crafting UI to clear it's currently selected spell component
    private final Runnable clearSelectedComponent;

    /**
     * Constructor initializes this component's dimensions
     *
     * @param x                       The x position of the tablet
     * @param y                       The y position of the tablet
     * @param width                   The width of the tablet
     * @param height                  The height of the tablet
     * @param spell                   The spell that this tablet represents
     * @param selectedComponentGetter The getter used in getting the current spell component
     */
    public AOTDGuiSpellTablet(int x, int y, int width, int height, Spell spell, Supplier<AOTDGuiSpellComponentSlot<?, ?>> selectedComponentGetter, Runnable clearSelectedComponent)
    {
        super(x, y, width, height);
        this.spell = spell;
        this.selectedComponentGetter = selectedComponentGetter;
        this.clearSelectedComponent = clearSelectedComponent;

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
        this.uiPowerSource = new AOTDGuiSpellPowerSourceSlot(152, 155, 20, 20, null);
        this.uiPowerSource.setComponentInstance(this.spell.getPowerSource());
        // When we click the power source check the selected component, if it's a power source perform additional updates
        this.uiPowerSource.addMouseListener(new AOTDMouseListener()
        {
            @Override
            public void mouseReleased(AOTDMouseEvent event)
            {
                // Test if we're hovering over the component...
                if (uiPowerSource.isHovered() && uiPowerSource.isVisible())
                {
                    // If we left click update the power source
                    if (event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Left)
                    {
                        // Grab the selected component
                        AOTDGuiSpellComponentSlot<?, ?> selectedComponent = selectedComponentGetter.get();
                        // If it's a spell power slot update the spell's power source
                        if (selectedComponent instanceof AOTDGuiSpellPowerSourceSlot)
                        {
                            // Grab the selected power source
                            AOTDGuiSpellPowerSourceSlot selectedPowerSource = (AOTDGuiSpellPowerSourceSlot) selectedComponent;
                            // Unhighlight the power source UI element
                            uiPowerSource.setHighlight(false);
                            // Create a new instance of the selected power source
                            SpellPowerSource spellPowerSource = selectedPowerSource.getComponentType().newInstance();
                            // Update the slot and spell
                            spell.setPowerSource(spellPowerSource);
                            uiPowerSource.setComponentInstance(spellPowerSource);
                            // Clear the selected component
                            clearSelectedComponent.run();
                        }
                    }
                    // If we right click set the slot's power source to null
                    else if (event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Right)
                    {
                        spell.setPowerSource(null);
                        uiPowerSource.setComponentType(null);
                    }
                }
            }

            @Override
            public void mouseEntered(AOTDMouseEvent event)
            {
                // When we hover the power source with a selected component "in hand" highlight it
                AOTDGuiSpellComponentSlot<?, ?> selectedComponent = selectedComponentGetter.get();
                if (selectedComponent instanceof AOTDGuiSpellPowerSourceSlot)
                {
                    uiPowerSource.setHighlight(true);
                }
            }

            @Override
            public void mouseExited(AOTDMouseEvent event)
            {
                // When we unhover the power source with a selected component "in hand" de-highlight it
                AOTDGuiSpellComponentSlot<?, ?> spellComponentSlot = selectedComponentGetter.get();
                if (spellComponentSlot instanceof AOTDGuiSpellPowerSourceSlot)
                {
                    uiPowerSource.setHighlight(false);
                }
            }
        });
        tablet.add(uiPowerSource);

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
        this.uiPowerSource.setComponentInstance(this.spell.getPowerSource());
        // Update the spell's name
        this.spellName.setText(this.spell.getName());
        // Remove all existing spell stages
        while (!this.uiSpellStages.isEmpty())
        {
            this.removeLastGuiSpellStage();
        }
        // Add one gui spell stage for each stage
        this.spell.getSpellStages().forEach(this::addGuiSpellStage);
        // Refresh each gui stage
        this.uiSpellStages.forEach(AOTDGuiSpellStage::refresh);
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
        AOTDGuiSpellStage nextSpellStage = new AOTDGuiSpellStage(5, (5 + this.uiSpellStages.size() * 35), 110, 45, spellStage);
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
        // When we click the delivery method check the selected component, if it's a delivery method perform additional updates
        AOTDGuiSpellDeliveryMethodSlot uiDeliveryMethod = nextSpellStage.getDeliveryMethod();
        uiDeliveryMethod.addMouseListener(new AOTDMouseListener()
        {
            @Override
            public void mouseReleased(AOTDMouseEvent event)
            {
                // Test if we're hovering over the component...
                if (uiDeliveryMethod.isHovered() && uiDeliveryMethod.isVisible())
                {
                    // If we left click update the delivery method
                    if (event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Left)
                    {
                        // Grab the selected component
                        AOTDGuiSpellComponentSlot<?, ?> selectedComponent = selectedComponentGetter.get();
                        // If it's a delivery method slot update the spell stage's delivery method
                        if (selectedComponent instanceof AOTDGuiSpellDeliveryMethodSlot)
                        {
                            // Grab the selected delivery method
                            AOTDGuiSpellDeliveryMethodSlot selectedDeliveryMethod = (AOTDGuiSpellDeliveryMethodSlot) selectedComponent;
                            // Unhighlight the delivery method UI element
                            uiDeliveryMethod.setHighlight(false);
                            // Create a new instance of the selected delivery method
                            SpellDeliveryMethod spellDeliveryMethod = selectedDeliveryMethod.getComponentType().newInstance();
                            // Update the slot and spell
                            spellStage.setDeliveryMethod(spellDeliveryMethod);
                            uiDeliveryMethod.setComponentInstance(spellDeliveryMethod);
                            // Clear the selected component
                            clearSelectedComponent.run();
                        }
                    }
                    // If we right click set the slot's delivery method to null
                    else if (event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Right)
                    {
                        spellStage.setDeliveryMethod(null);
                        uiDeliveryMethod.setComponentType(null);
                    }
                }
            }

            @Override
            public void mouseEntered(AOTDMouseEvent event)
            {
                // When we hover the delivery method with a selected component "in hand" highlight it
                AOTDGuiSpellComponentSlot<?, ?> selectedComponent = selectedComponentGetter.get();
                if (selectedComponent instanceof AOTDGuiSpellDeliveryMethodSlot)
                {
                    uiDeliveryMethod.setHighlight(true);
                }
            }

            @Override
            public void mouseExited(AOTDMouseEvent event)
            {
                // When we unhover the delivery method with a selected component "in hand" de-highlight it
                AOTDGuiSpellComponentSlot<?, ?> spellComponentSlot = selectedComponentGetter.get();
                if (spellComponentSlot instanceof AOTDGuiSpellDeliveryMethodSlot)
                {
                    uiDeliveryMethod.setHighlight(false);
                }
            }
        });
        // When we click the any of the effect slots check the selected component, if it's an effect perform additional updates
        for (int i = 0; i < nextSpellStage.getEffects().length; i++)
        {
            AOTDGuiSpellEffectSlot uiEffect = nextSpellStage.getEffects()[i];
            int index = i;
            uiEffect.addMouseListener(new AOTDMouseListener()
            {
                @Override
                public void mouseReleased(AOTDMouseEvent event)
                {
                    // Test if we're hovering over the component...
                    if (uiEffect.isHovered() && uiEffect.isVisible())
                    {
                        // If we left click update the effect
                        if (event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Left)
                        {
                            // Grab the selected component
                            AOTDGuiSpellComponentSlot<?, ?> selectedComponent = selectedComponentGetter.get();
                            // If it's an effect slot update the spell stage's effect method
                            if (selectedComponent instanceof AOTDGuiSpellEffectSlot)
                            {
                                // Grab the selected effect
                                AOTDGuiSpellEffectSlot selectedEffect = (AOTDGuiSpellEffectSlot) selectedComponent;
                                // Unhighlight the delivery method UI element
                                uiEffect.setHighlight(false);
                                // Create a new instance of the selected effect
                                SpellEffect spellEffect = selectedEffect.getComponentType().newInstance();
                                // Update the slot and spell
                                spellStage.getEffects()[index] = spellEffect;
                                uiEffect.setComponentInstance(spellEffect);
                                // Clear the selected component
                                clearSelectedComponent.run();
                            }
                        }
                        // If we right click set the slot's effect to null
                        else if (event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Right)
                        {
                            spellStage.getEffects()[index] = null;
                            uiEffect.setComponentType(null);
                        }
                    }
                }

                @Override
                public void mouseEntered(AOTDMouseEvent event)
                {
                    // When we hover the effect with a selected component "in hand" highlight it
                    AOTDGuiSpellComponentSlot<?, ?> selectedComponent = selectedComponentGetter.get();
                    if (selectedComponent instanceof AOTDGuiSpellEffectSlot)
                    {
                        uiEffect.setHighlight(true);
                    }
                }

                @Override
                public void mouseExited(AOTDMouseEvent event)
                {
                    // When we unhover the effect with a selected component "in hand" de-highlight it
                    AOTDGuiSpellComponentSlot<?, ?> spellComponentSlot = selectedComponentGetter.get();
                    if (spellComponentSlot instanceof AOTDGuiSpellEffectSlot)
                    {
                        uiEffect.setHighlight(false);
                    }
                }
            });
        }
        // Add the spell stage to the panel
        this.spellStagePanel.add(nextSpellStage);
        //Add the spell stage to the stage list
        this.uiSpellStages.add(nextSpellStage);
        // If only 1 spell stage exists hide the delete button
        if (this.uiSpellStages.size() == 1)
        {
            nextSpellStage.hideMinus();
        }
        // If more than one spell stage exist hide their +/- buttons
        else
        {
            for (int i = 0; i < this.uiSpellStages.size() - 1; i++)
            {
                AOTDGuiSpellStage otherSpellStage = this.uiSpellStages.get(i);
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
        if (!this.uiSpellStages.isEmpty())
        {
            // Grab the last spell stage in the list to remove
            AOTDGuiSpellStage lastStage = this.uiSpellStages.get(this.uiSpellStages.size() - 1);
            // Remove the stage from the panel and list of stages
            this.spellStagePanel.remove(lastStage);
            this.uiSpellStages.remove(lastStage);

            // If any spell stages remain update the last one
            if (!this.uiSpellStages.isEmpty())
            {
                // Grab the new last spell stage
                AOTDGuiSpellStage secondToLastStage = this.uiSpellStages.get(this.uiSpellStages.size() - 1);
                // Always show the + button
                secondToLastStage.showPlus();
                // Show the minus if this isn't the last spell stage
                if (this.uiSpellStages.size() != 1)
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
        this.spellStagePanel.setMaximumOffset(this.uiSpellStages.size() > 4 ? (this.uiSpellStages.size() - 4) * 35 : 0);
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
