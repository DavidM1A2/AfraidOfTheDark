package com.DavidM1A2.afraidofthedark.common.registry.meteor;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
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
	// What percent of the meteor is ore vs meteor block
	private final double richnessPercent;
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
	 * @param richnessPercent What percent of the meteor is ore vs meteor block, 1.0 indicates all ore, 0.0 indicates no ore
	 * @param interiorBlock The block that the meteor uses on the inside
	 * @param preRequisite The pre-requisite research to be able to see this type of meteor
	 */
	public MeteorEntry(ResourceLocation icon, int minMeteorRadius, int maxMeteorRadius, double richnessPercent, Block interiorBlock, Research preRequisite)
	{
		// Initialize all fields
		this.icon = icon;
		this.minMeteorRadius = minMeteorRadius;
		this.maxMeteorRadius = maxMeteorRadius;
		this.richnessPercent = richnessPercent;
		this.interiorBlock = interiorBlock;
		this.preRequisite = preRequisite;

		// Ensure the min/max radii are valid values
		if (this.minMeteorRadius < 2)
			AfraidOfTheDark.INSTANCE.getLogger().error("Meteor entries should not have a min radius less than 2!");
		if (this.minMeteorRadius > this.maxMeteorRadius)
			AfraidOfTheDark.INSTANCE.getLogger().error("Meteor entries max-radius should be larger than the min-radius!");
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

	public double getRichnessPercent()
	{
		return richnessPercent;
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
