package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.dimension.NoopTeleporter;
import com.DavidM1A2.afraidofthedark.common.dimension.voidChest.VoidChestWorldProvider;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ITeleporter;
import org.apache.commons.lang3.StringUtils;

/**
 * Mod dimensions class initializes dimension types used in AOTD
 */
public class ModDimensions
{
	// Teleporter class used by all dimension teleportation, this does nothing
	public static final ITeleporter NOOP_TELEPORTER = new NoopTeleporter();

	public static final DimensionType VOID_CHEST = DimensionType.register("Void Chest", StringUtils.EMPTY, DimensionManager.getNextFreeDimId(), VoidChestWorldProvider.class, false);

	public static final DimensionType[] DIMENSION_LIST = new DimensionType[]
	{
		VOID_CHEST
	};
}
