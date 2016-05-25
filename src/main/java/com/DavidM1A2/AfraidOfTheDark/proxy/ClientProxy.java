/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import java.util.Map;
import java.util.UUID;

import com.DavidM1A2.AfraidOfTheDark.client.entities.AOTDPainting.RenderArtwork;
import com.DavidM1A2.AfraidOfTheDark.client.entities.DeeeSyft.RenderDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Enaria.RenderEnaria;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Enaria.RenderGhastlyEnaria;
import com.DavidM1A2.AfraidOfTheDark.client.entities.EnchantedSkeleton.RenderEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.client.entities.SplinterDrone.RenderSplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.client.entities.SplinterDrone.RenderSplinterDroneProjectile;
import com.DavidM1A2.AfraidOfTheDark.client.entities.Werewolf.RenderWerewolf;
import com.DavidM1A2.AfraidOfTheDark.client.entities.bolts.IgneousBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.bolts.IronBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.bolts.SilverBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.bolts.StarMetalBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.bolts.WoodenBoltRender;
import com.DavidM1A2.AfraidOfTheDark.client.entities.spell.ModelSpellProjectile;
import com.DavidM1A2.AfraidOfTheDark.client.entities.spell.RenderSpell;
import com.DavidM1A2.AfraidOfTheDark.client.entities.tileEntities.TileEntityVoidChestRenderer;
import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly.EntityGhastlyEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.common.entities.EntityAOTDPainting.EntityArtwork;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDroneProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIgneousBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityStarMetalBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectileDive;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDArt;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
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
	}

	// register renderers as well
	@Override
	public void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityWerewolf.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderWerewolf<EntityWerewolf>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityDeeeSyft.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderDeeeSyft<EntityDeeeSyft>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityEnchantedSkeleton.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderEnchantedSkeleton<EntityEnchantedSkeleton>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDrone.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderSplinterDrone<EntitySplinterDrone>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDroneProjectile.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderSplinterDroneProjectile<EntitySplinterDroneProjectile>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityEnaria.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderEnaria<EntityEnaria>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityGhastlyEnaria.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderGhastlyEnaria<EntityGhastlyEnaria>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityIronBolt.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new IronBoltRender<EntityIronBolt>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntitySilverBolt.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new SilverBoltRender<EntitySilverBolt>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityWoodenBolt.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new WoodenBoltRender<EntityWoodenBolt>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityIgneousBolt.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new IgneousBoltRender<EntityIgneousBolt>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityStarMetalBolt.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new StarMetalBoltRender<EntityStarMetalBolt>(manager);
			}
		});
		for (final DeliveryMethods deliveryMethod : DeliveryMethods.values())
			RenderingRegistry.registerEntityRenderingHandler(deliveryMethod.getDeliveryEntity(), new IRenderFactory()
			{
				@Override
				public Render createRenderFor(RenderManager manager)
				{
					return deliveryMethod.getDeliveryRenderer(manager);
				}
			});

		// Add extra projectile
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectileDive.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderSpell(manager, new ModelSpellProjectile(), "afraidofthedark:textures/entity/spell/projectile.png");
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityArtwork.class, new IRenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderArtwork(manager);
			}
		});
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

	@Override
	public EntityPlayer getSpellOwner(Spell spell)
	{
		// Because minecraft is wierd, server side will return the client side
		// instance of entity player on SSP
		if (!Minecraft.getMinecraft().isSingleplayer())
			return Minecraft.getMinecraft().thePlayer;
		else
		{
			Map<UUID, EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().uuidToPlayerMap;
			if (players.size() == 1)
				return MinecraftServer.getServer().getConfigurationManager().playerEntityList.get(0);

			if (players.containsKey(spell.getSpellOwner()))
				return players.get(spell.getSpellOwner());
			else
				return null;
		}
	}

	@Override
	public void registerItemRenders()
	{
		final ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		if (Reference.isDebug)
		{
			itemModelMesher.register(ModItems.insanityControl, 0, new ModelResourceLocation(Reference.MOD_ID + ":insanityControl", "inventory"));
			itemModelMesher.register(ModItems.worldGenTest, 0, new ModelResourceLocation(Reference.MOD_ID + ":worldGenTest", "inventory"));
		}

		itemModelMesher.register(ModItems.journal, 0, new ModelResourceLocation(Reference.MOD_ID + ":journal", "inventory"));
		itemModelMesher.register(ModItems.journal, 1, new ModelResourceLocation(Reference.MOD_ID + ":journal", "inventory"));
		itemModelMesher.register(ModItems.astralSilverSword, 0, new ModelResourceLocation(Reference.MOD_ID + ":astralSilverSword", "inventory"));
		itemModelMesher.register(ModItems.igneousSword, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneousSword", "inventory"));
		itemModelMesher.register(ModItems.igneousSword, 1, new ModelResourceLocation(Reference.MOD_ID + ":igneousSwordFullCharge", "inventory"));
		ModelBakery.registerItemVariants(ModItems.igneousSword, new ModelResourceLocation(Reference.MOD_ID + ":igneousSword", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":igneousSwordFullCharge", "inventory"));
		itemModelMesher.register(ModItems.astralSilverIngot, 0, new ModelResourceLocation(Reference.MOD_ID + ":astralSilverIngot", "inventory"));
		itemModelMesher.register(ModItems.crossbow, 0, new ModelResourceLocation(Reference.MOD_ID + ":crossbowUnloaded", "inventory"));
		itemModelMesher.register(ModItems.crossbow, 1, new ModelResourceLocation(Reference.MOD_ID + ":crossbowQuarter", "inventory"));
		itemModelMesher.register(ModItems.crossbow, 2, new ModelResourceLocation(Reference.MOD_ID + ":crossbowHalf", "inventory"));
		itemModelMesher.register(ModItems.crossbow, 3, new ModelResourceLocation(Reference.MOD_ID + ":crossbowLoaded", "inventory"));
		ModelBakery.registerItemVariants(ModItems.crossbow, new ModelResourceLocation(Reference.MOD_ID + ":crossbowUnloaded", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":crossbowQuarter", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":crossbowHalf", "inventory"),
				new ModelResourceLocation(Reference.MOD_ID + ":crossbowLoaded", "inventory"));

		itemModelMesher.register(ModItems.wristCrossbow, 0, new ModelResourceLocation(Reference.MOD_ID + ":wristCrossbow", "inventory"));
		itemModelMesher.register(ModItems.woodenBolt, 0, new ModelResourceLocation(Reference.MOD_ID + ":woodenBolt", "inventory"));
		itemModelMesher.register(ModItems.ironBolt, 0, new ModelResourceLocation(Reference.MOD_ID + ":ironBolt", "inventory"));
		itemModelMesher.register(ModItems.silverBolt, 0, new ModelResourceLocation(Reference.MOD_ID + ":silverBolt", "inventory"));
		itemModelMesher.register(ModItems.igneousBolt, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneousBolt", "inventory"));
		itemModelMesher.register(ModItems.starMetalBolt, 0, new ModelResourceLocation(Reference.MOD_ID + ":starMetalBolt", "inventory"));
		itemModelMesher.register(ModItems.igneousHelmet, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneousHelmet", "inventory"));
		itemModelMesher.register(ModItems.igneousChestplate, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneousChestplate", "inventory"));
		itemModelMesher.register(ModItems.igneousLeggings, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneousLeggings", "inventory"));
		itemModelMesher.register(ModItems.igneousBoots, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneousBoots", "inventory"));
		itemModelMesher.register(ModItems.igneousGem, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneousGem", "inventory"));
		itemModelMesher.register(ModItems.starMetalHelmet, 0, new ModelResourceLocation(Reference.MOD_ID + ":starMetalHelmet", "inventory"));
		itemModelMesher.register(ModItems.starMetalChestplate, 0, new ModelResourceLocation(Reference.MOD_ID + ":starMetalChestplate", "inventory"));
		itemModelMesher.register(ModItems.starMetalLeggings, 0, new ModelResourceLocation(Reference.MOD_ID + ":starMetalLeggings", "inventory"));
		itemModelMesher.register(ModItems.starMetalBoots, 0, new ModelResourceLocation(Reference.MOD_ID + ":starMetalBoots", "inventory"));
		itemModelMesher.register(ModItems.telescope, 0, new ModelResourceLocation(Reference.MOD_ID + ":telescope", "inventory"));
		itemModelMesher.register(ModItems.sextant, 0, new ModelResourceLocation(Reference.MOD_ID + ":sextant", "inventory"));
		itemModelMesher.register(ModItems.sunstoneFragment, 0, new ModelResourceLocation(Reference.MOD_ID + ":sunstoneFragment", "inventory"));
		itemModelMesher.register(ModItems.starMetalFragment, 0, new ModelResourceLocation(Reference.MOD_ID + ":starMetalFragment", "inventory"));
		itemModelMesher.register(ModItems.starMetalStaff, 0, new ModelResourceLocation(Reference.MOD_ID + ":starMetalStaff", "inventory"));
		itemModelMesher.register(ModItems.vitaeLantern, 0, new ModelResourceLocation(Reference.MOD_ID + ":vitaeLanternEmpty", "inventory"));
		itemModelMesher.register(ModItems.vitaeLantern, 1, new ModelResourceLocation(Reference.MOD_ID + ":vitaeLanternLow", "inventory"));
		itemModelMesher.register(ModItems.vitaeLantern, 2, new ModelResourceLocation(Reference.MOD_ID + ":vitaeLanternHalf", "inventory"));
		itemModelMesher.register(ModItems.vitaeLantern, 3, new ModelResourceLocation(Reference.MOD_ID + ":vitaeLanternThreeQuarters", "inventory"));
		itemModelMesher.register(ModItems.vitaeLantern, 4, new ModelResourceLocation(Reference.MOD_ID + ":vitaeLanternFull", "inventory"));
		ModelBakery.registerItemVariants(ModItems.vitaeLantern, new ModelResourceLocation(Reference.MOD_ID + ":vitaeLanternEmpty", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":vitaeLanternLow", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":vitaeLanternHalf",
				"inventory"), new ModelResourceLocation(Reference.MOD_ID + ":vitaeLanternThreeQuarters", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":vitaeLanternFull", "inventory"));
		itemModelMesher.register(ModItems.starMetalKhopesh, 0, new ModelResourceLocation(Reference.MOD_ID + ":starMetalKhopesh", "inventory"));
		itemModelMesher.register(ModItems.starMetalKhopesh, 1, new ModelResourceLocation(Reference.MOD_ID + ":starMetalKhopeshFullCharge", "inventory"));
		ModelBakery.registerItemVariants(ModItems.starMetalKhopesh, new ModelResourceLocation(Reference.MOD_ID + ":starMetalKhopesh", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":starMetalKhopeshFullCharge", "inventory"));
		itemModelMesher.register(ModItems.cloakOfAgility, 0, new ModelResourceLocation(Reference.MOD_ID + ":cloakOfAgility", "inventory"));
		itemModelMesher.register(ModItems.researchScrollCloakOfAgility, 0, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollCloakOfAgility", "inventory"));
		itemModelMesher.register(ModItems.researchScrollAstronomy2, 0, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollAstronomy2", "inventory"));
		itemModelMesher.register(ModItems.researchScrollAstronomy2, 1, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollAstronomy2", "inventory"));
		itemModelMesher.register(ModItems.researchScrollAstronomy2, 2, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollAstronomy2", "inventory"));
		itemModelMesher.register(ModItems.researchScrollAstronomy2, 3, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollAstronomy2", "inventory"));
		itemModelMesher.register(ModItems.researchScrollAstronomy2, 4, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollAstronomy2", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 0, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollVitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 1, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollVitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 2, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollVitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 3, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollVitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 4, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollVitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 5, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollVitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollWristCrossbow, 0, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollWristCrossbow", "inventory"));
		itemModelMesher.register(ModItems.starMetalPlate, 0, new ModelResourceLocation(Reference.MOD_ID + ":starMetalPlate", "inventory"));
		itemModelMesher.register(ModItems.enchantedSkeletonBone, 0, new ModelResourceLocation(Reference.MOD_ID + ":enchantedSkeletonBone", "inventory"));
		itemModelMesher.register(ModItems.sleepingPotion, 0, new ModelResourceLocation(Reference.MOD_ID + ":sleepingPotion", "inventory"));
		itemModelMesher.register(ModItems.researchScrollInsanity, 0, new ModelResourceLocation(Reference.MOD_ID + ":researchScrollInsanity", "inventory"));
		itemModelMesher.register(ModItems.eldritchMetalIngot, 0, new ModelResourceLocation(Reference.MOD_ID + ":eldritchMetalIngot", "inventory"));
		itemModelMesher.register(ModItems.starMetalIngot, 0, new ModelResourceLocation(Reference.MOD_ID + ":starMetalIngot", "inventory"));
		itemModelMesher.register(ModItems.bladeOfExhumation, 0, new ModelResourceLocation(Reference.MOD_ID + ":bladeOfExhumation", "inventory"));
		itemModelMesher.register(ModItems.flaskOfSouls, 0, new ModelResourceLocation(Reference.MOD_ID + ":flaskOfSouls", "inventory"));
		itemModelMesher.register(ModItems.flaskOfSouls, 1, new ModelResourceLocation(Reference.MOD_ID + ":flaskOfSoulsCharged", "inventory"));
		ModelBakery.registerItemVariants(ModItems.flaskOfSouls, new ModelResourceLocation(Reference.MOD_ID + ":flaskOfSouls", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":flaskOfSoulsCharged", "inventory"));
		itemModelMesher.register(ModItems.werewolfBlood, 0, new ModelResourceLocation(Reference.MOD_ID + ":werewolfBlood", "inventory"));
		itemModelMesher.register(ModItems.gnomishMetalIngot, 0, new ModelResourceLocation(Reference.MOD_ID + ":gnomishMetalIngot", "inventory"));
		for (int i = 0; i < AOTDArt.values().length; i++)
			itemModelMesher.register(ModItems.artwork, i, new ModelResourceLocation(Reference.MOD_ID + ":artwork", "inventory"));
	}

	@Override
	public void registerBlockRenders()
	{
		OBJLoader.instance.addDomain(Reference.MOD_ID);

		final ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.spring), 0, new ModelResourceLocation(Reference.MOD_ID + ":spring", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.darkForest), 0, new ModelResourceLocation(Reference.MOD_ID + ":darkForest", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewood), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewood", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodLeaves), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewoodLeaves", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodPlanks), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewoodPlanks", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodStairs), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewoodStairs", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewoodHalfSlab", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodDoubleSlab), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewoodDoubleSlab", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.mangrove), 0, new ModelResourceLocation(Reference.MOD_ID + ":mangrove", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.sunstoneOre), 0, new ModelResourceLocation(Reference.MOD_ID + ":sunstoneOre", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.astralSilverOre), 0, new ModelResourceLocation(Reference.MOD_ID + ":astralSilverOre", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.meteor), 0, new ModelResourceLocation(Reference.MOD_ID + ":meteor", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.starMetalOre), 0, new ModelResourceLocation(Reference.MOD_ID + ":starMetalOre", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.igneousBlock), 0, new ModelResourceLocation(Reference.MOD_ID + ":igneousBlock", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodSapling), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewoodSapling", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.vitaeDisenchanter), 0, new ModelResourceLocation(Reference.MOD_ID + ":vitaeDisenchanter", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.voidChest), 0, new ModelResourceLocation(Reference.MOD_ID + ":voidChest", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.voidChestPortal), 0, new ModelResourceLocation(Reference.MOD_ID + ":voidChestPortal", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.eldritchObsidian), 0, new ModelResourceLocation(Reference.MOD_ID + ":eldritchObsidian", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.amorphousEldritchMetal), 0, new ModelResourceLocation(Reference.MOD_ID + ":amorphousEldritchMetal", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.eldritchStone), 0, new ModelResourceLocation(Reference.MOD_ID + ":eldritchStone", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.aOTDBarrier), 0, new ModelResourceLocation(Reference.MOD_ID + ":aOTDBarrier", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gnomishMetalPlate), 0, new ModelResourceLocation(Reference.MOD_ID + ":gnomishMetalPlate", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gnomishMetalStrut), 0, new ModelResourceLocation(Reference.MOD_ID + ":gnomishMetalStrut", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.glowStalk), 0, new ModelResourceLocation(Reference.MOD_ID + ":glowStalk", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.enariaSpawner), 0, new ModelResourceLocation(Reference.MOD_ID + ":enariaSpawner", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.enariasAltar), 0, new ModelResourceLocation(Reference.MOD_ID + ":enariasAltar", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.enariasAltar), 0, new ModelResourceLocation(Reference.MOD_ID + ":enariasAltar", "inventory"));
	}
}
