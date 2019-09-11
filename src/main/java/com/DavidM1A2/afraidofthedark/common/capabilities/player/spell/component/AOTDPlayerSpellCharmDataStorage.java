package com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.component;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Default storage implementation for the AOTD charm spell effect data
 */
public class AOTDPlayerSpellCharmDataStorage implements Capability.IStorage<IAOTDPlayerSpellCharmData>
{
    // NBT constants used for serialization
    private static final String NBT_CHARM_TICKS = "charm_ticks";
    private static final String NBT_CHARMING_ENTITY = "charming_entity";

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
    public NBTBase writeNBT(Capability<IAOTDPlayerSpellCharmData> capability, IAOTDPlayerSpellCharmData instance, EnumFacing side)
    {
        // Create a compound to write
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setInteger(NBT_CHARM_TICKS, instance.getCharmTicks());
        if (instance.getCharmingEntityId() != null)
        {
            nbt.setTag(NBT_CHARMING_ENTITY, NBTUtil.createUUIDTag(instance.getCharmingEntityId()));
        }

        return nbt;
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
    public void readNBT(Capability<IAOTDPlayerSpellCharmData> capability, IAOTDPlayerSpellCharmData instance, EnumFacing side, NBTBase nbt)
    {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt instanceof NBTTagCompound)
        {
            // The compound to read from
            NBTTagCompound compound = (NBTTagCompound) nbt;

            instance.setCharmTicks(compound.getInteger(NBT_CHARM_TICKS));
            if (compound.hasKey(NBT_CHARMING_ENTITY))
            {
                instance.setCharmingEntityId(NBTUtil.getUUIDFromTag(compound.getCompoundTag(NBT_CHARMING_ENTITY)));
            }
        }
        // There's an error, this should not be possible
        else
        {
            AfraidOfTheDark.INSTANCE.getLogger().error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!");
        }
    }
}
