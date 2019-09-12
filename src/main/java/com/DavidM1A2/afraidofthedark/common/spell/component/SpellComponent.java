package com.DavidM1A2.afraidofthedark.common.spell.component;

import com.DavidM1A2.afraidofthedark.common.spell.component.property.SpellComponentProperty;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all spell components (power sources, effects, and delivery methods)
 */
public abstract class SpellComponent implements INBTSerializable<NBTTagCompound>
{
    // Key for NBT representing the id of the type that this component is of
    protected static final String NBT_TYPE_ID = "type_id";

    // A list of editable properties of this component
    private final List<SpellComponentProperty> editableProperties = new ArrayList<>();

    /**
     * Adds an editable property that this spell component has
     *
     * @param property A property that this component has that can be edited
     */
    public void addEditableProperty(SpellComponentProperty property)
    {
        this.editableProperties.add(property);
    }

    /**
     * @return An unmodifiable list of editable component properties
     */
    public List<SpellComponentProperty> getEditableProperties()
    {
        return Collections.unmodifiableList(editableProperties);
    }

    /**
     * Should get the SpellComponentEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    public abstract <V extends SpellComponent, T extends SpellComponentEntry<T, V>> T getEntryRegistryType();

    /**
     * Serializes the spell component to NBT, override to add additional fields
     *
     * @return An NBT compound containing any required spell component info
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();

        // Store off the type ID used in deserialization
        compound.setString(NBT_TYPE_ID, this.getEntryRegistryType().getRegistryName().toString());

        return compound;
    }

    /**
     * Deserializes the state of this spell component from NBT
     *
     * @param nbt The NBT to deserialize from
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
    }
}
