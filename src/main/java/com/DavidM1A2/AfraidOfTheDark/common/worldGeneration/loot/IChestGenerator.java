/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot;

import java.util.List;
import java.util.Random;

public interface IChestGenerator
{
	public List<RandomItem> getPossibleItems(Random random);
}
