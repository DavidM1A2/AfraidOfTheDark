package com.DavidM1A2.afraidofthedark.client.gui.guiScreens;

import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiScreen;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDKeyListener;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDMouseMoveListener;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellComponentSlot;
import com.DavidM1A2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellScroll;
import com.DavidM1A2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellTablet;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.SpellStage;

import java.io.IOException;

/**
 * Class representing the spell crating GUI screen used to edit spells
 */
public class SpellCraftingGUI extends AOTDGuiScreen
{
    // A reference to the spell being edited
    private final Spell spell;
    // The tablet left side of the GUI
    private final AOTDGuiSpellTablet tablet;
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
        this.spell = new Spell(spell.serializeNBT());

        // First ensure the spell has the minimum 1 spell stage
        if (this.spell.getSpellStages().isEmpty())
        {
            this.spell.getSpellStages().add(new SpellStage());
        }

        // Create the left side tablet to hold the current spell settings
        this.tablet = new AOTDGuiSpellTablet(100, (Constants.GUI_HEIGHT - 256) / 2, 192, 256, this.spell);
        this.getContentPane().add(tablet);

        // Setup the selected component hover under the mouse cursor using image component
        AOTDGuiImage selectedCursorIcon = new AOTDGuiImage(0, 0, 20, 20, "afraidofthedark:textures/gui/spell_editor/blank_slot.png");
        selectedCursorIcon.addMouseMoveListener(new AOTDMouseMoveListener()
        {
            @Override
            public void mouseMoved(AOTDMouseEvent event)
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
        this.getContentPane().addMouseListener(new AOTDMouseListener()
        {
            @Override
            public void mousePressed(AOTDMouseEvent event)
            {
                if (event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Right && selectedComponent != null)
                {
                    selectedComponent.setHighlight(false);
                    selectedComponent = null;
                    selectedCursorIcon.setVisible(false);
                }
            }
        });

        // Create the right side scroll to hold the current spell components available
        AOTDGuiSpellScroll scroll = new AOTDGuiSpellScroll(340, (Constants.GUI_HEIGHT - 256) / 2, 220, 256);
        scroll.setComponentClickCallback(spellComponentSlot ->
        {
            // If we have a previously selected component deselect it
            if (selectedComponent != null)
            {
                selectedComponent.setHighlight(false);
            }
            // Update the selected component, highlight the component
            selectedComponent = spellComponentSlot;
            selectedComponent.setHighlight(true);
            selectedCursorIcon.setImageTexture(selectedComponent.getComponentType().getIcon());
            selectedCursorIcon.setVisible(true);
        });
        this.getContentPane().add(scroll);
        this.getContentPane().add(selectedCursorIcon);

        // Create a help overlay that comes up when you press the ? button
        AOTDGuiImage helpOverlay = new AOTDGuiImage(0, 0, Constants.GUI_WIDTH, Constants.GUI_HEIGHT, "afraidofthedark:textures/gui/spell_editor/help_screen.png");
        helpOverlay.setVisible(false);
        // When pressing any key hide the overlay
        helpOverlay.addKeyListener(new AOTDKeyListener()
        {
            @Override
            public void keyTyped(AOTDKeyEvent event)
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
        if (tablet.inventoryKeyClosesUI())
        {
            if (keyCode == INVENTORY_KEYCODE)
            {
                entityPlayer.openGui(Constants.MOD_ID, AOTDGuiHandler.SPELL_LIST_ID, entityPlayer.world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
            }
        }
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
