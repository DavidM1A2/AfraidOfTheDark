package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.structure.StructureIO
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import net.minecraftforge.registries.ForgeRegistries

/**
 * Class used to register our structures into the game
 */
object StructureRegister {
    private val REGISTER_STRUCTURE = ObfuscationReflectionHelper.findMethod(StructureIO::class.java, "func_143034_b", Class::class.java, String::class.java)

    fun register() {
        // Register the structure pieces
        for ((name, clazz) in ModStructures.STRUCTURE_PIECES) {
            StructureIO.registerStructureComponent(clazz, name.toString())
        }

        // Register each structure feature
        for (structure in ModStructures.STRUCTURES) {
            Feature.STRUCTURES[structure.structureName] = structure
        }
        // Register each structure in StructureIO
        for ((structure, structureStart) in ModStructures.STRUCTURE_STARTS) {
            REGISTER_STRUCTURE.invoke(null, structureStart, structure.structureName)
        }

        ForgeRegistries.BIOMES.forEach {
            ModStructures.STRUCTURES.forEach { structure -> structure.setupStructureIn(it) }
        }
    }
}