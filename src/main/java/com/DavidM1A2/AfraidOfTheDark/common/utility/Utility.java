/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.TrueTypeFont;
import com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest.VoidChestTeleporter;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Utility
{
	public static TrueTypeFont createTrueTypeFont(String name, float size, boolean antiAliasing)
	{
		try
		{
			final InputStream fontInputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Refrence.MOD_ID, "fonts/" + name + ".ttf")).getInputStream();
			return new TrueTypeFont(Font.createFont(Font.TRUETYPE_FONT, fontInputStream).deriveFont(size), antiAliasing);
		}
		catch (final FileNotFoundException e)
		{
			LogHelper.error("Error loading AOTD fonts. This will cause your minecraft to crash. Please mention this to the mod developer.");
			return null;
		}
		catch (final IOException e)
		{
			LogHelper.error("Error loading AOTD fonts. This will cause your minecraft to crash. Please mention this to the mod developer.");
			return null;
		}
		catch (FontFormatException e)
		{
			LogHelper.error("Error loading AOTD fonts. This will cause your minecraft to crash. Please mention this to the mod developer.");
			return null;
		}
	}

	public static boolean hasIndex(List<?> list, int index)
	{
		return index >= 0 && index < list.size();
	}

	public static <T> boolean hasIndex(T[] array, int index)
	{
		return index >= 0 && index < array.length;
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
}
