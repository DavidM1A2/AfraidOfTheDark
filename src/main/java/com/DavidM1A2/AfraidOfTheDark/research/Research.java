/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.research;

import java.util.ArrayList;
import java.util.List;

public class Research implements Cloneable
{
	// Array list of various researches available to the user
	private List<ResearchNode> researches = new ArrayList<ResearchNode>()
	{
		{
			add(new ResearchNode(ResearchTypes.AnUnbreakableCovenant, null));
			add(new ResearchNode(ResearchTypes.WerewolfExamination, ResearchTypes.AnUnbreakableCovenant));
			add(new ResearchNode(ResearchTypes.Crossbow, null));
			add(new ResearchNode(ResearchTypes.Astronomy1, ResearchTypes.WerewolfExamination));
			add(new ResearchNode(ResearchTypes.AstralSilver, ResearchTypes.Astronomy1));
			add(new ResearchNode(ResearchTypes.SilverInfusion, ResearchTypes.AstralSilver));
			add(new ResearchNode(ResearchTypes.DarkForest, ResearchTypes.AstralSilver));
			add(new ResearchNode(ResearchTypes.Astronomy2, ResearchTypes.DarkForest));
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

	public boolean isPreviousResearched(ResearchTypes type)
	{
		return getResearch(getResearch(type).getPrevious()).isResearched();
	}

	public ResearchNode getResearch(ResearchTypes type)
	{
		for (int i = 0; i < researches.size(); i++)
		{
			if (researches.get(i).getType() == type)
			{
				return researches.get(i);
			}
		}
		return null;
	}

	public static int getResearchAmount()
	{
		return 8;
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
