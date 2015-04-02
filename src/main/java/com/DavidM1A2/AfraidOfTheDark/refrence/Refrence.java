package com.DavidM1A2.AfraidOfTheDark.refrence;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.research.Research;

public class Refrence
{
	public static final String MOD_ID = "AfraidOfTheDark";
	public static final String MOD_NAME = "Afraid of the Dark";
	public static final String VERSION = "1.7.10-Alpha 0.1";
	public static final String SERVER_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.proxy.ClientProxy";
	public static final String GUI_FACTORY_CLASS = "com.DavidM1A2.AfraidOfTheDark.client.gui.GuiFactory";
	public static final int PACKET_ID_CROSSBOW = 1;
	public static final int PACKET_ID_INSANITY_UPDATE = 2;
	public static final int PACKET_ID_HAS_STARTED_AOTD_UPDATE_CLIENT = 3;
	public static final int PACKET_ID_HAS_STARTED_AOTD_UPDATE_SERVER = 4;
	public static final String NETWORK_CHANNEL_NAME = Refrence.MOD_ID;

	public static Research myResearch = new Research();

	public static final DamageSource silverWeapon = new DamageSource("silverWeapon").setProjectile();
	public static final ToolMaterial silver = EnumHelper.addToolMaterial("silverTool", 2, 250, 1, 4F, 22);

	// The creative tab
	public static final CreativeTabs AFRAID_OF_THE_DARK = new CreativeTabs(Refrence.MOD_ID.toLowerCase())
	{
		@Override
		public Item getTabIconItem()
		{
			return ModItems.journal;
		}
	};
}
