/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.research;

import java.util.ArrayList;
import java.util.List;

public class Research implements Cloneable
{
	/*
	 * 1 Look at werewolf, unlock werewolf entry 2 Learn how to refine silver 3 Learn how to infuse silver 4
	 */

	// Array list of various researches available to the user
	private List<ResearchNode> researches = new ArrayList<ResearchNode>()
	{
		{
			add(new ResearchNode(ResearchTypes.AnUnbreakableCovenant));
			add(new ResearchNode(ResearchTypes.WerewolfExamination));
			add(new ResearchNode(ResearchTypes.RefiningSilver));
			add(new ResearchNode(ResearchTypes.InfusingSilver));
		}
	};

	// Given an ID we can unlock a research by setting the node
	public void unlockResearch(ResearchTypes type)
	{
		for (int i = 0; i < researches.size(); i++)
		{
			if (researches.get(i).getType() == type)
			{
				researches.get(i).unlockNode();
			}
		}
	}

	public boolean isUnlocked(ResearchTypes type)
	{
		for (int i = 0; i < researches.size(); i++)
		{
			if (researches.get(i).getType() == type)
			{
				return researches.get(i).isResearched();
			}
		}
		return false;
	}

	public List<ResearchNode> getResearches()
	{
		return this.researches;
	}

	public static int getResearchAmount()
	{
		return 4;
	}

	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String toString()
	{
		String toReturn = "\n";
		for (int i = 0; i < getResearchAmount(); i++)
		{
			toReturn = toReturn + this.getResearches().get(i).toString() + "\n";
		}
		return toReturn;
	}
}
