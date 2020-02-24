package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.Structure
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Class used to register our structures into the game
 */
class StructureRegister {
    /**
     * Called to register all of our mod's structures into the game
     *
     * @param event The event with which we register structures into the game with
     */
    @SubscribeEvent
    fun registerStructures(event: RegistryEvent.Register<Structure>) {
        // Grab our structure registry
        val structureRegistry = event.registry

        // Add all mod structures
        structureRegistry.registerAll(*ModStructures.STRUCTURE_LIST)
    }
}