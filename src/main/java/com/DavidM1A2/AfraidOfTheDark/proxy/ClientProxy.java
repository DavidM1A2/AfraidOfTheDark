package com.DavidM1A2.AfraidOfTheDark.proxy;

import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.IronBoltRender;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.SilverBoltRender;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.WoodenBoltRender;
import com.DavidM1A2.AfraidOfTheDark.entities.WereWolf.EntityWereWolf;
import com.DavidM1A2.AfraidOfTheDark.entities.WereWolf.ModelWereWolf;
import com.DavidM1A2.AfraidOfTheDark.entities.WereWolf.WereWolfRender;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.item.crossbow.ItemCrossbowRender;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{

	@Override
	public void registerKeyBindings()
	{
		// ClientRegistry.registerKeyBinding(Keybindings.changeMode);
	}

	@Override
	public void registerRenderThings()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityWereWolf.class, new WereWolfRender(new ModelWereWolf(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityIronBolt.class, new IronBoltRender());
		RenderingRegistry.registerEntityRenderingHandler(EntitySilverBolt.class, new SilverBoltRender());
		RenderingRegistry.registerEntityRenderingHandler(EntityWoodenBolt.class, new WoodenBoltRender());
		MinecraftForgeClient.registerItemRenderer(ModItems.crossbow, (IItemRenderer) new ItemCrossbowRender());
	}
}
