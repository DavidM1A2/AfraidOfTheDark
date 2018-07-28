/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import com.DavidM1A2.AfraidOfTheDark.client.entities.Artwork.RenderArtwork;
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
import com.DavidM1A2.AfraidOfTheDark.common.entities.EntityArtwork.EntityArtwork;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDroneProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIgneousBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityStarMetalBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectileDive;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDArt;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethods;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
		RenderingRegistry.registerEntityRenderingHandler(EntityWerewolf.class, new IRenderFactory<EntityWerewolf>()
		{
			@Override
			public Render<EntityWerewolf> createRenderFor(RenderManager manager)
			{
				return new RenderWerewolf<EntityWerewolf>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityDeeeSyft.class, new IRenderFactory<EntityDeeeSyft>()
		{
			@Override
			public Render<EntityDeeeSyft> createRenderFor(RenderManager manager)
			{
				return new RenderDeeeSyft<EntityDeeeSyft>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityEnchantedSkeleton.class, new IRenderFactory<EntityEnchantedSkeleton>()
		{
			@Override
			public Render<EntityEnchantedSkeleton> createRenderFor(RenderManager manager)
			{
				return new RenderEnchantedSkeleton<EntityEnchantedSkeleton>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDrone.class, new IRenderFactory<EntitySplinterDrone>()
		{
			@Override
			public Render<EntitySplinterDrone> createRenderFor(RenderManager manager)
			{
				return new RenderSplinterDrone<EntitySplinterDrone>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDroneProjectile.class, new IRenderFactory<EntitySplinterDroneProjectile>()
		{
			@Override
			public Render<EntitySplinterDroneProjectile> createRenderFor(RenderManager manager)
			{
				return new RenderSplinterDroneProjectile<EntitySplinterDroneProjectile>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityEnaria.class, new IRenderFactory<EntityEnaria>()
		{
			@Override
			public Render<EntityEnaria> createRenderFor(RenderManager manager)
			{
				return new RenderEnaria<EntityEnaria>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityGhastlyEnaria.class, new IRenderFactory<EntityGhastlyEnaria>()
		{
			@Override
			public Render<EntityGhastlyEnaria> createRenderFor(RenderManager manager)
			{
				return new RenderGhastlyEnaria<EntityGhastlyEnaria>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityIronBolt.class, new IRenderFactory<EntityIronBolt>()
		{
			@Override
			public Render<EntityIronBolt> createRenderFor(RenderManager manager)
			{
				return new IronBoltRender<EntityIronBolt>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntitySilverBolt.class, new IRenderFactory<EntitySilverBolt>()
		{
			@Override
			public Render<EntitySilverBolt> createRenderFor(RenderManager manager)
			{
				return new SilverBoltRender<EntitySilverBolt>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityWoodenBolt.class, new IRenderFactory<EntityWoodenBolt>()
		{
			@Override
			public Render<EntityWoodenBolt> createRenderFor(RenderManager manager)
			{
				return new WoodenBoltRender<EntityWoodenBolt>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityIgneousBolt.class, new IRenderFactory<EntityIgneousBolt>()
		{
			@Override
			public Render<EntityIgneousBolt> createRenderFor(RenderManager manager)
			{
				return new IgneousBoltRender<EntityIgneousBolt>(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityStarMetalBolt.class, new IRenderFactory<EntityStarMetalBolt>()
		{
			@Override
			public Render<EntityStarMetalBolt> createRenderFor(RenderManager manager)
			{
				return new StarMetalBoltRender<EntityStarMetalBolt>(manager);
			}
		});
		for (final DeliveryMethods deliveryMethod : DeliveryMethods.values())
			if (deliveryMethod.getDeliveryEntity() != null)
				RenderingRegistry.registerEntityRenderingHandler(deliveryMethod.getDeliveryEntity(), new IRenderFactory()
				{
					@Override
					public Render createRenderFor(RenderManager manager)
					{
						return deliveryMethod.getDeliveryRenderer(manager);
					}
				});

		// Add extra projectile
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectileDive.class, new IRenderFactory<EntitySpellProjectileDive>()
		{
			@Override
			public Render<EntitySpellProjectileDive> createRenderFor(RenderManager manager)
			{
				return new RenderSpell<EntitySpellProjectileDive> (manager, new ModelSpellProjectile(), "afraidofthedark:textures/entity/spell/projectile.png");
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityArtwork.class, new IRenderFactory<EntityArtwork>()
		{
			@Override
			public Render<EntityArtwork> createRenderFor(RenderManager manager)
			{
				return new RenderArtwork(manager);
			}
		});
	}

	@Override
	public void registerMiscRenders()
	{
		ClientRegistry.bindTileEntitySpecialRenderer (TileEntityVoidChest.class, new TileEntityVoidChestRenderer());
	}

	@Override
	public void registerMiscellaneous()
	{
		Constants.entityVitaeResistance.put(EntityPlayerSP.class, 100);
		Constants.entityVitaeResistance.put(EntityOtherPlayerMP.class, 100);
	}

	@Override
	public EntityPlayer getSpellOwner(Spell spell)
	{
		// Because minecraft is wierd, server side will return the client side
		// INSTANCE of entity player on SSP
		if (!Minecraft.getMinecraft().isSingleplayer())
			return Minecraft.getMinecraft().player;
		else
		{
			PlayerList list = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
			if (list.getPlayers().size() == 1)
				return list.getPlayers().get(0);
			else
				return list.getPlayerByUUID(spell.getSpellOwner());
		}
	}

	@Override
	public void registerItemRenders()
	{
		final ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		if (Reference.isDebug)
		{
			itemModelMesher.register(ModItems.insanityControl, 0, new ModelResourceLocation(Reference.MOD_ID + ":insanity_control", "inventory"));
			itemModelMesher.register(ModItems.worldGenTest, 0, new ModelResourceLocation(Reference.MOD_ID + ":world_gen_test", "inventory"));
		}

		itemModelMesher.register(ModItems.journal, 0, new ModelResourceLocation(Reference.MOD_ID + ":journal", "inventory"));
		itemModelMesher.register(ModItems.journal, 1, new ModelResourceLocation(Reference.MOD_ID + ":journal", "inventory"));
		itemModelMesher.register(ModItems.astralSilverSword, 0, new ModelResourceLocation(Reference.MOD_ID + ":astral_silver_sword", "inventory"));
		itemModelMesher.register(ModItems.igneousSword, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneous_sword", "inventory"));
		itemModelMesher.register(ModItems.igneousSword, 1, new ModelResourceLocation(Reference.MOD_ID + ":igneous_sword_full_charge", "inventory"));
		ModelBakery.registerItemVariants(ModItems.igneousSword, new ModelResourceLocation(Reference.MOD_ID + ":igneous_sword", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":igneous_sword_full_charge", "inventory"));
		itemModelMesher.register(ModItems.astralSilverIngot, 0, new ModelResourceLocation(Reference.MOD_ID + ":astral_silver_ingot", "inventory"));
		itemModelMesher.register(ModItems.crossbow, 0, new ModelResourceLocation(Reference.MOD_ID + ":crossbow_unloaded", "inventory"));
		itemModelMesher.register(ModItems.crossbow, 1, new ModelResourceLocation(Reference.MOD_ID + ":crossbow_quarter", "inventory"));
		itemModelMesher.register(ModItems.crossbow, 2, new ModelResourceLocation(Reference.MOD_ID + ":crossbow_half", "inventory"));
		itemModelMesher.register(ModItems.crossbow, 3, new ModelResourceLocation(Reference.MOD_ID + ":crossbow_loaded", "inventory"));
		ModelBakery.registerItemVariants(ModItems.crossbow, new ModelResourceLocation(Reference.MOD_ID + ":crossbow_unloaded", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":crossbow_quarter", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":crossbow_half", "inventory"),
				new ModelResourceLocation(Reference.MOD_ID + ":crossbow_loaded", "inventory"));

		itemModelMesher.register(ModItems.wristCrossbow, 0, new ModelResourceLocation(Reference.MOD_ID + ":wrist_crossbow", "inventory"));
		itemModelMesher.register(ModItems.woodenBolt, 0, new ModelResourceLocation(Reference.MOD_ID + ":wooden_bolt", "inventory"));
		itemModelMesher.register(ModItems.ironBolt, 0, new ModelResourceLocation(Reference.MOD_ID + ":iron_bolt", "inventory"));
		itemModelMesher.register(ModItems.silverBolt, 0, new ModelResourceLocation(Reference.MOD_ID + ":silver_bolt", "inventory"));
		itemModelMesher.register(ModItems.igneousBolt, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneous_bolt", "inventory"));
		itemModelMesher.register(ModItems.starMetalBolt, 0, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_bolt", "inventory"));
		itemModelMesher.register(ModItems.igneousHelmet, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneous_helmet", "inventory"));
		itemModelMesher.register(ModItems.igneousChestplate, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneous_chestplate", "inventory"));
		itemModelMesher.register(ModItems.igneousLeggings, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneous_leggings", "inventory"));
		itemModelMesher.register(ModItems.igneousBoots, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneous_boots", "inventory"));
		itemModelMesher.register(ModItems.igneousGem, 0, new ModelResourceLocation(Reference.MOD_ID + ":igneous_gem", "inventory"));
		itemModelMesher.register(ModItems.starMetalHelmet, 0, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_helmet", "inventory"));
		itemModelMesher.register(ModItems.starMetalChestplate, 0, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_chestplate", "inventory"));
		itemModelMesher.register(ModItems.starMetalLeggings, 0, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_leggings", "inventory"));
		itemModelMesher.register(ModItems.starMetalBoots, 0, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_boots", "inventory"));
		itemModelMesher.register(ModItems.telescope, 0, new ModelResourceLocation(Reference.MOD_ID + ":telescope", "inventory"));
		itemModelMesher.register(ModItems.sextant, 0, new ModelResourceLocation(Reference.MOD_ID + ":sextant", "inventory"));
		itemModelMesher.register(ModItems.sunstoneFragment, 0, new ModelResourceLocation(Reference.MOD_ID + ":sunstone_fragment", "inventory"));
		itemModelMesher.register(ModItems.starMetalFragment, 0, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_fragment", "inventory"));
		itemModelMesher.register(ModItems.starMetalStaff, 0, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_staff", "inventory"));
		itemModelMesher.register(ModItems.vitaeLantern, 0, new ModelResourceLocation(Reference.MOD_ID + ":vitae_lantern_empty", "inventory"));
		itemModelMesher.register(ModItems.vitaeLantern, 1, new ModelResourceLocation(Reference.MOD_ID + ":vitae_lantern_low", "inventory"));
		itemModelMesher.register(ModItems.vitaeLantern, 2, new ModelResourceLocation(Reference.MOD_ID + ":vitae_lantern_half", "inventory"));
		itemModelMesher.register(ModItems.vitaeLantern, 3, new ModelResourceLocation(Reference.MOD_ID + ":vitae_lantern_three_quarters", "inventory"));
		itemModelMesher.register(ModItems.vitaeLantern, 4, new ModelResourceLocation(Reference.MOD_ID + ":vitae_lantern_full", "inventory"));
		ModelBakery.registerItemVariants(ModItems.vitaeLantern, new ModelResourceLocation(Reference.MOD_ID + ":vitae_lantern_empty", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":vitae_lantern_low", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":vitae_lantern_half",
				"inventory"), new ModelResourceLocation(Reference.MOD_ID + ":vitae_lantern_three_quarters", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":vitae_lantern_full", "inventory"));
		itemModelMesher.register(ModItems.starMetalKhopesh, 0, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_khopesh", "inventory"));
		itemModelMesher.register(ModItems.starMetalKhopesh, 1, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_khopesh_full_charge", "inventory"));
		ModelBakery.registerItemVariants(ModItems.starMetalKhopesh, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_khopesh", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":star_metal_khopesh_full_charge", "inventory"));
		itemModelMesher.register(ModItems.cloakOfAgility, 0, new ModelResourceLocation(Reference.MOD_ID + ":cloak_of_agility", "inventory"));
		itemModelMesher.register(ModItems.researchScrollCloakOfAgility, 0, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_cloak_of_agility", "inventory"));
		itemModelMesher.register(ModItems.researchScrollAstronomy2, 0, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_astronomy2", "inventory"));
		itemModelMesher.register(ModItems.researchScrollAstronomy2, 1, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_astronomy2", "inventory"));
		itemModelMesher.register(ModItems.researchScrollAstronomy2, 2, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_astronomy2", "inventory"));
		itemModelMesher.register(ModItems.researchScrollAstronomy2, 3, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_astronomy2", "inventory"));
		itemModelMesher.register(ModItems.researchScrollAstronomy2, 4, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_astronomy2", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 0, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_vitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 1, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_vitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 2, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_vitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 3, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_vitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 4, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_vitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollVitae1, 5, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_vitae1", "inventory"));
		itemModelMesher.register(ModItems.researchScrollWristCrossbow, 0, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_wrist_crossbow", "inventory"));
		itemModelMesher.register(ModItems.starMetalPlate, 0, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_plate", "inventory"));
		itemModelMesher.register(ModItems.enchantedSkeletonBone, 0, new ModelResourceLocation(Reference.MOD_ID + ":enchanted_skeleton_bone", "inventory"));
		itemModelMesher.register(ModItems.sleepingPotion, 0, new ModelResourceLocation(Reference.MOD_ID + ":sleeping_potion", "inventory"));
		itemModelMesher.register(ModItems.researchScrollInsanity, 0, new ModelResourceLocation(Reference.MOD_ID + ":research_scroll_insanity", "inventory"));
		itemModelMesher.register(ModItems.eldritchMetalIngot, 0, new ModelResourceLocation(Reference.MOD_ID + ":eldritch_metal_ingot", "inventory"));
		itemModelMesher.register(ModItems.starMetalIngot, 0, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_ingot", "inventory"));
		itemModelMesher.register(ModItems.bladeOfExhumation, 0, new ModelResourceLocation(Reference.MOD_ID + ":blade_of_exhumation", "inventory"));
		itemModelMesher.register(ModItems.flaskOfSouls, 0, new ModelResourceLocation(Reference.MOD_ID + ":flask_of_souls", "inventory"));
		itemModelMesher.register(ModItems.flaskOfSouls, 1, new ModelResourceLocation(Reference.MOD_ID + ":flask_of_souls_charged", "inventory"));
		ModelBakery.registerItemVariants(ModItems.flaskOfSouls, new ModelResourceLocation(Reference.MOD_ID + ":flask_of_souls", "inventory"), new ModelResourceLocation(Reference.MOD_ID + ":flask_of_souls_charged", "inventory"));
		itemModelMesher.register(ModItems.werewolfBlood, 0, new ModelResourceLocation(Reference.MOD_ID + ":werewolf_blood", "inventory"));
		itemModelMesher.register(ModItems.gnomishMetalIngot, 0, new ModelResourceLocation(Reference.MOD_ID + ":gnomish_metal_ingot", "inventory"));
		for (int i = 0; i < AOTDArt.values().length; i++)
			itemModelMesher.register(ModItems.artwork, i, new ModelResourceLocation(Reference.MOD_ID + ":artwork", "inventory"));
		itemModelMesher.register(ModItems.schematicGenerator, 0, new ModelResourceLocation(Reference.MOD_ID + ":schematic_generator", "inventory"));

		final ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
		final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		itemColors.registerItemColorHandler(new IItemColor()
		{
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex)
			{
				IBlockState iBlockState = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
				return blockColors.colorMultiplier(iBlockState, null, null, tintIndex);
			}
		}, ModBlocks.gravewoodLeaves, ModBlocks.mangroveLeaves);
	}

	@Override
	public void registerBlockRenders()
	{

		final ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.spring), 0, new ModelResourceLocation(Reference.MOD_ID + ":spring", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.darkForest), 0, new ModelResourceLocation(Reference.MOD_ID + ":dark_forest", "inventory"));

		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewood), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewood", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodLeaves), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewood_leaves", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodPlanks), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewood_planks", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodStairs), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewood_stairs", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewood_half_slab", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodDoubleSlab), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewood_upper_slab", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gravewoodSapling), 0, new ModelResourceLocation(Reference.MOD_ID + ":gravewood_sapling", "inventory"));

		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.mangrove), 0, new ModelResourceLocation(Reference.MOD_ID + ":mangrove", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.mangroveLeaves), 0, new ModelResourceLocation(Reference.MOD_ID + ":mangrove_leaves", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.mangrovePlanks), 0, new ModelResourceLocation(Reference.MOD_ID + ":mangrove_planks", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.mangroveStairs), 0, new ModelResourceLocation(Reference.MOD_ID + ":mangrove_stairs", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.mangroveHalfSlab), 0, new ModelResourceLocation(Reference.MOD_ID + ":mangrove_half_slab", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.mangroveDoubleSlab), 0, new ModelResourceLocation(Reference.MOD_ID + ":mangrove_upper_slab", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.mangroveSapling), 0, new ModelResourceLocation(Reference.MOD_ID + ":mangrove_sapling", "inventory"));

		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.sunstoneOre), 0, new ModelResourceLocation(Reference.MOD_ID + ":sunstone_ore", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.astralSilverOre), 0, new ModelResourceLocation(Reference.MOD_ID + ":astral_silver_ore", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.meteor), 0, new ModelResourceLocation(Reference.MOD_ID + ":meteor", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.starMetalOre), 0, new ModelResourceLocation(Reference.MOD_ID + ":star_metal_ore", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.igneousBlock), 0, new ModelResourceLocation(Reference.MOD_ID + ":igneous_block", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.vitaeDisenchanter), 0, new ModelResourceLocation(Reference.MOD_ID + ":vitae_disenchanter", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.voidChest), 0, new ModelResourceLocation(Reference.MOD_ID + ":void_chest", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.voidChestPortal), 0, new ModelResourceLocation(Reference.MOD_ID + ":void_chest_portal", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.eldritchObsidian), 0, new ModelResourceLocation(Reference.MOD_ID + ":eldritch_obsidian", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.amorphousEldritchMetal), 0, new ModelResourceLocation(Reference.MOD_ID + ":amorphous_eldritch_metal", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.eldritchStone), 0, new ModelResourceLocation(Reference.MOD_ID + ":eldritch_stone", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gnomishMetalPlate), 0, new ModelResourceLocation(Reference.MOD_ID + ":gnomish_metal_plate", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.gnomishMetalStrut), 0, new ModelResourceLocation(Reference.MOD_ID + ":gnomish_metal_strut", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.glowStalk), 0, new ModelResourceLocation(Reference.MOD_ID + ":glow_stalk", "inventory"));
		itemModelMesher.register(Item.getItemFromBlock(ModBlocks.enariaSpawner), 0, new ModelResourceLocation(Reference.MOD_ID + ":enaria_spawner", "inventory"));

		final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		blockColors.registerBlockColorHandler(new IBlockColor()
		{
			@Override
			public int colorMultiplier(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos, int tintIndex)
			{
				if (iBlockAccess != null && blockPos != null)
					return BiomeColorHelper.getFoliageColorAtPos(iBlockAccess, blockPos);
				return ColorizerFoliage.getFoliageColor(0.5, 1.0);
			}
		}, ModBlocks.gravewoodLeaves, ModBlocks.mangroveLeaves);
	}

	@Override
	public void preInit()
	{
		// Register custom models
		OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.enariasAltar), 0, new ModelResourceLocation(Reference.MOD_ID + ":enarias_altar", "inventory"));
	}
}
