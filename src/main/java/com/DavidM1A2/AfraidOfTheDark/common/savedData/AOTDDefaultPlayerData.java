package com.DavidM1A2.AfraidOfTheDark.common.savedData;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellManager;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class AOTDDefaultPlayerData implements IAOTDPlayerData
{

	@Override
	public boolean getHasStartedAOTD()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHasStartedAOTD(boolean hasStartedAOTD)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void syncHasStartedAOTD()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public double getPlayerInsanity()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPlayerInsanity(double insanity)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void syncPlayerInsanity()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public NBTTagList getPlayerInventory()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPlayerInventory(NBTTagList inventory)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getPlayerLocationOverworld()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPlayerLocationOverworld(int[] location)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getPlayerLocationNightmare()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPlayerLocationNightmare(int location)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getPlayerLocationVoidChest()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPlayerLocationVoidChest(int location)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public NBTTagCompound getResearches()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReseraches(NBTTagCompound researches)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isResearched(ResearchTypes research)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canResearch(ResearchTypes research)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unlockResearch(ResearchTypes research, boolean firstTimeResearched)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void syncResearches()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getHasBeatenEnaria()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHasBeatenEnaria(boolean hasBeatenEnaria)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void syncHasBeatenEnaria()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelectedWristCrossbowBolt(int selectedWristCrossbowBolt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getSelectedWristCrossbowBolt()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void syncSelectedWristCrossbowBolt()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public SpellManager getSpellManager()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSpellManager(SpellManager spellManager)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void syncSpellManager()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void syncAll()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void requestSyncAll()
	{
		// TODO Auto-generated method stub

	}
}
