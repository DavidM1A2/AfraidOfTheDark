package com.davidm1a2.afraidofthedark.common.block.core

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.IItemRenderProperties
import java.util.function.Consumer

class AOTDCustomRendererBlockItem(block: Block, private val renderer: BlockEntityWithoutLevelRenderer, properties: Properties) : BlockItem(block, properties) {
    override fun initializeClient(consumer: Consumer<IItemRenderProperties>) {
        consumer.accept(object : IItemRenderProperties {
            override fun getItemStackRenderer(): BlockEntityWithoutLevelRenderer {
                return renderer
            }
        })
    }
}