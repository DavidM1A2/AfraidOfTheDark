package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.nbt.INBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import org.apache.logging.log4j.LogManager

/**
 * Default storage implementation for the AOTD charm spell effect data
 */
class AOTDPlayerSpellCharmDataStorage : IStorage<IAOTDPlayerSpellCharmData> {
    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    override fun writeNBT(
        capability: Capability<IAOTDPlayerSpellCharmData>,
        instance: IAOTDPlayerSpellCharmData,
        side: EnumFacing?
    ): INBTBase {
        // Create a compound to write
        val nbt = NBTTagCompound()
        nbt.setInt(NBT_CHARM_TICKS, instance.charmTicks)
        instance.charmingEntityId?.let { nbt.setTag(NBT_CHARMING_ENTITY, NBTUtil.writeUniqueId(it)) }
        return nbt
    }

    /**
     * Called to read the NBTTagCompound into a capability
     *
     * @param capability The capability that is being read
     * @param instance   The instance to of the capability to read
     * @param side       ignored
     * @param nbt        An NBTTagCompound that contains all info about the capability
     */
    override fun readNBT(
        capability: Capability<IAOTDPlayerSpellCharmData>,
        instance: IAOTDPlayerSpellCharmData,
        side: EnumFacing?,
        nbt: INBTBase
    ) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is NBTTagCompound) {
            instance.charmTicks = nbt.getInt(NBT_CHARM_TICKS)

            if (nbt.hasKey(NBT_CHARMING_ENTITY)) {
                instance.charmingEntityId = NBTUtil.readUniqueId(nbt.getCompound(NBT_CHARMING_ENTITY))
            } else {
                instance.charmingEntityId = null
            }
        } else {
            logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private val logger = LogManager.getLogger()

        // NBT constants used for serialization
        private const val NBT_CHARM_TICKS = "charm_ticks"
        private const val NBT_CHARMING_ENTITY = "charming_entity"
    }
}