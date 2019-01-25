package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.item.ItemBladeOfExhumation;
import com.DavidM1A2.afraidofthedark.common.item.ItemDebug;
import com.DavidM1A2.afraidofthedark.common.item.ItemEnchantedSkeletonBone;
import com.DavidM1A2.afraidofthedark.common.item.ItemJournal;
import com.DavidM1A2.afraidofthedark.common.item.crossbow.ItemCrossbow;
import com.DavidM1A2.afraidofthedark.common.item.crossbow.bolts.*;
import net.minecraft.item.Item;

/**
 * A static class containing all of our item references for us
 */
public class ModItems
{
	public static final Item JOURNAL = new ItemJournal();
	public static final Item ENCHANTED_SKELETON_BONE = new ItemEnchantedSkeletonBone();
	public static final Item BLADE_OF_EXHUMATION = new ItemBladeOfExhumation();
	public static final Item CROSSBOW = new ItemCrossbow();

	public static final Item WOODEN_BOLT = new ItemWoodenBolt();
	public static final Item IRON_BOLT = new ItemIronBolt();
	public static final Item SILVER_BOLT = new ItemSilverBolt();
	public static final Item IGNEOUS_BOLT = new ItemIgneousBolt();
	public static final Item STAR_METAL_BOLT = new ItemStarMetalBolt();

	public static final Item DEBUG = new ItemDebug();

	// An array containing a list of items that AOTD adds
	public static Item[] ITEM_LIST = new Item[]
	{
		JOURNAL,
		ENCHANTED_SKELETON_BONE,
		BLADE_OF_EXHUMATION,
		CROSSBOW,
		WOODEN_BOLT,
		IRON_BOLT,
		SILVER_BOLT,
		IGNEOUS_BOLT,
		STAR_METAL_BOLT,
		DEBUG
	};
}
