package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.List;
import java.util.Random;

import net.minecraft.util.WeightedRandomChestContent;

public interface IChestGenerator
{
	public List<WeightedRandomChestContent> getPossibleItems(Random random);
}
