package com.DavidM1A2.afraidofthedark.common.spell;

import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Class representing a spell instance created by a player
 */
public class Spell implements INBTSerializable<NBTTagCompound>
{
    // Constants used for NBT serialization/deserialiation
    private static final String NBT_NAME = "name";
    private static final String NBT_ID = "id";
    private static final String NBT_OWNER_ID = "owner_id";
    private static final String NBT_POWER_SOURCE = "power_source";
    private static final String NBT_POWER_SOURCE_STATE = "power_source_state";

    // The spell's name, can't be null (empty by default)
    private String name;
    // The spell's universally unique identifier, cannot be null
    private UUID id;
    // The spell's owner's id (player's universally unique identifier), cannot be null
    private UUID ownerId;

    // The source that is powering the spell, can be null
    private SpellPowerSource spellPowerSource;
    // The NBT compound used to store the power source's state data
    private NBTTagCompound spellPowerSourceState;
    // The list of spell stages this spell can go through, can have 0 - inf elements

    /**
     * Constructor that takes in an NBT compound and creates the spell from NBT
     *
     * @param spellNBT The NBT containing the spell's information
     */
    public Spell(NBTTagCompound spellNBT)
    {
        this.deserializeNBT(spellNBT);
    }

    /**
     * Constructor that takes the player that created the spell in as a parameter
     *
     * @param entityPlayer The player that owns the spell/made the spell
     */
    public Spell(EntityPlayer entityPlayer)
    {
        // Assign a random spell ID
        this.id = UUID.randomUUID();
        // Assign the owner id to the player's id
        this.ownerId = entityPlayer.getPersistentID();
        // Empty spell name is default
        this.name = StringUtils.EMPTY;
        // Null spell power source is default
        this.spellPowerSource = null;
        // Null spell power source NBT for spell power source
        this.spellPowerSourceState = null;
    }

    /**
     * Writes the contents of the object into a new NBT compound
     *
     * @return An NBT compound with all this spell's data
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString(NBT_NAME, this.name);
        nbt.setTag(NBT_ID, NBTUtil.createUUIDTag(this.id));
        nbt.setTag(NBT_OWNER_ID, NBTUtil.createUUIDTag(this.ownerId));
        if (this.spellPowerSource != null)
        {
            nbt.setString(NBT_POWER_SOURCE, this.spellPowerSource.getRegistryName().toString());
            nbt.setTag(NBT_POWER_SOURCE_STATE, this.spellPowerSourceState);
        }

        return nbt;
    }

    /**
     * Reads in the contents of the NBT into the object
     *
     * @param nbt The NBT compound to read from
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.name = nbt.getString(NBT_NAME);
        this.id = NBTUtil.getUUIDFromTag(nbt.getCompoundTag(NBT_ID));
        this.ownerId = NBTUtil.getUUIDFromTag(nbt.getCompoundTag(NBT_OWNER_ID));
        if (nbt.hasKey(NBT_POWER_SOURCE))
        {
            this.spellPowerSource = ModRegistries.SPELL_POWER_SOURCES.getValue(new ResourceLocation(nbt.getString(NBT_POWER_SOURCE)));
            this.spellPowerSourceState = nbt.getCompoundTag(NBT_POWER_SOURCE_STATE);
        }
    }

    /**
     * @return A special string containing all of this spell's information
     */
    @Override
    public String toString()
    {
        return "Spell Printout:" +
                this.getName() + "\n";
    }

    ///
    /// Getters/Setters
    ///

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public UUID getId()
    {
        return this.id;
    }

    public void setOwnerId(UUID ownerId)
    {
        this.ownerId = ownerId;
    }

    public UUID getOwnerId()
    {
        return this.ownerId;
    }

    public void setSpellPowerSource(SpellPowerSource spellPowerSource)
    {
        this.spellPowerSource = spellPowerSource;
        // Don't forget to update the stored NBT for the new power source!
        if (spellPowerSource != null)
        {
            this.spellPowerSourceState = this.spellPowerSource.generateBaseState();
        }
        else
        {
            this.spellPowerSourceState = null;
        }
    }

    public SpellPowerSource getSpellPowerSource()
    {
        return spellPowerSource;
    }

    public NBTTagCompound getSpellPowerSourceState()
    {
        return spellPowerSourceState;
    }
}
