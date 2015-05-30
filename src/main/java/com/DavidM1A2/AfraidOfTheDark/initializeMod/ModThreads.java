/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.threads.RandomInsanityUpdate;
import com.DavidM1A2.AfraidOfTheDark.threads.ResearchCompleteCheck;

public class ModThreads
{
	public static RandomInsanityUpdate randomInsanityUpdate;
	public static ResearchCompleteCheck researchCompleteUpdate;

	// These threads check for research completion server side and also randomly
	// hand out insanity updates
	public static void register()
	{
		ModThreads.randomInsanityUpdate = new RandomInsanityUpdate();
		ModThreads.researchCompleteUpdate = new ResearchCompleteCheck();
	}

	public static void startInGameThreads()
	{
		ModThreads.register();
		if (!ModThreads.randomInsanityUpdate.isAlive())
		{
			ModThreads.randomInsanityUpdate.start();
		}
		if (!ModThreads.researchCompleteUpdate.isAlive())
		{
			ModThreads.researchCompleteUpdate.start();
		}
	}

	public static void stopInGameThreads()
	{
		if (ModThreads.randomInsanityUpdate.isAlive())
		{
			ModThreads.randomInsanityUpdate.terminate();
		}
		if (ModThreads.researchCompleteUpdate.isAlive())
		{
			ModThreads.researchCompleteUpdate.terminate();
		}
	}
}
