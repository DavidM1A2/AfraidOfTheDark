package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.nbt.DoubleNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability

class PlayerSpellLunarDataStorage : Capability.IStorage<IPlayerSpellLunarData> {
    override fun writeNBT(capability: Capability<IPlayerSpellLunarData>, instance: IPlayerSpellLunarData, side: Direction?): INBT {
        return DoubleNBT.valueOf(instance.vitae)
    }

    override fun readNBT(capability: Capability<IPlayerSpellLunarData>, instance: IPlayerSpellLunarData, side: Direction?, nbt: INBT) {
        if (nbt is DoubleNBT) {
            instance.vitae = nbt.asDouble
        }
    }
}