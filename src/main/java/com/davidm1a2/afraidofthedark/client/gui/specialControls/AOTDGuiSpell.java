package com.davidm1a2.afraidofthedark.client.gui.specialControls;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment;
import com.davidm1a2.afraidofthedark.client.gui.eventListeners.IAOTDMouseListener;
import com.davidm1a2.afraidofthedark.client.gui.eventListeners.IAOTDMouseMoveListener;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiButton;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiLabel;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel;
import com.davidm1a2.afraidofthedark.client.settings.ClientData;
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.constants.ModSounds;
import com.davidm1a2.afraidofthedark.common.spell.Spell;
import net.minecraft.init.SoundEvents;
import org.lwjgl.util.Color;

/**
 * UI component representing a spell in the GUI
 */
public class AOTDGuiSpell extends AOTDGuiContainer
{
    // The spell that is being drawn by this GuiSpell
    private final Spell spell;
    // Reference to the keybind label that allows us to bind keys to spells
    private final AOTDGuiLabel lblKeybind;
    // Callback function that we fire when we want a new keybind for this spell
    private Runnable keybindCallback;
    // Callback function that we fire when the delete spell button is pressed
    private Runnable deleteCallback;

    /**
     * Constructor just initializes the gui spell by laying out all necessary controls
     *
     * @param x The x position of the control
     * @param y The y position of the control
     * @param width The width of the control
     * @param height The height of the control
     * @param spell The spell that this control represents
     */
    public AOTDGuiSpell(int x, int y, int width, int height, Spell spell)
    {
        super(x, y, width, height);
        this.spell = spell;

        // The background image to hold all the buttons
        AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spell_list/spell_background.png");
        this.add(background);

        // The container for spell name
        AOTDGuiPanel spellNameContainer = new AOTDGuiPanel(10, 3, width - 20, 15, false);

        // The label holding the actual spell name
        AOTDGuiLabel lblSpellName = new AOTDGuiLabel(0, 0, spellNameContainer.getWidth(), spellNameContainer.getHeight(), ClientData.getInstance().getTargaMSHandFontSized(36f));
        // Set the name label's name and color
        lblSpellName.setText(this.spell.getName());
        lblSpellName.setTextColor(new Color(245, 61, 199));
        lblSpellName.setTextAlignment(TextAlignment.ALIGN_CENTER);
        // Update the hover text of the container and add the spell label to it
        spellNameContainer.setHoverText(lblSpellName.getText());
        spellNameContainer.add(lblSpellName);
        this.add(spellNameContainer);

        // When we hover any button play hover sound
        IAOTDMouseMoveListener hoverSound = event ->
        {
            if (event.getEventType() == AOTDMouseMoveEvent.EventType.Enter)
            {
                // Play a hover sound for visible buttons
                if (event.getSource().isVisible() && event.getSource().isHovered())
                {
                    entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.7f, 1.9f);
                }
            }
        };
        // When we click any button play the click sound
        IAOTDMouseListener clickSound = event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Click)
            {
                // Play a clicked sound for visible buttons
                if (event.getSource().isVisible() && event.getSource().isHovered())
                {
                    entityPlayer.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }
        };

        // Create a button to edit the spell
        AOTDGuiButton btnEdit = new AOTDGuiButton(5, 22, 24, 13, null, "afraidofthedark:textures/gui/spell_list/spell_edit.png", "afraidofthedark:textures/gui/spell_list/spell_edit_hovered.png");
        btnEdit.addMouseListener(clickSound);
        btnEdit.addMouseMoveListener(hoverSound);
        btnEdit.setHoverText("Edit Spell");
        btnEdit.addMouseListener(event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Click)
            {
                if (event.getSource().isHovered() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    // Set the edited spell to this one
                    ClientData.getInstance().setLastSelectedSpell(spell);
                    // Open the spell edit GUI
                    entityPlayer.openGui(AfraidOfTheDark.INSTANCE, AOTDGuiHandler.SPELL_CRAFTING_ID, entityPlayer.world, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
                }
            }
        });
        this.add(btnEdit);

        // Create a button to delete a spell
        AOTDGuiButton btnDelete = new AOTDGuiButton(width - 5 - 24, 22, 24, 13, null, "afraidofthedark:textures/gui/spell_list/spell_delete.png", "afraidofthedark:textures/gui/spell_list/spell_delete_hovered.png");
        btnEdit.addMouseListener(clickSound);
        btnEdit.addMouseMoveListener(hoverSound);
        btnDelete.addMouseListener(event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Press)
            {
                if (event.getSource().isHovered() && event.getSource().isVisible() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    if (deleteCallback != null)
                    {
                        deleteCallback.run();
                    }
                }
            }
        });
        btnDelete.setHoverTexts("Delete Spell", "This cannot be undone");
        this.add(btnDelete);

        // Create a button to keybind this spell
        lblKeybind = new AOTDGuiLabel(37, 20, 100, 13, ClientData.getInstance().getTargaMSHandFontSized(30f));
        lblKeybind.setTextAlignment(TextAlignment.ALIGN_CENTER);
        btnEdit.addMouseListener(clickSound);
        btnEdit.addMouseMoveListener(hoverSound);
        lblKeybind.addMouseListener(event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Press)
            {
                if (event.getSource().isHovered() && event.getSource().isVisible() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    if (keybindCallback != null)
                    {
                        lblKeybind.setHoverText("Awaiting keypress...");
                        lblKeybind.setText("Awaiting keypress...");
                        keybindCallback.run();
                    }
                }
            }
        });
        this.add(lblKeybind);

        // Refresh the spell labels
        this.refreshLabels();
    }

    /**
     * Refreshes this gui spell based on the current spell state if it's changed
     */
    public void refreshLabels()
    {
        // Grab the player's spell manager
        IAOTDPlayerSpellManager spellManager = entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null);
        // Get the keybinding for the spell
        String keybindingForSpell = spellManager.getKeybindingForSpell(this.spell);
        // if the keybind is non-null show it, otherwise mention it's unbound
        if (keybindingForSpell != null)
        {
            lblKeybind.setHoverText("Spell is bound to: " + keybindingForSpell);
            lblKeybind.setText(keybindingForSpell);
        }
        else
        {
            lblKeybind.setHoverText("Spell is unbound.");
            lblKeybind.setText("");
        }
    }

    /**
     * Sets the callback when the keybind button is pressed
     *
     * @param keybindCallback The keybind callback to fire
     */
    public void setKeybindCallback(Runnable keybindCallback)
    {
        this.keybindCallback = keybindCallback;
    }

    /**
     * Sets the callback when the delete button is pressed
     *
     * @param deleteCallback The delete callback to fire
     */
    public void setDeleteCallback(Runnable deleteCallback)
    {
        this.deleteCallback = deleteCallback;
    }

    /**
     * @return The spell that this gui spell represents
     */
    public Spell getSpell()
    {
        return spell;
    }
}
