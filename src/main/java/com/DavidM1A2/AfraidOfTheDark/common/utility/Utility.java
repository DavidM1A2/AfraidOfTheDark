/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest.VoidChestTeleporter;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Insanity;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Vitae;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedAOTDUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedInsanityUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedResearchUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedVitaeUpdate;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class Utility
{
	public static int ticksToMilliseconds(int ticks)
	{
		return ticks * 50;
	}

	public static double clampDouble(double val, double min, double max)
	{
		return Math.max(min, Math.min(max, val));
	}

	public static boolean hasIndex(List<?> list, int index)
	{
		try
		{
			list.get(index);
			return true;
		}
		catch (IndexOutOfBoundsException e)
		{
			return false;
		}
	}

	public static ConvertedRecipe getConvertedRecipeFromIRecipe(IRecipe currentRecipe)
	{
		int width = 0;
		int height = 0;
		ItemStack output = currentRecipe.getRecipeOutput();
		ItemStack[] input = null;

		if (currentRecipe instanceof ShapedRecipes)
		{
			ShapedRecipes shapedRecipe = (ShapedRecipes) currentRecipe;
			width = shapedRecipe.recipeWidth;
			height = shapedRecipe.recipeHeight;
			input = shapedRecipe.recipeItems;
		}
		else if (currentRecipe instanceof ShapedOreRecipe)
		{
			ShapedOreRecipe shapedOreRecipe = (ShapedOreRecipe) currentRecipe;
			Field[] fields = ShapedOreRecipe.class.getDeclaredFields();
			fields[4].setAccessible(true);
			fields[5].setAccessible(true);
			try
			{
				width = (Integer) fields[4].get(shapedOreRecipe);
				height = (Integer) fields[5].get(shapedOreRecipe);// reflection
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			fields[4].setAccessible(false);
			fields[5].setAccessible(false);
			input = new ItemStack[shapedOreRecipe.getInput().length];
			for (int i = 0; i < shapedOreRecipe.getInput().length; i++)
			{
				Object object = shapedOreRecipe.getInput()[i];
				if (object instanceof Item)
				{
					input[i] = new ItemStack((Item) object, 1, 0);
				}
				else if (object instanceof Block)
				{
					input[i] = new ItemStack((Block) object, 1, 0);
				}
				else if (object instanceof ItemStack)
				{
					input[i] = (ItemStack) object;
				}
				else if (object instanceof List)
				{
					// Don't fully support ore dictionary yet
					List<ItemStack> oreDictionaryList = (List<ItemStack>) object;
					if (!oreDictionaryList.isEmpty())
					{
						input[i] = oreDictionaryList.get(0);
					}
				}
			}
		}
		else if (currentRecipe instanceof ShapelessRecipes)
		{
			ShapelessRecipes shapelessRecipe = (ShapelessRecipes) currentRecipe;
			width = -1;
			height = -1;
			List<?> requiredItems = shapelessRecipe.recipeItems;
			input = new ItemStack[requiredItems.size()];
			for (int i = 0; i < requiredItems.size(); i++)
			{
				Object object = requiredItems.get(i);
				if (object instanceof Item)
				{
					input[i] = new ItemStack((Item) object, 1, 0);
				}
				else if (object instanceof Block)
				{
					input[i] = new ItemStack((Block) object, 1, 0);
				}
				else
				{
					input[i] = (ItemStack) object;
				}
			}
		}
		else if (currentRecipe instanceof ShapelessOreRecipe)
		{
			ShapelessOreRecipe shapelessOreRecipe = (ShapelessOreRecipe) currentRecipe;
			width = -1;
			height = -1;
			input = new ItemStack[shapelessOreRecipe.getInput().size()];
			List<Object> requiredItems = shapelessOreRecipe.getInput();
			for (int i = 0; i < requiredItems.size(); i++)
			{
				Object object = requiredItems.get(i);
				if (object instanceof Item)
				{
					input[i] = new ItemStack((Item) object, 1, 0);
				}
				else if (object instanceof Block)
				{
					input[i] = new ItemStack((Block) object, 1, 0);
				}
				else if (object instanceof ItemStack)
				{
					input[i] = (ItemStack) object;
				}
				else if (object instanceof List)
				{
					// Don't fully support ore dictionary yet
					List<ItemStack> oreDictionaryList = (List<ItemStack>) object;
					if (!oreDictionaryList.isEmpty())
					{
						input[i] = oreDictionaryList.get(0);
					}
				}
			}
		}

		if (width != 0)
		{
			return new ConvertedRecipe(width, height, output, input);
		}
		return null;
	}

	public static InputStream getInputStreamFromPath(String path)
	{
		InputStream inputStream = Utility.class.getClassLoader().getResourceAsStream(path);
		if (inputStream == null)
		{
			inputStream = Utility.class.getClassLoader().getResourceAsStream("assets/afraidofthedark/researchNotes/None.txt");
		}
		return inputStream;
	}

	public static void sendPlayerToVoidChest(EntityPlayerMP entityPlayer, int location)
	{
		Utility.sendPlayerToDimension(entityPlayer, Constants.VoidChestWorld.VOID_CHEST_WORLD_ID, false, VoidChestTeleporter.class);
		entityPlayer.playerNetServerHandler.setPlayerLocation(location * Constants.VoidChestWorld.BLOCKS_BETWEEN_ISLANDS + 24.5, 104, 3, 0, 0);
	}

	/*
	 * See EntityPlayerMP.travelToDimension
	 */
	public static void sendPlayerToDimension(EntityPlayerMP entityPlayer, int dimensionId, boolean spawnPortal, Class<? extends Teleporter> teleporter)
	{
		ServerConfigurationManager serverConfigurationManager = entityPlayer.mcServer.getConfigurationManager();
		int j = entityPlayer.dimension;
		WorldServer worldserver = serverConfigurationManager.getServerInstance().worldServerForDimension(entityPlayer.dimension);
		entityPlayer.dimension = dimensionId;
		WorldServer worldserver1 = serverConfigurationManager.getServerInstance().worldServerForDimension(entityPlayer.dimension);
		entityPlayer.playerNetServerHandler.sendPacket(new S07PacketRespawn(entityPlayer.dimension, worldserver1.getDifficulty(), worldserver1.getWorldInfo().getTerrainType(), entityPlayer.theItemInWorldManager.getGameType()));
		worldserver.removePlayerEntityDangerously(entityPlayer);
		entityPlayer.isDead = false;

		if (!spawnPortal)
		{
			try
			{
				serverConfigurationManager.transferEntityToWorld(entityPlayer, j, worldserver, worldserver1, teleporter.getDeclaredConstructor(WorldServer.class, int.class, int.class).newInstance(worldserver1, j, dimensionId));
			}
			catch (Exception e)
			{
				LogHelper.info("Error gererating portal at line 219 utility");
			}
		}
		else
		{
			serverConfigurationManager.transferEntityToWorld(entityPlayer, j, worldserver, worldserver1, worldserver1.getDefaultTeleporter());
		}

		serverConfigurationManager.func_72375_a(entityPlayer, worldserver);
		entityPlayer.playerNetServerHandler.setPlayerLocation(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, entityPlayer.rotationYaw, entityPlayer.rotationPitch);
		entityPlayer.theItemInWorldManager.setWorld(worldserver1);
		serverConfigurationManager.updateTimeAndWeatherForPlayer(entityPlayer, worldserver1);
		serverConfigurationManager.syncPlayerInventory(entityPlayer);
		Iterator iterator = entityPlayer.getActivePotionEffects().iterator();

		while (iterator.hasNext())
		{
			PotionEffect potioneffect = (PotionEffect) iterator.next();
			entityPlayer.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(entityPlayer.getEntityId(), potioneffect));
		}

		FMLCommonHandler.instance().firePlayerChangedDimensionEvent(entityPlayer, j, dimensionId);

		//new DelayedAOTDUpdate(600, entityPlayer, HasStartedAOTD.get(entityPlayer)).start();
		//new DelayedInsanityUpdate(700, entityPlayer, Insanity.get(entityPlayer)).start();
		//new DelayedResearchUpdate(800, entityPlayer, Research.get(entityPlayer)).start();
		//new DelayedVitaeUpdate(900, entityPlayer, Vitae.get(entityPlayer)).start();
		Constants.TIMER_FOR_DELAYS.schedule(new DelayedAOTDUpdate(entityPlayer, HasStartedAOTD.get(entityPlayer)), 500, TimeUnit.MILLISECONDS);
		Constants.TIMER_FOR_DELAYS.schedule(new DelayedInsanityUpdate(entityPlayer, Insanity.get(entityPlayer)), 600, TimeUnit.MILLISECONDS);
		Constants.TIMER_FOR_DELAYS.schedule(new DelayedResearchUpdate(entityPlayer, Research.get(entityPlayer)), 700, TimeUnit.MILLISECONDS);
		Constants.TIMER_FOR_DELAYS.schedule(new DelayedVitaeUpdate(entityPlayer, Vitae.get(entityPlayer)), 800, TimeUnit.MILLISECONDS);
	}
}
