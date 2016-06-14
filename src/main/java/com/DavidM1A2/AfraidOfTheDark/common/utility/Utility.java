/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.Vec3;
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
			explosion.func_180342_d();
		}

		Iterator iterator = world.playerEntities.iterator();

		while (iterator.hasNext())
		{
			EntityPlayer entityplayer = (EntityPlayer) iterator.next();

			if (entityplayer.getDistanceSq(x, y, z) < 4096.0D)
			{
				((EntityPlayerMP) entityplayer).playerNetServerHandler.sendPacket(new S27PacketExplosion(x, y, z, strength, explosion.getAffectedBlockPositions(), (Vec3) explosion.getPlayerKnockbackMap().get(entityplayer)));
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
