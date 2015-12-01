/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts.IgneousBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts.IronBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts.SilverBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts.StarMetalBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Bolts.WoodenBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.DeeeSyft.RenderDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Enaria.RenderEnaria;
import com.DavidM1A2.AfraidOfTheDark.client.entities.EnchantedSkeleton.RenderEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.client.entities.SplinterDrone.RenderSplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.client.entities.SplinterDrone.RenderSplinterDroneProjectile;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Werewolf.RenderWerewolf;
import com.DavidM1A2.AfraidOfTheDark.client.entities.tileEntities.TileEntityVoidChestRenderer;
import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityIgneousBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityStarMetalBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDroneProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.item.crossbow.ItemCrossbowRender;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

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

	// register renderers as well
	@Override
	public void registerRenderThings()
	{
		final RenderManager current = Minecraft.getMinecraft().getRenderManager();

		RenderingRegistry.registerEntityRenderingHandler(EntityWerewolf.class, new RenderWerewolf());
		RenderingRegistry.registerEntityRenderingHandler(EntityDeeeSyft.class, new RenderDeeeSyft());
		RenderingRegistry.registerEntityRenderingHandler(EntityEnchantedSkeleton.class, new RenderEnchantedSkeleton());
		RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDrone.class, new RenderSplinterDrone());
		RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDroneProjectile.class, new RenderSplinterDroneProjectile());
		RenderingRegistry.registerEntityRenderingHandler(EntityEnaria.class, new RenderEnaria());
		RenderingRegistry.registerEntityRenderingHandler(EntityIronBolt.class, new IronBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntitySilverBolt.class, new SilverBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntityWoodenBolt.class, new WoodenBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntityIgneousBolt.class, new IgneousBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntityStarMetalBolt.class, new StarMetalBoltRender(current));
		MinecraftForgeClient.registerItemRenderer(ModItems.crossbow, new ItemCrossbowRender());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVoidChest.class, new TileEntityVoidChestRenderer());
	}

	@Override
	public void registerMiscelaneous()
	{
		Constants.entityVitaeResistance.put(EntityPlayerSP.class, 100);
		Constants.entityVitaeResistance.put(EntityOtherPlayerMP.class, 100);
	}
}
