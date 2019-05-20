package com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * Default storage implementation for AOTD nightmare data
 */
public class AOTDPlayerNightmareDataStorage implements Capability.IStorage<IAOTDPlayerNightmareData>
{
    // Constant IDs used in NBT
    private static final String NBT_POSITIONAL_INDEX = "positional_index";
    private static final String NBT_PRE_TELEPORT_INVENTORY = "pre_teleport_inventory";
    private static final String NBT_PRE_TELEPORT_POSITION = "pre_teleport_position";
    private static final String NBT_PRE_TELEPORT_DIMENSION_ID = "pre_teleport_dimension_id";

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
    public NBTBase writeNBT(Capability<IAOTDPlayerNightmareData> capability, IAOTDPlayerNightmareData instance, EnumFacing side)
    {
        // Create a compound to write
        NBTTagCompound compound = new NBTTagCompound();

        compound.setInteger(NBT_POSITIONAL_INDEX, instance.getPositionalIndex());
        if (instance.getPreTeleportPlayerInventory() != null)
        {
            compound.setTag(NBT_PRE_TELEPORT_INVENTORY, instance.getPreTeleportPlayerInventory());
        }
        if (instance.getPreTeleportPosition() != null)
        {
            compound.setTag(NBT_PRE_TELEPORT_POSITION, NBTUtil.createPosTag(instance.getPreTeleportPosition()));
        }
        compound.setInteger(NBT_PRE_TELEPORT_DIMENSION_ID, instance.getPreTeleportDimensionID());

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
    public void readNBT(Capability<IAOTDPlayerNightmareData> capability, IAOTDPlayerNightmareData instance, EnumFacing side, NBTBase nbt)
    {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt instanceof NBTTagCompound)
        {
            // The compound to read from
            NBTTagCompound compound = (NBTTagCompound) nbt;

            instance.setPositionalIndex(compound.getInteger(NBT_POSITIONAL_INDEX));
            if (compound.hasKey(NBT_PRE_TELEPORT_INVENTORY))
            {
                instance.setPreTeleportPlayerInventory(compound.getTagList(NBT_PRE_TELEPORT_INVENTORY, Constants.NBT.TAG_COMPOUND));
            }
            else
            {
                instance.setPreTeleportPlayerInventory(null);
            }
            if (compound.hasKey(NBT_PRE_TELEPORT_POSITION))
            {
                instance.setPreTeleportPosition(NBTUtil.getPosFromTag((NBTTagCompound) compound.getTag(NBT_PRE_TELEPORT_POSITION)));
            }
            else
            {
                instance.setPreTeleportPosition(null);
            }
            instance.setPreTeleportDimensionID(compound.getInteger(NBT_PRE_TELEPORT_DIMENSION_ID));
        }
        // There's an error, this should not be possible
        else
        {
            AfraidOfTheDark.INSTANCE.getLogger().error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!");
        }
    }
}
