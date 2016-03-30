/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.reference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDrone;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public final class Constants
{

	public static Map<Class, Integer> entityVitaeResistance = new HashMap<Class, Integer>();
	public static Map<String, Integer> toolMaterialRepairCosts = new HashMap<String, Integer>();

	public static final ScheduledExecutorService TIMER_FOR_DELAYS = Executors.newSingleThreadScheduledExecutor();

	static
	{
		entityVitaeResistance.put(EntityPlayer.class, 100);
		entityVitaeResistance.put(EntityPlayerMP.class, 100);
		entityVitaeResistance.put(EntityBat.class, 5);
		entityVitaeResistance.put(EntityChicken.class, 10);
		entityVitaeResistance.put(EntityCow.class, 50);
		entityVitaeResistance.put(EntityMooshroom.class, 50);
		entityVitaeResistance.put(EntityPig.class, 40);
		entityVitaeResistance.put(EntityRabbit.class, 5);
		entityVitaeResistance.put(EntitySheep.class, 40);
		entityVitaeResistance.put(EntitySquid.class, 30);
		entityVitaeResistance.put(EntityVillager.class, 100);
		entityVitaeResistance.put(EntityCaveSpider.class, 90);
		entityVitaeResistance.put(EntityEnderman.class, 105);
		entityVitaeResistance.put(EntitySpider.class, 60);
		entityVitaeResistance.put(EntityPigZombie.class, 100);
		entityVitaeResistance.put(EntityBlaze.class, 100);
		entityVitaeResistance.put(EntityCreeper.class, 100);
		entityVitaeResistance.put(EntityGuardian.class, 150);
		entityVitaeResistance.put(EntityEndermite.class, 4);
		entityVitaeResistance.put(EntityGhast.class, 115);
		entityVitaeResistance.put(EntityMagmaCube.class, 60);
		entityVitaeResistance.put(EntitySilverfish.class, 25);
		entityVitaeResistance.put(EntitySkeleton.class, 100);
		entityVitaeResistance.put(EntitySlime.class, 85);
		entityVitaeResistance.put(EntityWitch.class, 100);
		entityVitaeResistance.put(EntityZombie.class, 100);
		entityVitaeResistance.put(EntityHorse.class, 50);
		entityVitaeResistance.put(EntityOcelot.class, 25);
		entityVitaeResistance.put(EntityWolf.class, 70);
		entityVitaeResistance.put(EntityIronGolem.class, 190);
		entityVitaeResistance.put(EntitySnowman.class, 15);
		entityVitaeResistance.put(EntityDragon.class, 300);
		entityVitaeResistance.put(EntityWither.class, 300);
		entityVitaeResistance.put(EntityWerewolf.class, 120);
		entityVitaeResistance.put(EntityDeeeSyft.class, 150);
		entityVitaeResistance.put(EntityEnchantedSkeleton.class, 100);
		entityVitaeResistance.put(EntitySplinterDrone.class, 100);
		entityVitaeResistance.put(EntityEnaria.class, Integer.MAX_VALUE);

		toolMaterialRepairCosts.put("EMERALD", 4);
		toolMaterialRepairCosts.put("GOLD", 5);
		toolMaterialRepairCosts.put("IRON", 3);
		toolMaterialRepairCosts.put("STONE", 1);
		toolMaterialRepairCosts.put("WOOD", 1);

		toolMaterialRepairCosts.put("LEATHER", 1);
		toolMaterialRepairCosts.put("CHAINMAIL", 2);
		toolMaterialRepairCosts.put("DIAMOND", 4);

		toolMaterialRepairCosts.put("silverTool", 4);
		toolMaterialRepairCosts.put("igneousTool", 4);
		toolMaterialRepairCosts.put("starMetalTool", 4);
		toolMaterialRepairCosts.put("igneous", 4);
		toolMaterialRepairCosts.put("starMetal", 4);
	}
}
