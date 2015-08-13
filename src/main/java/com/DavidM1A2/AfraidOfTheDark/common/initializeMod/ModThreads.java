/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.threads.RandomInsanityUpdate;

public class ModThreads
{
	public static RandomInsanityUpdate randomInsanityUpdate;

	// These threads check for research completion server side and also randomly
	// hand out insanity updates
	public static void register()
	{
		ModThreads.randomInsanityUpdate = new RandomInsanityUpdate();
	}

	public static void startInGameThreads()
	{
		ModThreads.register();
		if (!ModThreads.randomInsanityUpdate.isAlive())
		{
			ModThreads.randomInsanityUpdate.start();
		}
	}

	public static void stopInGameThreads()
	{
		if (ModThreads.randomInsanityUpdate.isAlive())
		{
			ModThreads.randomInsanityUpdate.terminate();
		}
	}
}
