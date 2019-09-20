package com.davidm1a2.afraidofthedark.client.gui.specialControls;

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiButton;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.davidm1a2.afraidofthedark.common.constants.ModSounds;
import com.davidm1a2.afraidofthedark.common.spell.SpellStage;

/**
 * Class representing the gui version of a spell stage
 */
public class AOTDGuiSpellStage extends AOTDGuiContainer
{
    // Two buttons, one for adding new spell stages and one for removing spell stages
    private final AOTDGuiButton addNewRow;
    private final AOTDGuiButton removeRow;
    // The delivery method of this spell stage
    private final AOTDGuiSpellDeliveryMethodSlot deliveryMethod;
    // The 4 effects of this spell stage
    private final AOTDGuiSpellEffectSlot[] effects;
    // The spell stage reference
    private final SpellStage spellStage;
    // Two runnables, one to be fired when add is called, and one to be fired when remove is called
    private Runnable addRunnable;
    private Runnable removeRunnable;

    /**
     * Constructor initializes the bounding box
     *
     * @param x          The X location of the top left corner
     * @param y          The Y location of the top left corner
     * @param width      The width of the component
     * @param height     The height of the component
     * @param spellStage The spell stage that this gui element represents
     */
    public AOTDGuiSpellStage(Integer x, Integer y, Integer width, Integer height, SpellStage spellStage)
    {
        super(x, y, width, height);
        this.spellStage = spellStage;

        // Set the background texture of the spell stage, save 14px for add and remove buttons
        AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height - 14, "afraidofthedark:textures/gui/spell_editor/spell_stage_background.png");
        this.add(background);

        // The amount of space between effect/delivery slots
        final int SLOT_SPACING = 20;

        // Create the delivery method slot
        this.deliveryMethod = new AOTDGuiSpellDeliveryMethodSlot(5, 5, height - 25, height - 25, null);
        this.add(deliveryMethod);

        // Create a slot for each effect
        this.effects = new AOTDGuiSpellEffectSlot[4];
        for (int i = 0; i < effects.length; i++)
        {
            // Place the slot at an offset based on the spacing
            this.effects[i] = new AOTDGuiSpellEffectSlot(5 + SLOT_SPACING * (i + 1), 5, height - 25, height - 25, null);
            this.add(effects[i]);
        }

        // Create two buttons, one to add a new row and one to remove the current row
        this.addNewRow = new AOTDGuiButton(0, height - 15, 15, 15, null, "afraidofthedark:textures/gui/spell_editor/add.png", "afraidofthedark:textures/gui/spell_editor/add_hovered.png");
        this.addNewRow.setHoverText("Add new spell stage");
        this.addNewRow.addMouseListener(event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Press)
            {
                // If we press the button and it's visible/hovered run the callback
                if (addNewRow.isHovered() && addNewRow.isVisible() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    if (addRunnable != null)
                    {
                        addRunnable.run();
                    }
                }
            }
        });
        this.addNewRow.addMouseMoveListener(event ->
        {
            if (event.getEventType() == AOTDMouseMoveEvent.EventType.Enter)
            {
                // If the button is hovered and visible play the button hover sound
                if (addNewRow.isHovered() && addNewRow.isVisible())
                {
                    entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f);
                }
            }
        });
        this.add(addNewRow);
        this.removeRow = new AOTDGuiButton(15, height - 15, 15, 15, null, "afraidofthedark:textures/gui/spell_editor/delete.png", "afraidofthedark:textures/gui/spell_editor/delete_hovered.png");
        this.removeRow.setHoverText("Remove spell stage");
        this.removeRow.addMouseListener(event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Press)
            {
                // If we press the button and it's visible/hovered run the callback
                if (removeRow.isHovered() && removeRow.isVisible() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    if (removeRunnable != null)
                    {
                        removeRunnable.run();
                    }
                }
            }
        });
        this.removeRow.addMouseMoveListener(event ->
        {
            if (event.getEventType() == AOTDMouseMoveEvent.EventType.Enter)
            {
                // If the button is hovered and visible play the button hover sound
                if (removeRow.isHovered() && removeRow.isVisible())
                {
                    entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f);
                }
            }
        });
        this.add(removeRow);
    }

    /**
     * Shows the + button
     */
    void showPlus()
    {
        this.addNewRow.setVisible(true);
    }

    /**
     * Hides the + button
     */
    void hidePlus()
    {
        this.addNewRow.setVisible(false);
    }

    /**
     * Shows the - button
     */
    void showMinus()
    {
        this.removeRow.setVisible(true);
    }

    /**
     * Hides the - button
     */
    void hideMinus()
    {
        this.removeRow.setVisible(false);
    }

    /**
     * Sets the on add callback, this will get fired when the add button is pressed
     *
     * @param addRunnable The callback runnable
     */
    void setOnAdd(Runnable addRunnable)
    {
        this.addRunnable = addRunnable;
    }

    /**
     * Sets the on remove callback, this will get fired when the remove button is pressed
     *
     * @param removeRunnable The callback runnable
     */
    void setOnRemove(Runnable removeRunnable)
    {
        this.removeRunnable = removeRunnable;
    }

    /**
     * Updates this spell stage's slots based on the spell stage.
     */
    void refresh()
    {
        // Update the delivery method icon based on delivery method
        this.deliveryMethod.setComponentInstance(this.spellStage.getDeliveryMethod());
        // Update each effect slot
        for (int i = 0; i < this.spellStage.getEffects().length; i++)
        {
            this.effects[i].setComponentInstance(this.spellStage.getEffects()[i]);
        }
    }

    /**
     * @return Gets the delivery method slot on this stage
     */
    public AOTDGuiSpellDeliveryMethodSlot getDeliveryMethod()
    {
        return deliveryMethod;
    }

    /**
     * @return Gets an array of the 4 effect slots on this stage
     */
    public AOTDGuiSpellEffectSlot[] getEffects()
    {
        return effects;
    }
}
