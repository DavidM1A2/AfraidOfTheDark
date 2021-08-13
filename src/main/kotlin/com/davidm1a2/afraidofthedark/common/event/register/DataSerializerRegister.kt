package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModDataSerializers
import net.minecraft.network.datasync.DataSerializers
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

class DataSerializerRegister {
    @SubscribeEvent
    fun commonSetupEvent(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            ModDataSerializers.LIST.forEach(DataSerializers::registerSerializer)
        }
    }
}