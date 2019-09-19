package com.DavidM1A2.afraidofthedark.client.gui.specialControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.IAOTDMouseListener;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.*;
import com.DavidM1A2.afraidofthedark.client.settings.ClientData;
import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.constants.ModSounds;
import com.DavidM1A2.afraidofthedark.common.spell.component.InvalidValueException;
import com.DavidM1A2.afraidofthedark.common.spell.component.SpellComponent;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.property.SpellComponentProperty;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * Compliment control to the tablet, allows players to click spell components up
 */
public class AOTDGuiSpellScroll extends AOTDGuiContainer
{
    // The callback that will be fired when a spell component is selected
    private Consumer<AOTDGuiSpellComponentSlot<?, ?>> componentClickCallback;
    // The scroll panel containing parts or the edit UI
    private final AOTDGuiScrollPanel scrollPanel;
    // The actual component scroll panel that holds a list of spell components
    private final AOTDGuiPanel componentScrollPanel;
    // The offset that the scroll panel should have when the component scroll panel is visible
    private final int componentScrollPanelOffset;
    // A List of any additional text fields we currently have editing properties
    private final List<Pair<SpellComponentProperty, AOTDGuiTextField>> currentPropEditors = new ArrayList<>();

    /**
     * Constructor initializes the bounding box
     *
     * @param x      The X location of the top left corner
     * @param y      The Y location of the top left corner
     * @param width  The width of the component
     * @param height The height of the component
     */
    public AOTDGuiSpellScroll(Integer x, Integer y, Integer width, Integer height)
    {
        super(x, y, width, height);

        // Create the base panel to attach all of our components to
        // The scroll that contains either a list of components or a component editor
        AOTDGuiPanel scroll = new AOTDGuiPanel(0, 0, width, height, false);

        // Add the background scroll texture image
        AOTDGuiImage backgroundScroll = new AOTDGuiImage(0, 0, width - 20, height, "afraidofthedark:textures/gui/spell_editor/effect_list_scroll.png");
        scroll.add(backgroundScroll);

        // Add a scroll bar to the right of the scroll
        AOTDGuiScrollBar componentScrollBar = new AOTDGuiScrollBar(backgroundScroll.getWidth(), 50, 13, height - 100);
        scroll.add(componentScrollBar);

        // Add a scroll panel to the scroll
        scrollPanel = new AOTDGuiScrollPanel(40, 50, 120, 175, true, componentScrollBar);
        scroll.add(scrollPanel);

        // Create a base panel for all components
        this.componentScrollPanel = new AOTDGuiPanel(0, 0, 120, 175, false);

        // The current component index we're adding to the scroll
        final int COMPONENTS_PER_LINE = 5;
        int currentComponent = 0;

        // Create the power source label
        AOTDGuiLabel powerSourceHeading = new AOTDGuiLabel(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 120, 20, ClientData.getInstance().getTargaMSHandFontSized(46f));
        powerSourceHeading.setTextColor(new Color(140, 35, 206));
        powerSourceHeading.setText("Power Sources");
        this.componentScrollPanel.add(powerSourceHeading);
        currentComponent = currentComponent + COMPONENTS_PER_LINE;

        // Listener to be used by all of our spell components
        IAOTDMouseListener componentClickListener = event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Press)
            {
                // If the component is hovered fire the listener
                if (event.getSource().isHovered() && event.getSource().isVisible() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    if (componentClickCallback != null)
                    {
                        componentClickCallback.accept((AOTDGuiSpellComponentSlot<?, ?>) event.getSource());
                    }
                }
            }
        };

        // Go over all power sources and add a slot for each
        for (SpellPowerSourceEntry powerSourceEntry : ModRegistries.SPELL_POWER_SOURCES)
        {
            AOTDGuiSpellPowerSourceSlot powerSource = new AOTDGuiSpellPowerSourceSlot(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 20, 20, powerSourceEntry);
            powerSource.addMouseListener(componentClickListener);
            this.componentScrollPanel.add(powerSource);
            currentComponent = currentComponent + 1;
        }

        // Round the current component to a multiple of COMPONENTS_PER_LINE
        currentComponent = MathHelper.roundUp(currentComponent, COMPONENTS_PER_LINE);

        // Create the effect label
        AOTDGuiLabel effectHeading = new AOTDGuiLabel(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 120, 20, ClientData.getInstance().getTargaMSHandFontSized(46f));
        effectHeading.setTextColor(new Color(140, 35, 206));
        effectHeading.setText("Effects");
        this.componentScrollPanel.add(effectHeading);
        currentComponent = currentComponent + COMPONENTS_PER_LINE;

        // Go over all effects and add a slot for each
        for (SpellEffectEntry effectEntry : ModRegistries.SPELL_EFFECTS)
        {
            AOTDGuiSpellEffectSlot effect = new AOTDGuiSpellEffectSlot(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 20, 20, effectEntry);
            effect.addMouseListener(componentClickListener);
            this.componentScrollPanel.add(effect);
            currentComponent = currentComponent + 1;
        }

        // Round the current component to a multiple of COMPONENTS_PER_LINE
        currentComponent = MathHelper.roundUp(currentComponent, COMPONENTS_PER_LINE);

        // Create the delivery method label
        AOTDGuiLabel deliveryMethodHeading = new AOTDGuiLabel(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 120, 20, ClientData.getInstance().getTargaMSHandFontSized(46f));
        deliveryMethodHeading.setTextColor(new Color(140, 35, 206));
        deliveryMethodHeading.setText("Delivery Methods");
        this.componentScrollPanel.add(deliveryMethodHeading);
        currentComponent = currentComponent + COMPONENTS_PER_LINE;

        // Go over all delivery methods and add a slot for each
        for (SpellDeliveryMethodEntry deliveryMethodEntry : ModRegistries.SPELL_DELIVERY_METHODS)
        {
            AOTDGuiSpellDeliveryMethodSlot deliveryMethod = new AOTDGuiSpellDeliveryMethodSlot(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 20, 20, deliveryMethodEntry);
            deliveryMethod.addMouseListener(componentClickListener);
            this.componentScrollPanel.add(deliveryMethod);
            currentComponent = currentComponent + 1;
        }

        // Update the scroll offset accordingly
        int offset = currentComponent / 6 - 5;
        offset = offset > 0 ? offset : 0;
        this.componentScrollPanelOffset = offset * 30;
        this.scrollPanel.setMaximumOffset(this.componentScrollPanelOffset);
        this.scrollPanel.add(componentScrollPanel);

        // Add the scroll to the component
        this.add(scroll);
    }

    /**
     * Called when a component is clicked
     *
     * @param componentClickCallback The callback that to fire
     */
    public void setComponentClickCallback(Consumer<AOTDGuiSpellComponentSlot<?, ?>> componentClickCallback)
    {
        this.componentClickCallback = componentClickCallback;
    }

    /**
     * Sets the current spell component to be edited, or null if no component is being edited
     *
     * @param spellComponent The spell component to edit, or null to clear it
     */
    public void setEditing(SpellComponent spellComponent)
    {
        // Clear the current list of prop editors
        currentPropEditors.clear();
        // If this is null then clear the currently edited spell
        if (spellComponent == null)
        {
            // Remove all nodes from the scroll panel
            this.scrollPanel.getChildren().forEach(this.scrollPanel::remove);
            // Add the component scroll panel back in
            this.scrollPanel.add(this.componentScrollPanel);
            // Reset the maximum offset
            this.scrollPanel.setMaximumOffset(this.componentScrollPanelOffset);
        }
        else
        {
            // Create a panel to hold all of our controls
            AOTDGuiPanel editPanel = new AOTDGuiPanel(0, 0, 120, 175, false);
            // Start at x=0, or the "top" of the scroll
            int currentY = 0;

            // Purple text color
            Color purpleText = new Color(140, 35, 206);

            // Create a heading label to indicate what is currently being edited
            AOTDGuiLabel heading = new AOTDGuiLabel(0, currentY, 120, 30, ClientData.getInstance().getTargaMSHandFontSized(32f));
            heading.setTextColor(purpleText);
            heading.setText(I18n.format(spellComponent.getEntryRegistryType().getUnlocalizedName()) + " Properties");
            editPanel.add(heading);
            currentY = currentY + heading.getHeight();

            // Grab a list of editable properties
            List<SpellComponentProperty> editableProperties = spellComponent.getEditableProperties();

            // If there are no editable properties say so with a text box
            if (editableProperties.isEmpty())
            {
                AOTDGuiTextBox noPropsLine = new AOTDGuiTextBox(0, currentY, 120, 30, ClientData.getInstance().getTargaMSHandFontSized(26f));
                noPropsLine.setTextColor(purpleText);
                noPropsLine.setText("This component has no editable properties.");
                editPanel.add(noPropsLine);
                currentY = currentY + noPropsLine.getHeight();
            }
            else
            {
                // Go over each editable property and add an editor for it
                for (SpellComponentProperty editableProp : editableProperties)
                {
                    // Create a label that states the name of the property
                    AOTDGuiLabel propertyName = new AOTDGuiLabel(0, currentY, 120, 15, ClientData.getInstance().getTargaMSHandFontSized(26f));
                    propertyName.setTextColor(purpleText);
                    propertyName.setText("Name: " + editableProp.getName());
                    editPanel.add(propertyName);
                    currentY = currentY + propertyName.getHeight();
                    // Create a text box that shows the description of the property
                    AOTDGuiTextBox propertyDescription = new AOTDGuiTextBox(0, currentY, 120, 12, ClientData.getInstance().getTargaMSHandFontSized(26f));
                    propertyDescription.setTextColor(purpleText);
                    propertyDescription.setText("Description: " + editableProp.getDescription());
                    // While we don't have enough room for the description increase the size by a constant
                    while (!propertyDescription.getOverflowText().isEmpty())
                    {
                        propertyDescription.setHeight(propertyDescription.getHeight() + 12);
                        propertyDescription.setText("Description: " + editableProp.getDescription());
                    }
                    editPanel.add(propertyDescription);
                    currentY = currentY + propertyDescription.getHeight();
                    // Create a text field that edits the property value
                    AOTDGuiTextField propertyEditor = new AOTDGuiTextField(0, currentY, 120, 30, ClientData.getInstance().getTargaMSHandFontSized(26f));
                    propertyEditor.setTextColor(purpleText);
                    propertyEditor.setText(editableProp.getGetter().get());
                    editPanel.add(propertyEditor);
                    // Store the editor off for later use
                    this.currentPropEditors.add(Pair.of(editableProp, propertyEditor));
                    currentY = currentY + propertyEditor.getHeight();
                }
            }

            // If we have any editable properties show the save button
            if (!editableProperties.isEmpty())
            {
                // Add a save button at the bottom if we have any editable properties
                AOTDGuiButton save = new AOTDGuiButton(0, currentY + 5, 50, 20, ClientData.getInstance().getTargaMSHandFontSized(32f), "afraidofthedark:textures/gui/spell_editor/button.png", "afraidofthedark:textures/gui/spell_editor/button_hovered.png");
                save.setTextAlignment(TextAlignment.ALIGN_CENTER);
                save.setText("Save");
                save.addMouseListener(event ->
                {
                    if (event.getEventType() == AOTDMouseEvent.EventType.Press)
                    {
                        if (save.isVisible() && save.isHovered() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                        {
                            // Go over all properties and their editors
                            for (Pair<SpellComponentProperty, AOTDGuiTextField> propEditorPair : currentPropEditors)
                            {
                                // Grab the property and editor
                                SpellComponentProperty property = propEditorPair.getLeft();
                                AOTDGuiTextField editor = propEditorPair.getRight();
                                // Attempt to set the property
                                try
                                {
                                    property.getSetter().accept(editor.getText());
                                }
                                // If we get an exception tell the player what went wrong
                                catch (InvalidValueException e)
                                {
                                    entityPlayer.sendMessage(new TextComponentTranslation("aotd.spell.property_edit_fail", propEditorPair.getKey().getName(), e.getMessage()));
                                }
                            }
                            // Clear the editor
                            setEditing(null);
                        }
                    }
                });
                // When we hover the button play the hover sound
                save.addMouseMoveListener(event ->
                {
                    if (event.getEventType() == AOTDMouseMoveEvent.EventType.Enter)
                    {
                        if (save.isVisible())
                        {
                            entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.7f, 1.7f);
                        }
                    }
                });
                editPanel.add(save);
            }

            // Add a cancel button at the bottom. Center it if we have no edit properties (and no save button!)
            int cancelX = editableProperties.isEmpty() ? editPanel.getWidth() / 2 - 25 : editPanel.getWidth() - 50;
            AOTDGuiButton cancel = new AOTDGuiButton(cancelX, currentY + 5, 50, 20, ClientData.getInstance().getTargaMSHandFontSized(32f), "afraidofthedark:textures/gui/spell_editor/button.png", "afraidofthedark:textures/gui/spell_editor/button_hovered.png");
            cancel.setTextAlignment(TextAlignment.ALIGN_CENTER);
            cancel.setText(editableProperties.isEmpty() ? "Close" : "Cancel");
            cancel.addMouseListener(event ->
            {
                if (event.getEventType() == AOTDMouseEvent.EventType.Press)
                {
                    if (cancel.isVisible() && cancel.isHovered() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                    {
                        // Clear the currently edited spell
                        setEditing(null);
                    }
                }
            });
            // When we hover the button play the hover sound
            cancel.addMouseMoveListener(event ->
            {
                if (event.getEventType() == AOTDMouseMoveEvent.EventType.Enter)
                {
                    if (cancel.isVisible())
                    {
                        entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.7f, 1.7f);
                    }
                }
            });
            editPanel.add(cancel);
            currentY = currentY + cancel.getHeight() + 5;

            // Remove all nodes from the scroll panel
            this.scrollPanel.getChildren().forEach(this.scrollPanel::remove);
            // Add in the edit panel
            this.scrollPanel.add(editPanel);
            // Update the scroll offset
            this.scrollPanel.setMaximumOffset(Math.max(0, currentY - editPanel.getHeight()));
        }
    }

    /**
     * @return True if the inventory key should currently close the UI, false otherwise
     */
    public boolean inventoryKeyClosesUI()
    {
        return currentPropEditors.stream().map(Pair::getRight).noneMatch(AOTDGuiTextField::isFocused);
    }
}
