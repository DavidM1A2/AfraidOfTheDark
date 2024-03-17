package com.davidm1a2.afraidofthedark.common.block.core

import net.minecraft.client.renderer.entity.ItemRenderer
import java.util.concurrent.Callable
import java.util.function.Supplier

/**
 * Interface we can implement to tell the registerer to use a custom itemstack renderer
 */
interface IUseBlockItemStackRenderer {
    fun getWrappedISTER(): Supplier<Callable<ItemRenderer>> {
        return Supplier {
            Callable {
                getItemRenderer()
            }
        }
    }

    fun getItemRenderer(): ItemRenderer
}