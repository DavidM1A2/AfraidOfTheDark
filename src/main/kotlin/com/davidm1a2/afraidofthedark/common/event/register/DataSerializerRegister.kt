package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import net.minecraft.network.datasync.DataSerializers

object DataSerializerRegister {
    fun register() {
        ModDataSerializers.LIST.forEach(DataSerializers::registerSerializer)
    }
}