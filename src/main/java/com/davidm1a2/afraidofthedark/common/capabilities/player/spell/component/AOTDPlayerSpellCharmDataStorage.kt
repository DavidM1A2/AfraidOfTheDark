package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage

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
    ): NBTBase? {
        // Create a compound to write
        val nbt = NBTTagCompound()
        nbt.setInteger(NBT_CHARM_TICKS, instance.charmTicks)
        instance.charmingEntityId?.let { nbt.setTag(NBT_CHARMING_ENTITY, NBTUtil.createUUIDTag(it)) }
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
        nbt: NBTBase
    ) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is NBTTagCompound) {
            instance.charmTicks = nbt.getInteger(NBT_CHARM_TICKS)

            if (nbt.hasKey(NBT_CHARMING_ENTITY)) {
                instance.charmingEntityId = NBTUtil.getUUIDFromTag(nbt.getCompoundTag(NBT_CHARMING_ENTITY))
            } else {
                instance.charmingEntityId = null
            }
        } else {
            AfraidOfTheDark.INSTANCE.logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        // NBT constants used for serialization
        private const val NBT_CHARM_TICKS = "charm_ticks"
        private const val NBT_CHARMING_ENTITY = "charming_entity"
    }
}