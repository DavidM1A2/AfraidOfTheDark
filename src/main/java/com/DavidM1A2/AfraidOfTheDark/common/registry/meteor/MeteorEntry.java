package com.DavidM1A2.afraidofthedark.common.registry.meteor;

import com.DavidM1A2.afraidofthedark.common.registry.bolt.BoltEntry;
import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Base class for all meteor entries, meteor entries are used to define new meteor properties
 */
public abstract class MeteorEntry extends IForgeRegistryEntry.Impl<MeteorEntry>
{
	// The resource location containing the icon that this meteor type will use in the telescope
	private final ResourceLocation icon;
	// The minimum radius of the meteor
	private final int minMeteorRadius;
	// The maximum radius of the meteor
	private final int maxMeteorRadius;
	// The block that the meteor uses on the inside
	private final Block interiorBlock;
	// The pre-requisite research to be able to see this type of meteor
	private final Research preRequisite;

	/**
	 * Constructor just initializes fields
	 *
	 * @param icon The resource location containing the icon that this meteor type will use in the telescope
	 * @param minMeteorRadius The minimum radius of the meteor
	 * @param maxMeteorRadius The maximum radius of the meteor
	 * @param interiorBlock The block that the meteor uses on the inside
	 * @param preRequisite The pre-requisite research to be able to see this type of meteor
	 */
	public MeteorEntry(ResourceLocation icon, int minMeteorRadius, int maxMeteorRadius, Block interiorBlock, Research preRequisite)
	{
		// Initialize all fields
		this.icon = icon;
		this.minMeteorRadius = minMeteorRadius;
		this.maxMeteorRadius = maxMeteorRadius;
		this.interiorBlock = interiorBlock;
		this.preRequisite = preRequisite;
	}

	/**
	 * @return The localized name of the meteor entry
	 */
	public String getLocalizedName()
	{
		return I18n.format("meteor_entry." + this.getRegistryName().getResourcePath());
	}

	///
	/// Getters for all fields
	///

	public ResourceLocation getIcon()
	{
		return this.icon;
	}

	public int getMinMeteorRadius()
	{
		return this.minMeteorRadius;
	}

	public int getMaxMeteorRadius()
	{
		return this.maxMeteorRadius;
	}

	public Block getInteriorBlock()
	{
		return this.interiorBlock;
	}

	public Research getPreRequisite()
	{
		return this.preRequisite;
	}
}
