package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import org.apache.logging.log4j.LogManager

class PlayerSpellInnateDataStorage : Capability.IStorage<IPlayerSpellInnateData> {
    override fun writeNBT(capability: Capability<IPlayerSpellInnateData>, instance: IPlayerSpellInnateData, side: Direction?): INBT {
        val compound = CompoundNBT()

        compound.putDouble("vitae", instance.vitae)

        return compound
    }

    override fun readNBT(capability: Capability<IPlayerSpellInnateData>, instance: IPlayerSpellInnateData, side: Direction?, nbt: INBT) {
        if (nbt is CompoundNBT) {
            instance.vitae = nbt.getDouble("vitae")
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}