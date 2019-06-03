package com.DavidM1A2.afraidofthedark.client.gui.specialControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.DavidM1A2.afraidofthedark.common.spell.component.SpellComponent;
import com.DavidM1A2.afraidofthedark.common.spell.component.SpellComponentEntry;

/**
 * Base class for all AOTD gui spell components
 */
public abstract class AOTDGuiSpellComponentSlot<T extends SpellComponentEntry<T, V>, V extends SpellComponent> extends AOTDGuiContainer
{
    // The foreground image of the spell slot
    private final AOTDGuiImage icon;
    // The highlight effect around the spell slot
    private final AOTDGuiImage highlight;
    // The type that this spell slot is
    private T componentType;
    // An instance of the component type
    private V componentInstance;

    /**
     * Constructor initializes the bounding box
     *
     * @param x      The X location of the top left corner
     * @param y      The Y location of the top left corner
     * @param width  The width of the component
     * @param height The height of the component
     */
    public AOTDGuiSpellComponentSlot(Integer x, Integer y, Integer width, Integer height, String slotBackground, T componentInstance)
    {
        super(x, y, width, height);

        // The background image of the spell slot
        AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height, slotBackground);
        this.add(background);

        // Add a highlight effect image over the background
        this.highlight = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spell_editor/highlight.png");
        this.highlight.setVisible(false);
        this.add(highlight);

        // Set the icon to be blank
        this.icon = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spell_editor/blank_slot.png");
        this.add(icon);

        this.setComponentType(componentInstance);
    }

    /**
     * Sets the component type and icon of this slot from a type
     *
     * @param componentType The component type to use
     */
    public void setComponentType(T componentType)
    {
        this.componentType = componentType;
        // If the component type is null hide the icon and set the instance to null too
        if (this.componentType == null)
        {
            this.icon.setVisible(false);
            this.componentInstance = null;
        }
        // If the component type is non-null show the the right icon and create a new instance of the type
        else
        {
            this.icon.setImageTexture(componentType.getIcon());
            this.icon.setVisible(true);
            this.componentInstance = this.componentType.newInstance();
        }
        // Update the hover text based on the slot
        this.refreshHoverText();
    }

    /**
     * Sets the component instance and icon of this slot from an instance
     *
     * @param componentInstance The component instance to use
     */
    public void setComponentInstance(V componentInstance)
    {
        this.componentInstance = componentInstance;
        // If the instance is null hide the icon and set the type to null too
        if (this.componentInstance == null)
        {
            this.icon.setVisible(false);
            this.componentType = null;
        }
        // If the instance is non-null set the component type and icon
        else
        {
            this.componentType = this.componentInstance.getEntryRegistryType();
            this.icon.setImageTexture(componentType.getIcon());
            this.icon.setVisible(true);
        }
        this.refreshHoverText();
    }

    /**
     * Refreshes the text that gets displayed when the slot is hovered
     */
    abstract void refreshHoverText();

    /**
     * @return The component type of this slot
     */
    public T getComponentType()
    {
        return componentType;
    }

    /**
     * @return The instance of this component type
     */
    public V getComponentInstance()
    {
        return componentInstance;
    }

    /**
     * True if the highlight should be shown, false otherwise
     *
     * @param highlit True if the slot is highlit, false otherwise
     */
    public void setHighlight(boolean highlit)
    {
        this.highlight.setVisible(highlit);
    }
}
