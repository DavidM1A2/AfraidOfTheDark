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
		randomInsanityUpdate = new RandomInsanityUpdate();
		researchCompleteUpdate = new ResearchCompleteCheck();
	}

	public static void startInGameThreads()
	{
		register();
		if (!randomInsanityUpdate.isAlive())
		{
			randomInsanityUpdate.start();
		}
		if (!researchCompleteUpdate.isAlive())
		{
			researchCompleteUpdate.start();
		}
	}

	public static void stopInGameThreads()
	{
		if (randomInsanityUpdate.isAlive())
		{
			randomInsanityUpdate.terminate();
		}
		if (researchCompleteUpdate.isAlive())
		{
			researchCompleteUpdate.terminate();
		}
	}
}
