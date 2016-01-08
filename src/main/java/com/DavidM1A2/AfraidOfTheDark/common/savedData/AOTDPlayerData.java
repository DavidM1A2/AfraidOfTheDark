/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */

package com.DavidM1A2.AfraidOfTheDark.common.savedData;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncSelectedWristCrossbowBolt;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateHasBeatenEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellManager;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTObjectWriter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class AOTDPlayerData implements IExtendedEntityProperties
{
	// PROPERTIES =============================================================

	private final EntityPlayer entityPlayer;

	private boolean hasStartedAOTD = false;
	private double playerInsanity;
	private NBTTagList inventoryList = new NBTTagList();
	private int[] playerLocationOverworld = new int[3];
	private int playerLocationNightmare;
	private int playerLocationVoidChest;
	private NBTTagCompound researches = new NBTTagCompound();
	private boolean hasBeatenEnaria;
	private int selectedWristCrossbowBolt = 0;
	private SpellManager spellManager = new SpellManager();
	private static final String HAS_STARTED_AOTD = "playerStartedAOTD";
	private final static String PLAYER_INSANITY = "PlayerInsanity";
	private final static String INVENTORY_SAVER = "inventorySaver";
	private final static String PLAYER_LOCATION_OVERWORLD = "playerLocationOverworld";
	private final static String PLAYER_LOCATION_NIGHTMARE = "playerLocationNightmare";
	private final static String PLAYER_LOCATION_VOID_CHEST = "playerLocationVoidChest";
	private final static String RESEARCH_DATA = "unlockedResearches";
	private final static String HAS_BEATEN_ENARIA = "hasBeatenEnaria";
	private final static String SELECTED_WRIST_CROSSBOW_BOLT = "selectedWristCrossbowBolt";
	private final static String SPELL_MANAGER = "spellManager";

	// CONSTRUCTOR, GETTER, REGISTER ==========================================

	public AOTDPlayerData(EntityPlayer entityPlayer)
	{
		this.entityPlayer = entityPlayer;
	}

	private static String getIdentifier()
	{
		return "AOTDPlayerData";
	}

	public static AOTDPlayerData get(EntityPlayer entityPlayer)
	{
		return (AOTDPlayerData) entityPlayer.getExtendedProperties(AOTDPlayerData.getIdentifier());
	}

	public static void register(EntityPlayer entityPlayer)
	{
		if (entityPlayer.getExtendedProperties(AOTDPlayerData.getIdentifier()) == null)
		{
			entityPlayer.registerExtendedProperties(AOTDPlayerData.getIdentifier(), new AOTDPlayerData(entityPlayer));
		}
	}

	// LOAD, SAVE =============================================================

	@Override
	public void saveNBTData(NBTTagCompound nbt)
	{
		nbt.setBoolean(HAS_STARTED_AOTD, this.getHasStartedAOTD());
		nbt.setDouble(PLAYER_INSANITY, this.getPlayerInsanity());
		nbt.setTag(INVENTORY_SAVER, inventoryList);
		nbt.setIntArray(PLAYER_LOCATION_OVERWORLD, playerLocationOverworld);
		nbt.setInteger(PLAYER_LOCATION_NIGHTMARE, playerLocationNightmare);
		nbt.setTag(RESEARCH_DATA, researches);
		nbt.setInteger(PLAYER_LOCATION_VOID_CHEST, this.playerLocationVoidChest);
		nbt.setBoolean(HAS_BEATEN_ENARIA, this.hasBeatenEnaria);
		nbt.setInteger(SELECTED_WRIST_CROSSBOW_BOLT, this.selectedWristCrossbowBolt);
		NBTObjectWriter.writeObjectToNBT(SPELL_MANAGER, this.spellManager, nbt);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt)
	{
		this.setHasStartedAOTD(nbt.getBoolean(HAS_STARTED_AOTD));
		this.setPlayerInsanity(nbt.getDouble(PLAYER_INSANITY));
		this.setPlayerInventory(nbt.getTagList(INVENTORY_SAVER, 10));
		this.setPlayerLocationOverworld(nbt.getIntArray(PLAYER_LOCATION_OVERWORLD));
		this.setPlayerLocationNightmare(nbt.getInteger(PLAYER_LOCATION_NIGHTMARE));
		this.setReseraches((NBTTagCompound) nbt.getTag(RESEARCH_DATA));
		this.setPlayerLocationVoidChest(nbt.getInteger(PLAYER_LOCATION_VOID_CHEST));
		this.setHasBeatenEnaria(nbt.getBoolean(HAS_BEATEN_ENARIA));
		this.setSelectedWristCrossbowBolt(nbt.getInteger(SELECTED_WRIST_CROSSBOW_BOLT));
		this.spellManager = (SpellManager) NBTObjectWriter.readObjectFromNBT(SPELL_MANAGER, nbt);
	}

	@Override
	public void init(Entity entity, World world)
	{
	}

	public boolean isServerSide()
	{
		return this.entityPlayer instanceof EntityPlayerMP;
	}

	// GETTER, SETTER, SYNCER =================================================

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
			AfraidOfTheDark.getPacketHandler().sendTo(new UpdateAOTDStatus(this.getHasStartedAOTD()), (EntityPlayerMP) entityPlayer);
		}
		else
		{
			AfraidOfTheDark.getPacketHandler().sendToServer(new UpdateAOTDStatus(this.getHasStartedAOTD()));
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
			AfraidOfTheDark.getPacketHandler().sendTo(new UpdateInsanity(this.getPlayerInsanity()), (EntityPlayerMP) entityPlayer);
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

	public int[] getPlayerLocationOverworld()
	{
		return this.playerLocationOverworld;
	}

	public void setPlayerLocationOverworld(int[] location)
	{
		this.playerLocationOverworld = location;
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
		return AOTDPlayerData.get(entityPlayer).getHasStartedAOTD() && !this.isResearched(research) && (research.getPrevious() != null ? this.isResearched(research.getPrevious()) : true);
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
			AfraidOfTheDark.getPacketHandler().sendTo(new UpdateResearch(this.researches), (EntityPlayerMP) entityPlayer);
		}
		else
		{
			AfraidOfTheDark.getPacketHandler().sendToServer(new UpdateResearch(this.researches));
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
			AfraidOfTheDark.getPacketHandler().sendTo(new UpdateHasBeatenEnaria(this.hasBeatenEnaria), (EntityPlayerMP) this.entityPlayer);
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
			AfraidOfTheDark.getPacketHandler().sendToServer(new SyncSelectedWristCrossbowBolt(this.selectedWristCrossbowBolt));
		}
	}

	public SpellManager getSpellManager()
	{
		return this.spellManager;
	}

	public void syncSpellManager()
	{

	}

	public void syncAll()
	{
		if (this.isServerSide())
		{
			AfraidOfTheDark.getPacketHandler().sendTo(new SyncAOTDPlayerData(this), (EntityPlayerMP) this.entityPlayer);
		}
	}

	public void requestSyncAll()
	{
		if (!this.isServerSide())
		{
			AfraidOfTheDark.getPacketHandler().sendToServer(new SyncAOTDPlayerData());
		}
	}
}
