package com.DavidM1A2.afraidofthedark.common.utility;

import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityBolt;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityIronBolt;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityWoodenBolt;
import com.DavidM1A2.afraidofthedark.common.research.base.Research;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import java.util.function.BiFunction;

/**
 * Utility class that contains a list of bolt type definitions
 */
public enum AOTDBoltHelper
{
	WOODEN("Wooden", ModItems.WOODEN_BOLT, EntityWoodenBolt::new, null),
	IRON("Iron", ModItems.IRON_BOLT, EntityIronBolt::new, null);/*
	SILVER("Silver", null, null, null),
	IGNEOUS("Igneous", null, null, null),
	STAR_METAL("Star Metal", null, null, null);*/

	// The name of the bolt type
	private final String name;
	// The item that this bolt type is represented by
	private final Item item;
	// The factory to make entity bolts from a world object
	private final BiFunction<World, EntityPlayer, EntityBolt> entityFactory;
	// The pre-requisite research that needs to be researched for this to be used
	private final Research preRequisite;

	/**
	 * Constructor initializes fields
	 *
	 * @param name The name of the bolt type
	 * @param item The item that this bolt type is represented by
	 * @param entityFactory The factory to make entity bolts from a world object
	 * @param preRequisite The pre-requisite research that needs to be researched for this to be used
	 */
	AOTDBoltHelper(String name, Item item, BiFunction<World, EntityPlayer, EntityBolt> entityFactory, Research preRequisite)
	{
		this.name = name;
		this.item = item;
		this.entityFactory = entityFactory;
		this.preRequisite = preRequisite;
	}

	/**
	 * Creates an entity of a specified bolt given a world to create in
	 *
	 * @param world The world to spawn from
	 * @param entityPlayer The player shooting the bolt
	 * @return The entity bolt that was created
	 */
	public EntityBolt createEntity(World world, EntityPlayer entityPlayer)
	{
		return this.entityFactory.apply(world, entityPlayer);
	}

	/**
	 * @return The name of the bolt type
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @return The item that this bolt type is represented by
	 */
	public Item getItem()
	{
		return this.item;
	}

	/**
	 * @return The pre-requisite research that needs to be researched for this to be used
	 */
	public Research getPreRequisite()
	{
		return this.preRequisite;
	}
}
