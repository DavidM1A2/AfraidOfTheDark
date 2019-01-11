package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.item.ItemDebug;
import com.DavidM1A2.afraidofthedark.common.item.ItemEnchnatedSkeletonBone;
import com.DavidM1A2.afraidofthedark.common.item.ItemJournal;
import net.minecraft.item.Item;

/**
 * A static class containing all of our item references for us
 */
public class ModItems
{
	public static final Item JOURNAL = new ItemJournal();
	public static final Item ENCHANTED_SKELETON_BONE = new ItemEnchnatedSkeletonBone();

	public static final Item DEBUG = new ItemDebug();

	// An array containing a list of items that AOTD adds
	public static Item[] ITEM_LIST = new Item[]
	{
		JOURNAL,
		ENCHANTED_SKELETON_BONE,
		DEBUG
	};
}
