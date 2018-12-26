/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDLootTables;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDSchematics;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemSchematicGenerator extends AOTDItem
{
	// Set the name and stack size to 1
	public ItemSchematicGenerator()
	{
		super();
		this.setUnlocalizedName("schematic_generator");
		this.setRegistryName("schematic_generator");
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand hand)
	{
		ItemStack itemStack = entityPlayer.getHeldItem(hand);
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
					entityPlayer.addChatMessage(new TextComponentString("Currently selected schematic: " + this.getCurrentSchematic(itemStack).toString()));
			}
			else
			{
				SchematicGenerator.generateSchematicWithLoot(this.getCurrentSchematic(itemStack).getSchematic(), world, entityPlayer.getPosition().getX() + 1, entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ() + 1, AOTDLootTables.Crypt.getLootTable());
			}
		else
		{
			if (!world.isRemote)
				entityPlayer.addChatMessage(new TextComponentString("You must be in creative to use this item"));
		}

		return ActionResult.<ItemStack>newResult(EnumActionResult.SUCCESS, itemStack);
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
