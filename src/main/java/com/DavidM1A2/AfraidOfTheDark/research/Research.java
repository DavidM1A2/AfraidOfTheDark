package com.DavidM1A2.AfraidOfTheDark.research;

import java.util.ArrayList;
import java.util.List;

public class Research
{
	/*
	 * 1 Look at werewolf, unlock werewolf entry 2 Learn how to refine silver 3
	 * Learn how to infuse silver 4
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 1 Journal 2 3
	 */

	private List<ResearchNode> researches = new ArrayList<ResearchNode>()
	{
		{
			add(new ResearchNode(0, "An Unbreakable Covenant"));
			add(new ResearchNode(1, "Werewolf Examination"));
			add(new ResearchNode(2, "Refining Silver"));
			add(new ResearchNode(3, "Infusing Silver"));
		}
	};

	public void unlockResearch(int ID)
	{
		for (int i = 0; i < researches.size(); i++)
		{
			if (researches.get(i).getNodeID() == ID)
			{
				researches.get(i).unlockNode();
			}
		}
	}

	public byte[] unlockedResearches()
	{
		byte[] unlockedResearches = new byte[researches.size()];

		for (int i = 0; i < researches.size(); i++)
		{
			if (researches.get(i).isResearched())
			{
				unlockedResearches[i] = 1;
			}
			else
			{
				unlockedResearches[i] = 0;
			}
		}

		return unlockedResearches;
	}

	public boolean isUnlocked(int id)
	{
		return researches.get(id).isResearched();
	}
}
