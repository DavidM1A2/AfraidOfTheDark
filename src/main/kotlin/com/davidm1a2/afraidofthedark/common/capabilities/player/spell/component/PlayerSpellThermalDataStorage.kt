package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import org.apache.logging.log4j.LogManager

class PlayerSpellThermalDataStorage : Capability.IStorage<IPlayerSpellThermalData> {
    override fun writeNBT(capability: Capability<IPlayerSpellThermalData>, instance: IPlayerSpellThermalData, side: Direction?): INBT {
        val compound = CompoundNBT()

        compound.putDouble("vitae", instance.vitae)
        compound.putDouble("heat", instance.heat)

        return compound
    }

    override fun readNBT(capability: Capability<IPlayerSpellThermalData>, instance: IPlayerSpellThermalData, side: Direction?, nbt: INBT) {
        if (nbt is CompoundNBT) {
            instance.vitae = nbt.getDouble("vitae")
            instance.heat = nbt.getDouble("heat")
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}