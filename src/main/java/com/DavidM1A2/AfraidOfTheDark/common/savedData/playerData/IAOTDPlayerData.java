package com.DavidM1A2.AfraidOfTheDark.common.savedData.playerData;

import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellManager;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public interface IAOTDPlayerData
{
	public boolean getHasStartedAOTD();

	public void setHasStartedAOTD(boolean hasStartedAOTD);

	public void syncHasStartedAOTD();

	public double getPlayerInsanity();

	public void setPlayerInsanity(double insanity);

	public void syncPlayerInsanity();

	public NBTTagList getPlayerInventory();

	public void setPlayerInventory(NBTTagList inventory);
	// Auto synced

	public Point3D getPlayerLocationPreTeleport();

	public int getPlayerDimensionPreTeleport();

	public void setPlayerLocationPreTeleport(Point3D location, int dimensionID);

	public int getPlayerLocationNightmare();

	public void setPlayerLocationNightmare(int location);

	public int getPlayerLocationVoidChest();

	public void setPlayerLocationVoidChest(int location);

	public NBTTagCompound getResearches();

	public void setReseraches(NBTTagCompound researches);

	public boolean isResearched(ResearchTypes research);

	public boolean canResearch(ResearchTypes research);

	public void unlockResearch(ResearchTypes research, boolean firstTimeResearched);

	public void syncResearches();

	public void setSelectedWristCrossbowBolt(int selectedWristCrossbowBolt);

	public int getSelectedWristCrossbowBolt();

	public void syncSelectedWristCrossbowBolt();

	public SpellManager getSpellManager();

	public void setSpellManager(SpellManager spellManager);

	public void syncSpellManager();

	public boolean hasEnariasAltar();

	public void setHasEnariasAltar(boolean enariasAltarGenerated);

	public void syncHasEnariasAltar();

	public void syncAll();

	public void requestSyncAll();
}
