package com.davidm1a2.afraidofthedark.common.capabilities.player.research

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage

/**
 * Default storage implementation for AOTD player research
 */
class AOTDPlayerResearchStorage : IStorage<IAOTDPlayerResearch>
{
    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    override fun writeNBT(capability: Capability<IAOTDPlayerResearch>, instance: IAOTDPlayerResearch, side: EnumFacing?): NBTBase?
    {
        // Create a compound to write
        val compound = NBTTagCompound()
        // Write each researches name as a key with true/false as the value
        for (research in ModRegistries.RESEARCH)
            compound.setBoolean(research.registryName.toString(), instance.isResearched(research))
        return compound
    }

    /**
     * Called to read the NBTTagCompound into a capability
     *
     * @param capability The capability that is being read
     * @param instance   The instance to of the capability to read
     * @param side       ignored
     * @param nbt        An NBTTagCompound that contains all info about the capability
     */
    override fun readNBT(capability: Capability<IAOTDPlayerResearch>,
                         instance: IAOTDPlayerResearch,
                         side: EnumFacing?,
                         nbt: NBTBase)
    {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is NBTTagCompound)
        {
            // For each research if we have researched it unlock that research in our instance
            for (research in ModRegistries.RESEARCH) instance.setResearch(research, nbt.getBoolean(research.registryName.toString()))
        }
        else
        {
            AfraidOfTheDark.INSTANCE.logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }
}