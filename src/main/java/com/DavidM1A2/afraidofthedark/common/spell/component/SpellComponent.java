package com.DavidM1A2.afraidofthedark.common.spell.component;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all spell components (power sources, effects, and delivery methods)
 */
public abstract class SpellComponent<T extends IForgeRegistryEntry<T>> extends IForgeRegistryEntry.Impl<T>
{
    // A list of editable properties of this component
    private final List<EditableSpellComponentProperty> editableProperties = new ArrayList<>();

    /**
     * Adds an editable property that this spell component has
     *
     * @param property A property that this component has that can be edited
     */
    public void addEditableProperty(EditableSpellComponentProperty property)
    {
        this.editableProperties.add(property);
    }

    /**
     * @return An unmodifiable list of editable component properties
     */
    public List<EditableSpellComponentProperty> getEditableProperties()
    {
        return Collections.unmodifiableList(editableProperties);
    }

    /**
     * Called to initialize any NBT this spell component might need/can use to store any state
     *
     * @return An NBT compound containing any state this component must save on a per instance basis on each spell in the world
     */
    public NBTTagCompound generateBaseState()
    {
        NBTTagCompound state = new NBTTagCompound();

        // Go over each editable property and add the default state of this property to the state. Setting any
        // property to null ensures the default property is set instead
        this.editableProperties.forEach(editableProperty -> editableProperty.getPropertySetter().setProperty(null, state));

        return state;
    }
}
