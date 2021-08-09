package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class used to register our structures into the game
 */
class StructureRegister {
    /**
     * Called by forge to register any of our structures
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerItems(event: RegistryEvent.Register<Structure<*>>) {
        val registry = event.registry

        ModStructures.STRUCTURE_PIECES.forEach {
            Registry.register(Registry.STRUCTURE_PIECE, it.first.toString(), it.second)
        }

        registry.registerAll(*ModStructures.STRUCTURES)
    }
}