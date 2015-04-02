package com.DavidM1A2.AfraidOfTheDark.research;

public class ResearchNode 
{
	private boolean isResearched = false;
	private String name = "";
	private int ID = -1;
	
	public ResearchNode(int ID, String researchName)
	{
		this.name = researchName;
		this.ID = ID;
	}
	
	public void unlockNode()
	{
		this.isResearched = true;
	}
	
	public int getNodeID()
	{
		return this.ID;
	}
	
	public boolean isResearched()
	{
		return this.isResearched;
	}
}
