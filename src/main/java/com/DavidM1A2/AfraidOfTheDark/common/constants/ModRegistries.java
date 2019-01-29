package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.registry.bolt.BoltEntry;
import com.DavidM1A2.afraidofthedark.common.registry.meteor.MeteorEntry;
import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class containing references to any registries AOTD adds. We can't actually init the registry here though, so that is done
 * inside RegisterRegistries
 */
public class ModRegistries
{
	// Fields that are unchanged and basically final (just not initialized here) representing the registries we are adding. Initialized from RegistryRegister
	public static IForgeRegistry<Structure> STRUCTURE;
	public static IForgeRegistry<Research> RESEARCH;
	public static IForgeRegistry<BoltEntry> BOLTS;
	public static IForgeRegistry<MeteorEntry> METEORS;
}
