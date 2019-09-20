package com.davidm1a2.afraidofthedark.common.capabilities.world;

import com.davidm1a2.afraidofthedark.common.capabilities.world.storage.DelayedDeliveryEntry;
import com.davidm1a2.afraidofthedark.common.constants.Constants;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * World data that stores any spell data that is not stored in entity form and must persist
 */
public class SpellStateData extends WorldSavedData
{
    // The ID of the AOTD spell states in progress
    private static final String IDENTIFIER = Constants.MOD_ID + "_spell_states";

    // NBT constants used in serialization/deserialization
    private static final String NBT_DELAYED_DELIVERY_ENTRIES = "delayed_delivery_entries";

    // A list of delayed delivery entries that are awaiting delivery
    private List<DelayedDeliveryEntry> delayedDeliveryEntries = new ArrayList<>();

    /**
     * Constructor just calls super with our ID
     */
    public SpellStateData()
    {
        this(IDENTIFIER);
    }

    /**
     * Constructor where we can supply our own ID
     *
     * @param identifier The ID to use for this data
     */
    public SpellStateData(String identifier)
    {
        super(identifier);
    }

    /**
     * Called to get the SpellStateData for this world. Returns null if on client side
     *
     * @param world The world to get data for
     * @return The data for that world or null if it is not present
     */
    public static SpellStateData get(World world)
    {
        // If we are on client side throw an exception
        if (world.isRemote)
        {
            throw new UnsupportedOperationException("Attempted to get the SpellStateData client side!");
        }

        // Grab the storage object for all worlds (per server, not per world!)
        MapStorage storage = world.getMapStorage();
        // Get the saved spell state data for all worlds (per server, not per world!)
        SpellStateData spellStateData = (SpellStateData) storage.getOrLoadData(SpellStateData.class, IDENTIFIER);

        // If it does not exist, instantiate new spell state data and store it into the storage object
        if (spellStateData == null)
        {
            spellStateData = new SpellStateData();
            storage.setData(IDENTIFIER, spellStateData);
            spellStateData.markDirty();
        }

        // Return the data
        return spellStateData;
    }

    /**
     * Writes the delayed entries to NBT
     *
     * @param nbt The NBT to write to
     * @return The compound that represents the spell state data
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList delayedEntries = new NBTTagList();
        // Write each entry to the list
        this.delayedDeliveryEntries.forEach(delayedDeliveryEntry -> delayedEntries.appendTag(delayedDeliveryEntry.serializeNBT()));

        nbt.setTag(NBT_DELAYED_DELIVERY_ENTRIES, delayedEntries);

        return nbt;
    }

    /**
     * Reads the spell state data in from NBT
     *
     * @param nbt The NBT to read spell data in
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        // Go over each delayed delivery entry and read it in
        NBTTagList delayedEntries = nbt.getTagList(NBT_DELAYED_DELIVERY_ENTRIES, net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < delayedEntries.tagCount(); i++)
        {
            this.delayedDeliveryEntries.add(new DelayedDeliveryEntry(delayedEntries.getCompoundTagAt(i)));
        }
    }

    /**
     * Adds a delayed delivery method to the world data for storing until the delay is over
     *
     * @param state The state to store
     */
    public void addDelayedDelivery(DeliveryTransitionState state)
    {
        this.delayedDeliveryEntries.add(new DelayedDeliveryEntry(state));
        this.markDirty();
    }

    /**
     * Called once per tick, tests every delayed delivery method to see if it's ready to fire
     */
    public void update()
    {
        // Update each delayed delivery entry
        this.delayedDeliveryEntries.forEach(DelayedDeliveryEntry::update);
        // Get a list of delayed entries that are ready to fire
        List<DelayedDeliveryEntry> readyDelayedEntries = this.delayedDeliveryEntries.stream().filter(DelayedDeliveryEntry::isReadyToFire).collect(Collectors.toList());
        // If we have any ready delayed entries remove and fire them
        if (!readyDelayedEntries.isEmpty())
        {
            // Remove the delayed entries
            this.delayedDeliveryEntries.removeAll(readyDelayedEntries);
            // Fire the delayed entries
            readyDelayedEntries.forEach(DelayedDeliveryEntry::fire);
            // Ensure the data gets written to disk
            this.markDirty();
        }
    }
}
