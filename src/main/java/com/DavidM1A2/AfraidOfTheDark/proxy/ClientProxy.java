/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.proxy;

import com.DavidM1A2.afraidofthedark.common.reference.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

/**
 * Proxy that is only to be instantiated on the CLIENT side
 */
public class ClientProxy extends CommonProxy
{
	/**
	 * Called to register any item renderers, only happens client side
	 */
	@Override
	public void registerItemRenders()
	{
		ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();


	}

	/**
	 * Called to register any block renderers, only happens client side
	 */
	@Override
	public void registerBlockRenders()
	{
		ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.GRAVEWOOD_SAPLING), 0, new ModelResourceLocation(ModBlocks.GRAVEWOOD_SAPLING.getRegistryName(), "inventory"));
	}
}
