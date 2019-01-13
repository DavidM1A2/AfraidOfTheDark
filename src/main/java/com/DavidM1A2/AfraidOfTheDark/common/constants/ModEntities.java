package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

/**
 * A static class containing all of our entity references for us
 */
public class ModEntities
{
	// Various entity IDs
	public static final int ENCHANTED_SKELETON_ID = 7;

	// All mod entity static fields
	public static final EntityEntry ENCHANTED_SKELETON = EntityEntryBuilder.create()
			.egg(0x996600, 0xe69900)
			.entity(EntityEnchantedSkeleton.class)
			.id(new ResourceLocation(Constants.MOD_ID, "enchanted_skeleton"), ENCHANTED_SKELETON_ID)
			.name("enchanted_skeleton")
			.tracker(50, 1, true)
			.build();

	// An array containing a list of entities that AOTD adds
	public static EntityEntry[] ENTITY_LIST = new EntityEntry[]
	{
		ENCHANTED_SKELETON
	};
}
