package com.DavidM1A2.AfraidOfTheDark.common.savedData.playerData;

import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellManager;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class AOTDDefaultPlayerData implements IAOTDPlayerData
{

	@Override
	public boolean getHasStartedAOTD()
	{

		return false;
	}

	@Override
	public void setHasStartedAOTD(boolean hasStartedAOTD)
	{

	}

	@Override
	public void syncHasStartedAOTD()
	{

	}

	@Override
	public double getPlayerInsanity()
	{

		return 0;
	}

	@Override
	public void setPlayerInsanity(double insanity)
	{

	}

	@Override
	public void syncPlayerInsanity()
	{

	}

	@Override
	public NBTTagList getPlayerInventory()
	{

		return null;
	}

	@Override
	public void setPlayerInventory(NBTTagList inventory)
	{

	}

	@Override
	public int[] getPlayerLocationOverworld()
	{

		return null;
	}

	@Override
	public void setPlayerLocationOverworld(int[] location)
	{

	}

	@Override
	public int getPlayerLocationNightmare()
	{

		return 0;
	}

	@Override
	public void setPlayerLocationNightmare(int location)
	{

	}

	@Override
	public int getPlayerLocationVoidChest()
	{

		return 0;
	}

	@Override
	public void setPlayerLocationVoidChest(int location)
	{

	}

	@Override
	public NBTTagCompound getResearches()
	{

		return null;
	}

	@Override
	public void setReseraches(NBTTagCompound researches)
	{

	}

	@Override
	public boolean isResearched(ResearchTypes research)
	{

		return false;
	}

	@Override
	public boolean canResearch(ResearchTypes research)
	{

		return false;
	}

	@Override
	public void unlockResearch(ResearchTypes research, boolean firstTimeResearched)
	{

	}

	@Override
	public void syncResearches()
	{

	}

	@Override
	public boolean getHasBeatenEnaria()
	{

		return false;
	}

	@Override
	public void setHasBeatenEnaria(boolean hasBeatenEnaria)
	{

	}

	@Override
	public void syncHasBeatenEnaria()
	{

	}

	@Override
	public void setSelectedWristCrossbowBolt(int selectedWristCrossbowBolt)
	{

	}

	@Override
	public int getSelectedWristCrossbowBolt()
	{

		return 0;
	}

	@Override
	public void syncSelectedWristCrossbowBolt()
	{

	}

	@Override
	public SpellManager getSpellManager()
	{

		return null;
	}

	@Override
	public void setSpellManager(SpellManager spellManager)
	{

	}

	@Override
	public void syncSpellManager()
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
