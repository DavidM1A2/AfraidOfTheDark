package com.davidm1a2.afraidofthedark.common.constants;

import com.davidm1a2.afraidofthedark.common.dimension.NoopTeleporter;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.util.ITeleporter;

/**
 * Mod dimensions class initializes dimension types used in AOTD
 */
public class ModDimensions
{
    // Teleporter class used by all dimension teleportation, this does nothing
    public static final ITeleporter NOOP_TELEPORTER = new NoopTeleporter();

    public static DimensionType VOID_CHEST;
    public static DimensionType NIGHTMARE;
}
