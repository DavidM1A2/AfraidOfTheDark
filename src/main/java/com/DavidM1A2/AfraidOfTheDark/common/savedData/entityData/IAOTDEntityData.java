/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.savedData.entityData;

public interface IAOTDEntityData
{ 
	public int getVitaeLevel();
	public boolean setVitaeLevel(int vitaeLevel);
	public void syncVitaeLevel();

	public void syncAll();
	public void requestSyncAll();
}
