package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.registries.ForgeRegistries

class StructureMapNode : INBTSerializable<CompoundNBT> {
    var upperLeftChild: StructureMapNode? = null
    var upperRightChild: StructureMapNode? = null
    var lowerLeftChild: StructureMapNode? = null
    var lowerRightChild: StructureMapNode? = null

    private var structure: ResourceLocation? = null
    private var structurePos: BlockPos? = null

    fun insertStructure(structure: AOTDStructure<*>, structurePos: BlockPos?) {
        this.structure = structure.registryName
        this.structurePos = structurePos
    }

    fun hasStructure(): Boolean {
        return structure != null
    }

    fun getStructure(): AOTDStructure<*>? {
        return structure?.let { ForgeRegistries.STRUCTURE_FEATURES.getValue(it) as? AOTDStructure<*> }
    }

    fun getStructurePos(): BlockPos? {
        return this.structurePos
    }

    override fun serializeNBT(): CompoundNBT {
        val nbt = CompoundNBT()

        structure?.let { nbt.putString(NBT_STRUCTURE, it.toString()) }
        structurePos?.let { nbt.put(NBT_STRUCTURE_POSITION, NBTUtil.writeBlockPos(it)) }

        upperLeftChild?.let { nbt.put(NBT_UPPER_LEFT_CHILD, it.serializeNBT()) }
        upperRightChild?.let { nbt.put(NBT_UPPER_RIGHT_CHILD, it.serializeNBT()) }
        lowerLeftChild?.let { nbt.put(NBT_LOWER_LEFT_CHILD, it.serializeNBT()) }
        lowerRightChild?.let { nbt.put(NBT_LOWER_RIGHT_CHILD, it.serializeNBT()) }

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        structure = if (nbt.contains(NBT_STRUCTURE)) {
            ResourceLocation(nbt.getString(NBT_STRUCTURE))
        } else {
            null
        }
        structurePos = if (nbt.contains(NBT_STRUCTURE_POSITION)) {
            NBTUtil.readBlockPos(nbt.getCompound(NBT_STRUCTURE_POSITION))
        } else {
            null
        }

        upperLeftChild = if (nbt.contains(NBT_UPPER_LEFT_CHILD)) {
            StructureMapNode().also { deserializeNBT(nbt.getCompound(NBT_UPPER_LEFT_CHILD)) }
        } else {
            null
        }
        upperRightChild = if (nbt.contains(NBT_UPPER_RIGHT_CHILD)) {
            StructureMapNode().also { deserializeNBT(nbt.getCompound(NBT_UPPER_RIGHT_CHILD)) }
        } else {
            null
        }
        lowerLeftChild = if (nbt.contains(NBT_LOWER_LEFT_CHILD)) {
            StructureMapNode().also { deserializeNBT(nbt.getCompound(NBT_LOWER_LEFT_CHILD)) }
        } else {
            null
        }
        lowerRightChild = if (nbt.contains(NBT_LOWER_RIGHT_CHILD)) {
            StructureMapNode().also { deserializeNBT(nbt.getCompound(NBT_LOWER_RIGHT_CHILD)) }
        } else {
            null
        }
    }

    companion object {
        private const val NBT_STRUCTURE = "structure"
        private const val NBT_STRUCTURE_POSITION = "structure_position"
        private const val NBT_UPPER_LEFT_CHILD = "upper_left_child"
        private const val NBT_UPPER_RIGHT_CHILD = "upper_right_child"
        private const val NBT_LOWER_LEFT_CHILD = "lower_left_child"
        private const val NBT_LOWER_RIGHT_CHILD = "lower_right_child"
    }
}