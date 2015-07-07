/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts.IgneousBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts.IronBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts.SilverBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts.StarMetalBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts.WoodenBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.DeeeSyft.RenderDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Werewolf.RenderWerewolf;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityIgneousBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityStarMetalBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.item.crossbow.ItemCrossbowRender;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateLanternState;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateVitae;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.CustomFont;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

// Just client things go here
public class ClientProxy extends CommonProxy
{
	// register key bindings go here
	@Override
	public void registerKeyBindings()
	{
		ClientRegistry.registerKeyBinding(Keybindings.rollWithCloakOfAgility);
		ClientRegistry.registerKeyBinding(Keybindings.fireWristCrossbow);
		ClientRegistry.registerKeyBinding(Keybindings.changeLanternMode);
	}

	// Here we register packets and a channel
	@Override
	public void registerChannel()
	{
		super.registerChannel();
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateAOTDStatus.HandlerClient.class, UpdateAOTDStatus.class, Constants.Packets.PACKET_ID_HAS_STARTED_AOTD_UPDATE, Side.CLIENT);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateResearch.HandlerClient.class, UpdateResearch.class, Constants.Packets.PACKET_ID_RESEARCH_UPDATE, Side.CLIENT);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateInsanity.Handler.class, UpdateInsanity.class, Constants.Packets.PACKET_ID_INSANITY_UPDATE, Side.CLIENT);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateVitae.HandlerClient.class, UpdateVitae.class, Constants.Packets.PACKET_ID_VITAE_UPDATE, Side.CLIENT);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateLanternState.HandlerClient.class, UpdateLanternState.class, Constants.Packets.PACKET_ID_UPDATE_LANTERN_STATE, Side.CLIENT);
	}

	// register renderers as well
	@Override
	public void registerRenderThings()
	{
		final RenderManager current = Minecraft.getMinecraft().getRenderManager();

		RenderingRegistry.registerEntityRenderingHandler(EntityWerewolf.class, new RenderWerewolf());
		RenderingRegistry.registerEntityRenderingHandler(EntityDeeeSyft.class, new RenderDeeeSyft());
		RenderingRegistry.registerEntityRenderingHandler(EntityIronBolt.class, new IronBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntitySilverBolt.class, new SilverBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntityWoodenBolt.class, new WoodenBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntityIgneousBolt.class, new IgneousBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntityStarMetalBolt.class, new StarMetalBoltRender(current));
		MinecraftForgeClient.registerItemRenderer(ModItems.crossbow, (IItemRenderer) new ItemCrossbowRender());
	}

	@Override
	public void registerMiscelaneous()
	{
		try
		{
			final InputStream textFont = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Refrence.MOD_ID, "fonts/Targa MS Hand.ttf")).getInputStream();
			final InputStream titleFont = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Refrence.MOD_ID, "fonts/coolvetica.ttf")).getInputStream();

			ClientData.journalFont = new CustomFont(textFont, 16);
			ClientData.journalTitleFont = new CustomFont(titleFont, 26);

			// Set the journal font sizes
			ClientData.journalFont.setFontSize(20, 32, 126, false);
			ClientData.journalTitleFont.setFontSize(32, 32, 126, false);
		}
		catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}

		Constants.entityVitaeResistance.put(EntityPlayerSP.class, 100);
		Constants.entityVitaeResistance.put(EntityOtherPlayerMP.class, 100);
	}
}
