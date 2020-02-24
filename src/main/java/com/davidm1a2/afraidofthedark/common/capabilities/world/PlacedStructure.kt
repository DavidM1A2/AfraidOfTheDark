package com.davidm1a2.afraidofthedark.common.capabilities.world

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.Structure
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.util.INBTSerializable
import java.util.*

/**
 * Class representing a structure that has been placed in the world with an ID, and data
 *
 * @property structure The structure that this placed structure is based on
 * @property uuid The UUID of this placed structure
 * @property data The NBT tag compound with additional data needed for this structure to generate
 */
class PlacedStructure : INBTSerializable<NBTTagCompound> {
    lateinit var structure: Structure
        private set
    lateinit var uuid: UUID
        private set
    lateinit var data: NBTTagCompound
        private set

    /**
     * Constructor needs to know structure and position of this placed structure
     *
     * @param structure The structure that was placed
     * @param data      The additional data this structure requires to be generated
     */
    internal constructor(structure: Structure, data: NBTTagCompound) {
        this.structure = structure
        uuid = UUID.randomUUID()
        this.data = data
    }

    /**
     * Constructor that initializes this structure from NBT
     *
     * @param nbtTagCompound The NBT compound with data that this structure requires
     */
    internal constructor(nbtTagCompound: NBTTagCompound) {
        deserializeNBT(nbtTagCompound)
    }

    /**
     * Writes this structure to a new NBT compound
     *
     * @return An nbt tag compound with all the structure's information
     */
    override fun serializeNBT(): NBTTagCompound {
        val compound = NBTTagCompound()
        // Write the structure name, position, uuid, and additional data
        compound.setString(NBT_STRUCTURE_NAME, structure.registryName.toString())
        compound.setTag(NBT_UUID, NBTUtil.createUUIDTag(uuid))
        compound.setTag(NBT_DATA, data)
        return compound
    }

    /**
     * Reads the object's state in from the supplied NBT compound
     *
     * @param compound The compound to read state from
     */
    override fun deserializeNBT(compound: NBTTagCompound) {
        // Read the structure, position, uuid, and data from the compound
        structure = ModRegistries.STRUCTURE.getValue(ResourceLocation(compound.getString(NBT_STRUCTURE_NAME)))!!
        uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag(NBT_UUID))
        data = compound.getCompoundTag(NBT_DATA)
    }

    companion object {
        // NBT Tag constants for name, position, uuid, and data
        private const val NBT_STRUCTURE_NAME = "structure_name"
        private const val NBT_UUID = "uuid"
        private const val NBT_DATA = "data"
    }
}