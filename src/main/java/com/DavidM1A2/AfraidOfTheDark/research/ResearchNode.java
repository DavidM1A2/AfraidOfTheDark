/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.research;

public class ResearchNode
{
	// Is the research node researched?
	private boolean isResearched = false;
	// Name of the research and ID
	private ResearchTypes researchName;
	private ResearchTypes previousResearch = null;

	public ResearchNode(ResearchTypes researchName, ResearchTypes previousResearch)
	{
		this.researchName = researchName;
		this.previousResearch = previousResearch;
	}

	// Unlock this node
	public void unlockNode()
	{
		this.isResearched = true;
	}

	public ResearchTypes getType()
	{
		return this.researchName;
	}

	public ResearchTypes getPrevious()
	{
		return this.previousResearch;
	}

	// Is the node researched?
	public boolean isResearched()
	{
		return this.isResearched;
	}

	public String toString()
	{
		return "Name = " + this.researchName.toString() + "      isResearched? " + this.isResearched;
	}
}
