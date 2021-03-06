package com.davidm1a2.afraidofthedark.common.world.structure.base

import net.minecraft.world.World
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraftforge.fml.common.ObfuscationReflectionHelper

private val WORLD_FIELD = ObfuscationReflectionHelper.findField(ChunkGenerator::class.java, "field_222540_a")

internal fun <T : GenerationSettings> ChunkGenerator<T>.getWorld(): World {
    return WORLD_FIELD[this] as World
}