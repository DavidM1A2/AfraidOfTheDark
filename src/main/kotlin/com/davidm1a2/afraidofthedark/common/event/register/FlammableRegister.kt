package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.Blocks
import net.minecraft.block.FireBlock

/**
 * Registers which blocks can burn
 */
object FlammableRegister {
    fun register() {
        val fire = Blocks.FIRE as FireBlock

        fire.setFireInfo(ModBlocks.GRAVEWOOD, 5, 5)
        fire.setFireInfo(ModBlocks.MANGROVE, 5, 5)
        fire.setFireInfo(ModBlocks.SACRED_MANGROVE, 5, 5)

        fire.setFireInfo(ModBlocks.GRAVEWOOD_PLANKS, 5, 20)
        fire.setFireInfo(ModBlocks.MANGROVE_PLANKS, 5, 20)
        fire.setFireInfo(ModBlocks.SACRED_MANGROVE_PLANKS, 5, 20)

        fire.setFireInfo(ModBlocks.GRAVEWOOD_SLAB, 5, 20)
        fire.setFireInfo(ModBlocks.MANGROVE_SLAB, 5, 20)
        fire.setFireInfo(ModBlocks.SACRED_MANGROVE_SLAB, 5, 20)

        fire.setFireInfo(ModBlocks.GRAVEWOOD_FENCE, 5, 20)
        fire.setFireInfo(ModBlocks.MANGROVE_FENCE, 5, 20)
        fire.setFireInfo(ModBlocks.SACRED_MANGROVE_FENCE, 5, 20)

        fire.setFireInfo(ModBlocks.GRAVEWOOD_FENCE_GATE, 5, 20)
        fire.setFireInfo(ModBlocks.MANGROVE_FENCE_GATE, 5, 20)
        fire.setFireInfo(ModBlocks.SACRED_MANGROVE_FENCE_GATE, 5, 20)

        fire.setFireInfo(ModBlocks.GRAVEWOOD_STAIRS, 5, 20)
        fire.setFireInfo(ModBlocks.MANGROVE_STAIRS, 5, 20)
        fire.setFireInfo(ModBlocks.SACRED_MANGROVE_STAIRS, 5, 20)

        fire.setFireInfo(ModBlocks.GRAVEWOOD_LEAVES, 30, 60)
        fire.setFireInfo(ModBlocks.MANGROVE_LEAVES, 30, 60)
        fire.setFireInfo(ModBlocks.SACRED_MANGROVE_LEAVES, 30, 60)
    }
}