package com.DavidM1A2.afraidofthedark.common.registry.bolt;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.EntityBolt;
import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.function.BiFunction;

/**
 * Base class for all AOtD bolt entries
 */
public class AOTDBoltEntry extends BoltEntry
{
	/**
	 * The constructor sets the class fields
	 *
	 * @param name The name this bolt entry will have in the registry
	 * @param boltItem The item that this bolt entry represents
	 * @param boltEntityFactory The entity that this bolt entry represents
	 * @param preRequisite The research that is required to use this bolt
	 */
	public AOTDBoltEntry(String name, Item boltItem, BiFunction<World, EntityPlayer, EntityBolt> boltEntityFactory, Research preRequisite)
	{
		super(boltItem, boltEntityFactory, preRequisite);
		this.setRegistryName(new ResourceLocation(Constants.MOD_ID, name));
	}
}
