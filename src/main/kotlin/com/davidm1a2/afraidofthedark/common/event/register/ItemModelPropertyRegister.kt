package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.item.core.IHasModelProperties
import net.minecraft.item.ItemModelsProperties
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

class ItemModelPropertyRegister {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    fun fmlClientSetupEvent(event: FMLClientSetupEvent) {
        event.enqueueWork {
            ModItems.ITEM_LIST
                .filter { it is IHasModelProperties }
                .forEach {
                    val properties = (it as IHasModelProperties).getProperties()
                    for (property in properties) {
                        ItemModelsProperties.register(it, property.first, property.second)
                    }
                }
        }
    }
}