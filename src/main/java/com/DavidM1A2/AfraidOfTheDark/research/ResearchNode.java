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
	private String name = "";
	private int ID = -1;

	public ResearchNode(int ID, String researchName)
	{
		this.name = researchName;
		this.ID = ID;
	}

	// Unlock this node
	public void unlockNode()
	{
		this.isResearched = true;
	}

	// Get the node ID
	public int getNodeID()
	{
		return this.ID;
	}

	// Is the node researched?
	public boolean isResearched()
	{
		return this.isResearched;
	}

	public String toString()
	{
		return "Name = " + this.name + "      isResearched? " + this.isResearched;
	}
}
