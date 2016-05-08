package com.DavidM1A2.AfraidOfTheDark.common.savedData.playerData;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncSelectedWristCrossbowBolt;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncSpellManager;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateHasBeatenEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellManager;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class AOTDPlayerData implements ICapabilitySerializable<NBTTagCompound>, IAOTDPlayerData
{
	private final EntityPlayer entityPlayer;

	private boolean hasStartedAOTD = false;
	private double playerInsanity;
	private NBTTagList inventoryList = new NBTTagList();
	private Point3D playerLocationPreTeleport = new Point3D(0, 200, 0);
	private int playerDimensionPreTeleport;
	private int playerLocationNightmare;
	private int playerLocationVoidChest;
	private NBTTagCompound researches = new NBTTagCompound();
	private boolean hasBeatenEnaria;
	private int selectedWristCrossbowBolt = 0;
	private SpellManager spellManager = new SpellManager();
	private final static String PLAYER_LOCATION_VOID_CHEST = "playerLocationVoidChest";
	private final static String PLAYER_LOCATION_NIGHTMARE = "playerLocationNightmare";
	private final static String RESEARCH_DATA = "unlockedResearches";

	public AOTDPlayerData(EntityPlayer entityPlayer)
	{
		this.entityPlayer = entityPlayer;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return ModCapabilities.PLAYER_DATA != null && capability == ModCapabilities.PLAYER_DATA;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (ModCapabilities.PLAYER_DATA != null && capability == ModCapabilities.PLAYER_DATA)
		{
			return (T) this;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return (NBTTagCompound) ModCapabilities.PLAYER_DATA.getStorage().writeNBT(ModCapabilities.PLAYER_DATA, this, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound compound)
	{
		ModCapabilities.PLAYER_DATA.getStorage().readNBT(ModCapabilities.PLAYER_DATA, this, null, compound);
	}

	private boolean isServerSide()
	{
		return entityPlayer instanceof EntityPlayerMP;
	}

	public boolean getHasStartedAOTD()
	{
		return this.hasStartedAOTD;
	}

	public void setHasStartedAOTD(boolean hasStartedAOTD)
	{
		this.hasStartedAOTD = hasStartedAOTD;
	}

	public void syncHasStartedAOTD()
	{
		if (this.isServerSide())
		{
			AfraidOfTheDark.instance.getPacketHandler().sendTo(new UpdateAOTDStatus(this.getHasStartedAOTD()), (EntityPlayerMP) entityPlayer);
		}
		else
		{
			AfraidOfTheDark.instance.getPacketHandler().sendToServer(new UpdateAOTDStatus(this.getHasStartedAOTD()));
		}
	}

	public double getPlayerInsanity()
	{
		return this.playerInsanity;
	}

	public void setPlayerInsanity(double insanity)
	{
		this.playerInsanity = MathHelper.clamp_double(insanity, 0, 100);
	}

	public void syncPlayerInsanity()
	{
		if (this.isServerSide())
		{
			AfraidOfTheDark.instance.getPacketHandler().sendTo(new UpdateInsanity(this.getPlayerInsanity()), (EntityPlayerMP) entityPlayer);
		}
		else
		{
			// Can't send it the other way
		}
	}

	public NBTTagList getPlayerInventory()
	{
		return this.inventoryList;
	}

	public void setPlayerInventory(NBTTagList inventory)
	{
		this.inventoryList = inventory;
	}

	@Override
	public Point3D getPlayerLocationPreTeleport()
	{
		return this.playerLocationPreTeleport;
	}

	@Override
	public int getPlayerDimensionPreTeleport()
	{
		return this.playerDimensionPreTeleport;
	}

	@Override
	public void setPlayerLocationPreTeleport(Point3D location, int dimensionID)
	{
		this.playerDimensionPreTeleport = dimensionID;
		this.playerLocationPreTeleport = location;
	}

	public int getPlayerLocationNightmare()
	{
		return this.playerLocationNightmare;
	}

	public static int getPlayerLocationNightmareOffline(NBTTagCompound nbt)
	{
		return nbt.getCompoundTag("ForgeData").getInteger(PLAYER_LOCATION_NIGHTMARE);
	}

	public void setPlayerLocationNightmare(int location)
	{
		this.playerLocationNightmare = location;
	}

	public int getPlayerLocationVoidChest()
	{
		return this.playerLocationVoidChest;
	}

	public static int getPlayerLocationVoidChestOffline(NBTTagCompound nbt)
	{
		return nbt.getCompoundTag("ForgeData").getInteger(PLAYER_LOCATION_VOID_CHEST);
	}

	public void setPlayerLocationVoidChest(int location)
	{
		this.playerLocationVoidChest = location;
	}

	public NBTTagCompound getResearches()
	{
		return this.researches;
	}

	public void setReseraches(NBTTagCompound researches)
	{
		this.researches = researches;
	}

	public boolean isResearched(ResearchTypes research)
	{
		return this.getResearches().getBoolean(RESEARCH_DATA + research.toString());
	}

	public boolean canResearch(ResearchTypes research)
	{
		return this.getHasStartedAOTD() && !this.isResearched(research) && (research.getPrevious() != null ? this.isResearched(research.getPrevious()) : true);
	}

	public void unlockResearch(ResearchTypes research, boolean firstTimeResearched)
	{
		this.researches.setBoolean(RESEARCH_DATA + research.toString(), true);
		if (!this.isServerSide() && firstTimeResearched)
		{
			ClientData.researchAchievedOverlay.displayResearch(research, new ItemStack(ModItems.journal, 1), false);
			entityPlayer.playSound("afraidofthedark:achievementUnlocked", 1.0f, 1.0f);
		}
		this.syncResearches();
	}

	public void syncResearches()
	{
		if (this.isServerSide())
		{
			AfraidOfTheDark.instance.getPacketHandler().sendTo(new UpdateResearch(this.researches), (EntityPlayerMP) entityPlayer);
		}
		else
		{
			AfraidOfTheDark.instance.getPacketHandler().sendToServer(new UpdateResearch(this.researches));
		}
	}

	public boolean getHasBeatenEnaria()
	{
		return this.hasBeatenEnaria;
	}

	public void setHasBeatenEnaria(boolean hasBeatenEnaria)
	{
		this.hasBeatenEnaria = hasBeatenEnaria;
	}

	public void syncHasBeatenEnaria()
	{
		if (this.isServerSide())
		{
			AfraidOfTheDark.instance.getPacketHandler().sendTo(new UpdateHasBeatenEnaria(this.hasBeatenEnaria), (EntityPlayerMP) this.entityPlayer);
		}
	}

	public void setSelectedWristCrossbowBolt(int selectedWristCrossbowBolt)
	{
		this.selectedWristCrossbowBolt = selectedWristCrossbowBolt;
	}

	public int getSelectedWristCrossbowBolt()
	{
		return this.selectedWristCrossbowBolt;
	}

	public void syncSelectedWristCrossbowBolt()
	{
		if (!this.isServerSide())
		{
			AfraidOfTheDark.instance.getPacketHandler().sendToServer(new SyncSelectedWristCrossbowBolt(this.selectedWristCrossbowBolt));
		}
	}

	public SpellManager getSpellManager()
	{
		return this.spellManager;
	}

	public void setSpellManager(SpellManager spellManager)
	{
		this.spellManager = spellManager;
	}

	public void syncSpellManager()
	{
		if (!this.isServerSide())
		{
			AfraidOfTheDark.instance.getPacketHandler().sendToServer(new SyncSpellManager(this.spellManager));
		}
		else
		{
			AfraidOfTheDark.instance.getPacketHandler().sendTo(new SyncSpellManager(this.spellManager), (EntityPlayerMP) this.entityPlayer);
		}
	}

	public void syncAll()
	{
		if (this.isServerSide())
		{
			AfraidOfTheDark.instance.getPacketHandler().sendTo(new SyncAOTDPlayerData(this), (EntityPlayerMP) this.entityPlayer);
		}
	}

	public void requestSyncAll()
	{
		if (!this.isServerSide())
		{
			AfraidOfTheDark.instance.getPacketHandler().sendToServer(new SyncAOTDPlayerData());
		}
	}
}
