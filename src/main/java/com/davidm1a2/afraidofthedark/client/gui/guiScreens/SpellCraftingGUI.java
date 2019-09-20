package com.davidm1a2.afraidofthedark.client.gui.guiScreens;

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiScreen;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent;
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellComponentSlot;
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellScroll;
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellTablet;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.davidm1a2.afraidofthedark.common.constants.Constants;
import com.davidm1a2.afraidofthedark.common.spell.Spell;
import com.davidm1a2.afraidofthedark.common.spell.SpellStage;

import java.io.IOException;

/**
 * Class representing the spell crating GUI screen used to edit spells
 */
public class SpellCraftingGUI extends AOTDGuiScreen
{
    // The tablet left side of the GUI
    private final AOTDGuiSpellTablet tablet;
    // The scroll right side of the GUI
    private final AOTDGuiSpellScroll scroll;
    // The selected cursor icon to hold the currently selected component's icon
    private final AOTDGuiImage selectedCursorIcon;
    // The current component slot that is selected on the scroll
    private AOTDGuiSpellComponentSlot<?, ?> selectedComponent;

    /**
     * Constructor creates the spell GUI
     *
     * @param spell The spell that this gui will edit
     */
    public SpellCraftingGUI(Spell spell)
    {
        // Clone the spell so we don't modify the original
        Spell spellClone = new Spell(spell.serializeNBT());

        // First ensure the spell has the minimum 1 spell stage
        if (spellClone.getSpellStages().isEmpty())
        {
            spellClone.getSpellStages().add(new SpellStage());
        }

        // Create the left side tablet to hold the current spell settings
        this.tablet = new AOTDGuiSpellTablet(100, (Constants.GUI_HEIGHT - 256) / 2, 192, 256, spellClone, this::getSelectedComponent, () -> this.setSelectedComponent(null));
        this.getContentPane().add(tablet);

        // Setup the selected component hover under the mouse cursor using image component
        selectedCursorIcon = new AOTDGuiImage(0, 0, 20, 20, "afraidofthedark:textures/gui/spell_editor/blank_slot.png");
        selectedCursorIcon.addMouseMoveListener(event ->
        {
            if (event.getEventType() == AOTDMouseMoveEvent.EventType.Move)
            {
                // If we have nothing selected put the component off in the middle of nowhere
                if (selectedComponent == null)
                {
                    selectedCursorIcon.setX(-20);
                    selectedCursorIcon.setY(-20);
                }
                // If we have something selected center the icon on the cursor
                else
                {
                    selectedCursorIcon.setX((int) (event.getMouseX() / tablet.getScaleX()) - selectedCursorIcon.getWidthScaled() / 2);
                    selectedCursorIcon.setY((int) (event.getMouseY() / tablet.getScaleY()) - selectedCursorIcon.getHeightScaled() / 2);
                }
            }
        });
        // If we right click clear the selected component
        this.getContentPane().addMouseListener(event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Press)
            {
                if (event.getClickedButton() == AOTDMouseEvent.RIGHT_MOUSE_BUTTON && selectedComponent != null)
                {
                    setSelectedComponent(null);
                }
            }
        });

        // Create the right side scroll to hold the current spell components available
        this.scroll = new AOTDGuiSpellScroll(340, (Constants.GUI_HEIGHT - 256) / 2, 220, 256);
        // When we click a component on the scroll update it as hovered
        this.scroll.setComponentClickCallback(this::setSelectedComponent);
        // When we click a component on the tablet update it as being edited
        this.tablet.setComponentEditCallback(slot -> scroll.setEditing(slot.getComponentInstance()));
        this.getContentPane().add(scroll);
        this.getContentPane().add(selectedCursorIcon);

        // Create a help overlay that comes up when you press the ? button
        AOTDGuiImage helpOverlay = new AOTDGuiImage(0, 0, Constants.GUI_WIDTH, Constants.GUI_HEIGHT, "afraidofthedark:textures/gui/spell_editor/help_screen.png");
        helpOverlay.setVisible(false);
        // When pressing any key hide the overlay
        helpOverlay.addKeyListener(event ->
        {
            if (event.getEventType() == AOTDKeyEvent.KeyEventType.Type)
            {
                if (event.getSource().isVisible())
                {
                    event.getSource().setVisible(false);
                }
            }
        });
        // When pressing help on the tablet show the help overlay
        tablet.setOnHelp(() -> helpOverlay.setVisible(true));
        this.getContentPane().add(helpOverlay);
    }

    /**
     * Called whenever a key is typed, we ask our key handler to handle the event
     * Also open the spell list GUI
     *
     * @param character The character typed
     * @param keyCode   The code of the character typed
     * @throws IOException forwarded from the super method
     */
    @Override
    protected void keyTyped(char character, int keyCode) throws IOException
    {
        super.keyTyped(character, keyCode);
        // If the inventory key closes the ui and is pressed open the spell list UI
        if (tablet.inventoryKeyClosesUI() && scroll.inventoryKeyClosesUI())
        {
            if (keyCode == INVENTORY_KEYCODE)
            {
                entityPlayer.openGui(Constants.MOD_ID, AOTDGuiHandler.SPELL_LIST_ID, entityPlayer.world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
            }
        }
    }

    /**
     * Sets the currently selected component slot, or null to clear it
     *
     * @param selectedComponent The currently selected spell component slot on the spell scroll, or null
     */
    private void setSelectedComponent(AOTDGuiSpellComponentSlot<?, ?> selectedComponent)
    {
        // If we have a previously selected component deselect it
        if (this.selectedComponent != null)
        {
            this.selectedComponent.setHighlight(false);
        }
        // If the new component is non-null update our image texture and highlight the component
        if (selectedComponent != null)
        {
            // Update the selected component, highlight the component
            this.selectedComponent = selectedComponent;
            this.selectedComponent.setHighlight(true);
            this.selectedCursorIcon.setImageTexture(this.selectedComponent.getComponentType().getIcon());
            this.selectedCursorIcon.setVisible(true);
        }
        // If it is null clear out our selected component and hide the icon
        else
        {
            this.selectedComponent = null;
            this.selectedCursorIcon.setVisible(false);
        }
    }

    /**
     * @return The currently selected spell component slot on the spell scroll
     */
    private AOTDGuiSpellComponentSlot<?, ?> getSelectedComponent()
    {
        return this.selectedComponent;
    }

    /**
     * False, inventory doesn't close the gui screen. We have more advanced logic in keyTyped()
     *
     * @return False to avoid any super code going off
     */
    @Override
    public boolean inventoryToCloseGuiScreen()
    {
        return false;
    }

    /**
     * True, we want the background to be a gradient
     *
     * @return True
     */
    @Override
    public boolean drawGradientBackground()
    {
        return true;
    }
}
