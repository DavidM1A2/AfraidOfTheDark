package com.davidm1a2.afraidofthedark.common.item.blockitem

import com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor.VitaeExtractorItemStackRenderer
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.world.item.BlockItem
import net.minecraftforge.client.IItemRenderProperties
import java.util.function.Consumer

class VitaeExtractorBlockItem : BlockItem(ModBlocks.VITAE_EXTRACTOR, Properties().tab(Constants.AOTD_CREATIVE_TAB)) {
    override fun initializeClient(consumer: Consumer<IItemRenderProperties>) {
        consumer.accept(object : IItemRenderProperties {
            private val renderer = VitaeExtractorItemStackRenderer()

            override fun getItemStackRenderer(): BlockEntityWithoutLevelRenderer {
                return renderer
            }
        })
    }
}