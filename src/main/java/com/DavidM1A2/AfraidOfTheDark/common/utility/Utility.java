/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class Utility
{
	private static final Random random = new Random();

	public static int randInt(int min, int max)
	{
		if (min > max)
		{
			int temp = max;
			max = min;
			min = temp;
		}
		return Utility.random.nextInt((max - min) + 1) + min;
	}

	public static boolean hasIndex(List<?> list, int index)
	{
		return index >= 0 && index < list.size();
	}

	public static <T> boolean hasIndex(T[] array, int index)
	{
		return index >= 0 && index < array.length;
	}

	public static final boolean hasItem(EntityPlayer entityPlayer, Item item)
	{
		for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
			if (itemStack.getItem() == item)
				return true;
		return false;
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

	public static void createExplosionWithoutBlockDamageServer(World world, Entity entity, double x, double y, double z, int strength, boolean isFlaming, boolean isSmoking)
	{
		Explosion explosion = new Explosion(world, entity, x, y, z, strength, isFlaming, isSmoking);
		if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion))
			return;
		explosion.doExplosionB(false);

		if (!isSmoking)
		{
			explosion.clearAffectedBlockPositions();
		}

		Iterator iterator = world.playerEntities.iterator();

		while (iterator.hasNext())
		{
			EntityPlayer entityplayer = (EntityPlayer) iterator.next();

			if (entityplayer.getDistanceSq(x, y, z) < 4096.0D)
			{
				((EntityPlayerMP) entityplayer).connection.sendPacket(new SPacketExplosion(x, y, z, strength, explosion.getAffectedBlockPositions(), (Vec3d) explosion.getPlayerKnockbackMap().get(entityplayer)));
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

	// http://www.obsidianscheduler.com/blog/easy-deep-cloning-of-serializable-and-non-serializable-objects-in-java/
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(T t)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			serializeToOutputStream(t, bos);
			byte[] bytes = bos.toByteArray();
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
			return (T) ois.readObject();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static void serializeToOutputStream(Serializable ser, OutputStream os) throws IOException
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(os))
		{
			oos.writeObject(ser);
			oos.flush();
		}
	}
}
