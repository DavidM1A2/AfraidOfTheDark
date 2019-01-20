package com.DavidM1A2.afraidofthedark.common.utility;

import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicLoader;
import net.minecraft.util.ResourceLocation;

import java.io.InputStream;

/**
 * Class that helps load resources from server side as well as client side
 */
public class ResourceUtil
{
	/**
	 * Returns a resource location as an input stream, this is much better than Minecraft.getMinecraft().getResourceManager() because that is only client side
	 *
	 * @param resourceLocation The resource location to read
	 * @return The input stream to read from
	 */
	public static InputStream getInputStream(ResourceLocation resourceLocation)
	{
		return ResourceUtil.class.getResourceAsStream("/assets/" + resourceLocation.getResourceDomain() + "/" + resourceLocation.getResourcePath());
	}
}
