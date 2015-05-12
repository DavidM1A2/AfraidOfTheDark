/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.research;

public class ResearchNode
{
	// Is the research node researched?
	private boolean isResearched = false;
	// Name of the research and ID
	private ResearchTypes type;

	public ResearchNode(ResearchTypes researchName)
	{
		this.type = researchName;
	}

	// Unlock this node
	public void unlockNode()
	{
		this.isResearched = true;
	}

	public ResearchTypes getType()
	{
		return this.type;
	}

	// Is the node researched?
	public boolean isResearched()
	{
		return this.isResearched;
	}

	public String toString()
	{
		return "Name = " + this.type.toString() + "      isResearched? " + this.isResearched;
	}
}
