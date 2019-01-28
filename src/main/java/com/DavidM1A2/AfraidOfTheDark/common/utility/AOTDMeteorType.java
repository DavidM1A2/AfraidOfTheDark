package com.DavidM1A2.afraidofthedark.common.utility;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.util.ResourceLocation;

/**
 * Utility class that contains a list of meteor type definitions
 */
public enum AOTDMeteorType
{
	ASTRAL_SILVER("Astral Silver", new ResourceLocation(Constants.MOD_ID, "textures/gui/astral_silver_meteor.png")),
	STAR_METAL("Star Metal", new ResourceLocation(Constants.MOD_ID, "textures/gui/star_metal_meteor.png")),
	IGNEOUS("Igneous", new ResourceLocation(Constants.MOD_ID, "textures/gui/igneous_meteor.png"));

	// The name of the meteor type
	private final String name;
	// The resource location containing the icon that this meteor type will use
	private final ResourceLocation icon;

	/**
	 * Constructor just initializes fields
	 *
	 * @param name The name of the meteor type
	 * @param icon The resource location containing the icon that this meteor type will use
	 */
	AOTDMeteorType(String name, ResourceLocation icon)
	{
		this.name = name;
		this.icon = icon;
	}

	/**
	 * @return The name of the meteor type
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @return The resource location containing the icon that this meteor type will use
	 */
	public ResourceLocation getIcon()
	{
		return this.icon;
	}
}
