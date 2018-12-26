/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDSchematics;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemSchematicGenerator extends AOTDItem
{
	// Set the name and stack size to 1
	public ItemSchematicGenerator()
	{
		super();
		this.setUnlocalizedName("schematicGenerator");
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (entityPlayer.capabilities.isCreativeMode)
			if (entityPlayer.isSneaking())
			{
				int currentOrdinal = this.getCurrentSchematic(itemStack).ordinal();
				if (entityPlayer.onGround)
				{
					if (currentOrdinal == AOTDSchematics.values().length - 1)
						currentOrdinal = 0;
					else
						currentOrdinal++;
				}
				else
				{
					if (currentOrdinal == 0)
						currentOrdinal = AOTDSchematics.values().length - 1;
					else
						currentOrdinal--;
				}
				this.setCurrentSchematic(itemStack, AOTDSchematics.values()[currentOrdinal]);
				if (!world.isRemote)
					entityPlayer.addChatMessage(new ChatComponentText("Currently selected schematic: " + this.getCurrentSchematic(itemStack).toString()));
			}
			else
			{
				SchematicGenerator.generateSchematic(this.getCurrentSchematic(itemStack).getSchematic(), world, entityPlayer.getPosition().getX() + 1, entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ() + 1);
			}
		else
		{
			if (!world.isRemote)
				entityPlayer.addChatMessage(new ChatComponentText("You must be in creative to use this item"));
		}

		return super.onItemRightClick(itemStack, world, entityPlayer);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> tooltip, boolean advanced)
	{
		tooltip.add("Shift & Right Click to change the generated dungeon");
		tooltip.add("If airborne, schematics cycle forward, otherwise they cycle backwards");
		tooltip.add("Right click to generate the dungeon at the");
		tooltip.add("playersX + 1, playersY, playersZ + 1 into the positive X and Z direction");
		tooltip.add("Currently selected: " + this.getCurrentSchematic(itemStack).toString());
	}

	public AOTDSchematics getCurrentSchematic(ItemStack itemStack)
	{
		return AOTDSchematics.values()[NBTHelper.getInt(itemStack, "currentSchematic")];
	}

	public void setCurrentSchematic(ItemStack itemStack, AOTDSchematics schematic)
	{
		NBTHelper.setInteger(itemStack, "currentSchematic", schematic.ordinal());
	}
}
