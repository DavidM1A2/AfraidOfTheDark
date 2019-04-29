package com.DavidM1A2.afraidofthedark.common.capabilities.world;

import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

/**
 * Class representing a structure that has been placed in the world with an ID, position, and data
 */
public class PlacedStructure implements INBTSerializable<NBTTagCompound>
{
    // NBT Tag constants for name, position, uuid, and data
    private static final String NBT_STRUCTURE_NAME = "structure_name";
    private static final String NBT_POSITION = "position";
    private static final String NBT_UUID = "uuid";
    private static final String NBT_DATA = "data";

    // The structure that this placed structure is based on
    private Structure structure;
    // The position that this structure is placed at
    private BlockPos position;
    // The UUID of this placed structure
    private UUID uuid;
    // The NBT tag compound with additional data needed for this structure to generate
    private NBTTagCompound data;

    /**
     * Constructor needs to know structure and position of this placed structure
     *
     * @param structure The structure that was placed
     * @param position The position that the structure was placed at
     * @param data The additional data this structure requires to be generated
     */
    PlacedStructure(Structure structure, BlockPos position, NBTTagCompound data)
    {
        this.structure = structure;
        this.position = position;
        this.uuid = UUID.randomUUID();
        this.data = data;
    }

    /**
     * Constructor that initializes this structure from NBT
     *
     * @param nbtTagCompound The NBT compound with data that this structure requires
     */
    PlacedStructure(NBTTagCompound nbtTagCompound)
    {
        this.deserializeNBT(nbtTagCompound);
    }

    /**
     * Writes this structure to a new NBT compound
     *
     * @return An nbt tag compound with all the structure's information
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        // Write the structure name, position, uuid, and additional data
        compound.setString(NBT_STRUCTURE_NAME, this.structure.getRegistryName().toString());
        compound.setTag(NBT_POSITION, NBTUtil.createPosTag(this.position));
        compound.setTag(NBT_UUID, NBTUtil.createUUIDTag(this.uuid));
        compound.setTag(NBT_DATA, this.data);
        return compound;
    }

    /**
     * Reads the object's state in from the supplied NBT compound
     *
     * @param compound The compound to read state from
     */
    @Override
    public void deserializeNBT(NBTTagCompound compound)
    {
        // Read the structure, position, uuid, and data from the compound
        this.structure = ModRegistries.STRUCTURE.getValue(new ResourceLocation(compound.getString(NBT_STRUCTURE_NAME)));
        this.position = NBTUtil.getPosFromTag(compound.getCompoundTag(NBT_POSITION));
        this.uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag(NBT_UUID));
        this.data = compound.getCompoundTag(NBT_DATA);
    }

    /**
     * @return The structure that this placed structure represents
     */
    public Structure getStructure()
    {
        return structure;
    }

    /**
     * @return The position this structure was placed at
     */
    public BlockPos getPosition()
    {
        return position;
    }

    /**
     * @return The UUID of this structure
     */
    public UUID getUUID()
    {
        return uuid;
    }

    /**
     * @return The NBT data associated with this placed structure
     */
    public NBTTagCompound getData()
    {
        return data;
    }
}
