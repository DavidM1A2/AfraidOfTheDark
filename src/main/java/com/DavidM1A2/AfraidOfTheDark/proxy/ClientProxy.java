/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
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
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateInsanity;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateVitae;
import com.DavidM1A2.AfraidOfTheDark.playerData.Vitae;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

// Just client things go here
public class ClientProxy extends CommonProxy
{
	// register key bindings go here
	@Override
	public void registerKeyBindings()
	{
		// ClientRegistry.registerKeyBinding(Keybindings.changeMode);
	}

	// Here we register packets and a channel
	@Override
	public void registerChannel()
	{
		super.registerChannel();
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateAOTDStatus.HandlerClient.class, UpdateAOTDStatus.class, Refrence.PACKET_ID_HAS_STARTED_AOTD_UPDATE, Side.CLIENT);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateResearch.HandlerClient.class, UpdateResearch.class, Refrence.PACKET_ID_RESEARCH_UPDATE, Side.CLIENT);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateInsanity.Handler.class, UpdateInsanity.class, Refrence.PACKET_ID_INSANITY_UPDATE, Side.CLIENT);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateVitae.HandlerClient.class, UpdateVitae.class, Refrence.PACKET_ID_VITAE_UPDATE, Side.CLIENT);
	}

	// register renderers as well
	@Override
	public void registerRenderThings()
	{
		final RenderManager current = Minecraft.getMinecraft().getRenderManager();

		RenderingRegistry.registerEntityRenderingHandler(EntityWereWolf.class, new WereWolfRender(current, new ModelWereWolf(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityIronBolt.class, new IronBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntitySilverBolt.class, new SilverBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntityWoodenBolt.class, new WoodenBoltRender(current));
		MinecraftForgeClient.registerItemRenderer(ModItems.crossbow, (IItemRenderer) new ItemCrossbowRender());
	}
}
