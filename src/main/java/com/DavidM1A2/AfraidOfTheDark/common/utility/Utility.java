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

import com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest.VoidChestTeleporter;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class Utility
{
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
	}

	public static void createExplosionWithoutBlockDamageServer(World world, Entity entity, double x, double y, double z, int strength, boolean isFlaming, boolean isSmoking)
	{
		Explosion explosion = new Explosion(world, entity, x, y, z, strength, isFlaming, isSmoking);
		if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion))
			return;
		explosion.doExplosionB(false);

		if (!isSmoking)
		{
			explosion.func_180342_d();
		}

		Iterator iterator = world.playerEntities.iterator();

		while (iterator.hasNext())
		{
			EntityPlayer entityplayer = (EntityPlayer) iterator.next();

			if (entityplayer.getDistanceSq(x, y, z) < 4096.0D)
			{
				((EntityPlayerMP) entityplayer).playerNetServerHandler.sendPacket(new S27PacketExplosion(x, y, z, strength, explosion.func_180343_e(), (Vec3) explosion.func_77277_b().get(entityplayer)));
			}
		}
	}

	public static void createExplosionWithoutBlockDamageClient(World world, Entity entity, double x, double y, double z, int strength, boolean isFlaming, boolean isSmoking)
	{
		Explosion explosion = new Explosion(world, entity, x, y, z, strength, isFlaming, isSmoking);
		if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion))
			return;
		explosion.doExplosionB(true);
	}

	public static MovingObjectPosition rayTraceServerSide(Entity entity, double distance, float eyeHeight)
	{
		Vec3 locationFrom = new Vec3(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ);
		Vec3 look = entity.getLook(eyeHeight);
		Vec3 locationTo = locationFrom.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
		return entity.worldObj.rayTraceBlocks(locationFrom, locationTo, false, false, true);
	}
}
