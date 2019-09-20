package com.davidm1a2.afraidofthedark.common.capabilities.player.spell;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.spell.Spell;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Default storage implementation for the AOTD spell manager
 */
public class AOTDPlayerSpellManagerStorage implements Capability.IStorage<IAOTDPlayerSpellManager>
{
    // The spell list
    private static final String NBT_SPELLS_LIST = "spells";
    // The keybinds list
    private static final String NBT_KEYBINDS_LIST = "keybinds";
    // Two NBT fields, one for the keybind value and one for the keybind spell UUID used as a pair
    private static final String NBT_KEYBIND = "keybind";
    private static final String NBT_KEYBIND_SPELL_UUID = "keybind_spell_uuid";

    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IAOTDPlayerSpellManager> capability, IAOTDPlayerSpellManager instance, EnumFacing side)
    {
        // Create a compound to write
        NBTTagCompound compound = new NBTTagCompound();

        // Create a list of nbt spells
        NBTTagList spellsNBT = new NBTTagList();
        // Write each spell to NBT
        for (Spell spell : instance.getSpells())
        {
            // Write the spell to NBT
            spellsNBT.appendTag(spell.serializeNBT());
        }
        // Set the spell list into the compound
        compound.setTag(NBT_SPELLS_LIST, spellsNBT);

        // Go over each keybind and store it off
        NBTTagList keybindingsNBT = new NBTTagList();
        // Go over every spell that has a keybinding
        for (Spell spell : instance.getSpells())
        {
            // Grab the keybinding for the spell, test if it's valid
            String keybinding = instance.getKeybindingForSpell(spell);
            if (keybinding != null)
            {
                // Store the spell UUID, keybind pair
                NBTTagCompound keybindCompound = new NBTTagCompound();
                keybindCompound.setTag(NBT_KEYBIND_SPELL_UUID, NBTUtil.createUUIDTag(spell.getId()));
                keybindCompound.setString(NBT_KEYBIND, keybinding);
                keybindingsNBT.appendTag(keybindCompound);
            }
        }
        // Set the spell keybinds into the compound
        compound.setTag(NBT_KEYBINDS_LIST, keybindingsNBT);

        return compound;
    }

    /**
     * Called to read the NBTTagCompound into a capability
     *
     * @param capability The capability that is being read
     * @param instance   The instance to of the capability to read
     * @param side       ignored
     * @param nbt        An NBTTagCompound that contains all info about the capability
     */
    @Override
    public void readNBT(Capability<IAOTDPlayerSpellManager> capability, IAOTDPlayerSpellManager instance, EnumFacing side, NBTBase nbt)
    {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt instanceof NBTTagCompound)
        {
            // The compound to read from
            NBTTagCompound compound = (NBTTagCompound) nbt;

            // Grab the list of spells
            NBTTagList spellsNBT = compound.getTagList(NBT_SPELLS_LIST, Constants.NBT.TAG_COMPOUND);
            // Go over each spell in the list
            for (int i = 0; i < spellsNBT.tagCount(); i++)
            {
                // Grab the compound for the spell
                NBTTagCompound spellNBT = spellsNBT.getCompoundTagAt(i);
                // Create a new spell instance, and read in the state from NBT
                Spell spell = new Spell(spellNBT);
                // Add the spell to the list
                instance.addOrUpdateSpell(spell);
            }

            // A utility temp map of uuid -> spell for use in determining keybinds in O(1) for extra memory usage
            Map<UUID, Spell> idToSpell = instance.getSpells().stream().collect(Collectors.toMap(Spell::getId, Function.identity()));
            // Restore the keybindings
            NBTTagList keybindingsNBT = compound.getTagList(NBT_KEYBINDS_LIST, Constants.NBT.TAG_COMPOUND);
            // Go over each compound in the list
            for (int i = 0; i < keybindingsNBT.tagCount(); i++)
            {
                // Grab the compound for the keybinding
                NBTTagCompound keybindingNBT = keybindingsNBT.getCompoundTagAt(i);
                // Grab the key and spell UUID
                UUID spellUUID = NBTUtil.getUUIDFromTag(keybindingNBT.getCompoundTag(NBT_KEYBIND_SPELL_UUID));
                String keybind = keybindingNBT.getString(NBT_KEYBIND);
                // Keybind the key to the spell
                instance.keybindSpell(keybind, idToSpell.get(spellUUID));
            }
        }
        // There's an error, this should not be possible
        else
        {
            AfraidOfTheDark.INSTANCE.getLogger().error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!");
        }
    }
}
