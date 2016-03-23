/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.savedData.entityData;

public class AOTDDefaultEntityData implements IAOTDEntityData
{

	@Override
	public int getVitaeLevel()
	{
		return 0;
	}

	@Override
	public boolean setVitaeLevel(int vitaeLevel)
	{
		return false;
	}

	@Override
	public void syncVitaeLevel()
	{

	}

	@Override
	public void syncAll()
	{

	}

	@Override
	public void requestSyncAll()
	{

	}

}
