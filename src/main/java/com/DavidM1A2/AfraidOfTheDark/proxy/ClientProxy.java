/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import com.DavidM1A2.AfraidOfTheDark.client.entities.DeeeSyft.RenderDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Enaria.RenderEnaria;
import com.DavidM1A2.AfraidOfTheDark.client.entities.EnchantedSkeleton.RenderEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.client.entities.SplinterDrone.RenderSplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.client.entities.SplinterDrone.RenderSplinterDroneProjectile;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Werewolf.RenderWerewolf;
import com.DavidM1A2.AfraidOfTheDark.client.entities.bolts.IgneousBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.bolts.IronBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.bolts.SilverBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.bolts.StarMetalBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.bolts.WoodenBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.tileEntities.TileEntityVoidChestRenderer;
import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDroneProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIgneousBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityStarMetalBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethods;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
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
	public void registerEntityRenders()
	{
		// RenderingRegistry.registerEntityRenderingHandler(EntityWerewolf.class,
		// new RenderWerewolf());
		RenderingRegistry.registerEntityRenderingHandler(EntityWerewolf.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderWerewolf<EntityWerewolf>(manager);
			}
		});
		// RenderingRegistry.registerEntityRenderingHandler(EntityDeeeSyft.class,
		// new RenderDeeeSyft());
		RenderingRegistry.registerEntityRenderingHandler(EntityDeeeSyft.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderDeeeSyft<EntityDeeeSyft>(manager);
			}
		});
		// RenderingRegistry.registerEntityRenderingHandler(EntityEnchantedSkeleton.class,
		// new RenderEnchantedSkeleton());
		RenderingRegistry.registerEntityRenderingHandler(EntityEnchantedSkeleton.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderEnchantedSkeleton<EntityEnchantedSkeleton>(manager);
			}
		});
		// RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDrone.class,
		// new RenderSplinterDrone());
		RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDrone.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderSplinterDrone<EntitySplinterDrone>(manager);
			}
		});
		// RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDroneProjectile.class,
		// new RenderSplinterDroneProjectile());
		RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDroneProjectile.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderSplinterDroneProjectile<EntitySplinterDroneProjectile>(manager);
			}
		});
		// RenderingRegistry.registerEntityRenderingHandler(EntityEnaria.class,
		// new RenderEnaria());
		RenderingRegistry.registerEntityRenderingHandler(EntityEnaria.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderEnaria<EntityEnaria>(manager);
			}
		});
		// RenderingRegistry.registerEntityRenderingHandler(EntityIronBolt.class,
		// new IronBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntityIronBolt.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new IronBoltRender<EntityIronBolt>(manager);
			}
		});
		// RenderingRegistry.registerEntityRenderingHandler(EntitySilverBolt.class,
		// new SilverBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntitySilverBolt.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new SilverBoltRender<EntitySilverBolt>(manager);
			}
		});
		// RenderingRegistry.registerEntityRenderingHandler(EntityWoodenBolt.class,
		// new WoodenBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntityWoodenBolt.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new WoodenBoltRender<EntityWoodenBolt>(manager);
			}
		});
		// RenderingRegistry.registerEntityRenderingHandler(EntityIgneousBolt.class,
		// new IgneousBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntityIgneousBolt.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new IgneousBoltRender<EntityIgneousBolt>(manager);
			}
		});
		// RenderingRegistry.registerEntityRenderingHandler(EntityStarMetalBolt.class,
		// new StarMetalBoltRender(current));
		RenderingRegistry.registerEntityRenderingHandler(EntityStarMetalBolt.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new StarMetalBoltRender<EntityStarMetalBolt>(manager);
			}
		});
		// RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class,
		// new RenderSpell(new ModelSpellProjectile(),
		// "afraidofthedark:textures/entity/spell/projectile.png"));
		for (final DeliveryMethods deliveryMethod : DeliveryMethods.values())
			RenderingRegistry.registerEntityRenderingHandler(deliveryMethod.getDeliveryEntity(), new IRenderFactory()
			{
				@Override
				public Render createRenderFor(RenderManager manager)
				{
					return deliveryMethod.getDeliveryRenderer(manager);
				}
			});
		//		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new IRenderFactory() {
		//			@Override
		//			public Render createRenderFor(RenderManager manager) {
		//				return new RenderSpell<EntitySpellProjectile>(manager, new ModelSpellProjectile(),
		//						"afraidofthedark:textures/entity/spell/projectile.png");
		//			}
		//		});
	}

	@Override
	public void registerMiscRenders()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVoidChest.class, new TileEntityVoidChestRenderer());
		// MinecraftForgeClient.registerItemRenderer(ModItems.crossbow, new
		// ItemCrossbowRender());
	}

	@Override
	public void registerMiscelaneous()
	{
		Constants.entityVitaeResistance.put(EntityPlayerSP.class, 100);
		Constants.entityVitaeResistance.put(EntityOtherPlayerMP.class, 100);
	}
}
