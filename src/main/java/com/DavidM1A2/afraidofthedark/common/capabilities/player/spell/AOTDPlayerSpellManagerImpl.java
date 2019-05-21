package com.DavidM1A2.afraidofthedark.common.capabilities.player.spell;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.packets.capabilityPackets.SyncSpell;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

/**
 * Default implementation of the AOTD spell manager
 */
public class AOTDPlayerSpellManagerImpl implements IAOTDPlayerSpellManager
{
    // Keep two maps, one of keybind -> spell id and one of spell id -> spell
    // Key -> spell id is a bimap so we can answer queries both ways quickly
    private final BiMap<String, UUID> keybindToSpellId = HashBiMap.create();
    private final Map<UUID, Spell> spellIdToSpell = new HashMap<>();

    /**
     * Adds a spell to the list if it doesnt exist, or updates the spell if the ID already exists
     *
     * @param spell The spell to add or update
     */
    @Override
    public void addOrUpdateSpell(Spell spell)
    {
        this.spellIdToSpell.put(spell.getId(), spell);
    }

    /**
     * Deletes a spell from the list
     *
     * @param spell The spell to delete from the list
     */
    @Override
    public void deleteSpell(Spell spell)
    {
        this.spellIdToSpell.remove(spell.getId());
        // Make use of our bi-map to remove by "value"
        this.keybindToSpellId.inverse().remove(spell.getId());
    }

    @Override
    public Collection<Spell> getSpells()
    {
        return Collections.unmodifiableCollection(this.spellIdToSpell.values());
    }

    /**
     * Adds a keybinding to a given spell
     *
     * @param key The key to bind to the spell
     * @param spell The spell to fire when the key is pressed
     */
    @Override
    public void keybindSpell(String key, Spell spell)
    {
        // Test if we have a spell registered by the id first
        if (this.spellIdToSpell.containsKey(spell.getId()))
        {
            this.keybindToSpellId.forcePut(key, spell.getId());
        }
        else
        {
            AfraidOfTheDark.INSTANCE.getLogger().error("Cannot bind a spell that isn't registered!");
        }
    }

    /**
     * Tests if a key is registered as a keybinding
     *
     * @param key The key to test
     * @return True if the key maps to a spell, false otherwise
     */
    @Override
    public boolean keybindExists(String key)
    {
        return this.keybindToSpellId.containsKey(key);
    }

    /**
     * Gets the keybinding a spell is bound to or null if it is not bound to any
     *
     * @param spell The spell to grab the keybinding for
     * @return The keybinding a spell is bound to or null if it is unbound
     */
    @Override
    public String getKeybindingForSpell(Spell spell)
    {
        return this.keybindToSpellId.inverse().getOrDefault(spell.getId(), null);
    }

    /**
     * Gets the spell associated with a given keybinding or null if it does not exist
     *
     * @param key The keybinding to lookup spells for
     * @return A spell that this key is bound to or null if it does not exist
     */
    @Override
    public Spell getSpellForKeybinding(String key)
    {
        // Get the spell's UUID for the keybinding
        UUID spellUUID = this.keybindToSpellId.getOrDefault(key, null);
        // If it's non-null get the spell for the id
        if (spellUUID != null)
        {
            // Return the spell or null
            return this.spellIdToSpell.getOrDefault(spellUUID, null);
        }
        return null;
    }

    /**
     * Synchronizes the spell manager between server and client, can be sent
     * from any side. This will sync all known spells and keybindings
     *
     * @param entityPlayer The player to sync the spell manager to
     */
    @Override
    public void syncAll(EntityPlayer entityPlayer)
    {
        // Go over each spell and sync them one by one. We do this to avoid a large memory overhead
        // of sending many spells in a single packet
        this.spellIdToSpell.values().forEach(spell -> this.sync(entityPlayer, spell));
    }

    /**
     * Synchronizes a specific spell between server and client, can be sent from
     * any side. This will only send the spell and keybinding information for a single spell
     */
    @Override
    public void sync(EntityPlayer entityPlayer, Spell spell)
    {
        // Grab the keybind or null
        String keybind = this.getKeybindingForSpell(spell);
        // Create the packet
        SyncSpell syncSpellPacket = new SyncSpell(spell, keybind);
        // If we're on server side send the packet to the player, otherwise send it to the server
        if (this.isServerSide(entityPlayer))
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendTo(syncSpellPacket, (EntityPlayerMP) entityPlayer);
        }
        else
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(syncSpellPacket);
        }
    }

    /**
     * Returns true if the player is on server side or false if not
     *
     * @param entityPlayer The player to test
     * @return true if the player is on server side or false if not
     */
    private boolean isServerSide(EntityPlayer entityPlayer)
    {
        return !entityPlayer.world.isRemote;
    }
}
