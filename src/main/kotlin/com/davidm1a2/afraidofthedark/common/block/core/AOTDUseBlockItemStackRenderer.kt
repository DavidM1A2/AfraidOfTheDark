package com.davidm1a2.afraidofthedark.common.block.core

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer
import java.util.concurrent.Callable
import java.util.function.Supplier

/**
 * Interface we can implement to tell the registerer to use a custom itemstack renderer
 */
interface AOTDUseBlockItemStackRenderer {
    fun getWrappedISTER(): Supplier<Callable<ItemStackTileEntityRenderer>> {
        return Supplier {
            Callable {
                getISTER()
            }
        }
    }

    fun getISTER(): ItemStackTileEntityRenderer
}