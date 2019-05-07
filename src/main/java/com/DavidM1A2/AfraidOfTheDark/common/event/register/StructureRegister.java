package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModStructures;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class used to register our structures into the game
 */
public class StructureRegister
{
    /**
     * Called to register all of our mod's structures into the game
     *
     * @param event The event with which we register structures into the game with
     */
    @SubscribeEvent
    public void registerStructures(RegistryEvent.Register<Structure> event)
    {
        // Grab our structure registry
        IForgeRegistry<Structure> structureRegistry = event.getRegistry();
        // Add all mod structures
        structureRegistry.registerAll(ModStructures.STRUCTURE_LIST);
    }
}
