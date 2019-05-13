package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.*;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.structure.base.Structure;

/**
 * A list of structures to be registered
 */
public class ModStructures
{
    public static final Structure CRYPT = new StructureCrypt();
    public static final Structure WITCH_HUT = new StructureWitchHut();
    public static final Structure VOID_CHEST = new StructureVoidChest();
    public static final Structure DARK_FOREST = new StructureDarkForest();
    public static final Structure NIGHTMARE_ISLAND = new StructureNightmareIsland();
    public static final Structure GNOMISH_CITY = new StructureGnomishCity();

    public static Structure[] STRUCTURE_LIST = new Structure[]
            {
                    CRYPT,
                    WITCH_HUT,
                    VOID_CHEST,
                    DARK_FOREST,
                    NIGHTMARE_ISLAND,
                    GNOMISH_CITY
            };
}
