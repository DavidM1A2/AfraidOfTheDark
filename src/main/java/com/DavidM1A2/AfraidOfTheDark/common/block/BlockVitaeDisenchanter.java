package com.DavidM1A2.AfraidOfTheDark.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Vitae;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.google.common.collect.Maps;

public class BlockVitaeDisenchanter extends AOTDBlock
{
	public BlockVitaeDisenchanter()
	{
		super(Material.rock);
		this.setUnlocalizedName("vitaeDisenchanter");
		this.setHardness(10.0F);
		this.setResistance(50.0F);
	}

	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			if (entityPlayer.inventory.getCurrentItem() == null)
			{
				// Change mode
			}
			else
			{
				if (readyToDisenchant(entityPlayer))
				{
					NBTTagList enchantments = entityPlayer.getHeldItem().getEnchantmentTagList();
					for (int i = 0; i < enchantments.tagCount(); i++)
					{
						if (enchantments.get(i) instanceof NBTTagCompound)
						{
							NBTTagCompound enchantment = (NBTTagCompound) enchantments.get(i);

							for (int j = 0; j < entityPlayer.inventory.mainInventory.length; j++)
							{
								ItemStack book = entityPlayer.inventory.mainInventory[j];
								if (book != null && book.getItem() instanceof ItemBook)
								{
									if (book.stackSize == 1)
									{
										entityPlayer.inventory.setInventorySlotContents(j, null);
									}
									else
									{
										book.stackSize = book.stackSize - 1;
									}
									break;
								}
							}

							ItemStack newBook = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.func_180306_c(enchantment.getInteger("id")), enchantment.getInteger("lvl")));

							if (entityPlayer.inventory.getFirstEmptyStack() < 0)
							{
								EntityItem entity = new EntityItem(world, entityPlayer.posX, entityPlayer.posY + 1, entityPlayer.posZ, newBook);
								world.spawnEntityInWorld(entity);
							}
							else
							{
								entityPlayer.inventory.addItemStackToInventory(newBook);
							}
						}
					}

					EnchantmentHelper.setEnchantments(Maps.newHashMap(), entityPlayer.getHeldItem());

					entityPlayer.inventoryContainer.detectAndSendChanges();
				}
			}
		}

		return super.onBlockActivated(world, blockPos, iBlockState, entityPlayer, side, hitX, hitY, hitZ);
	}

	private boolean readyToDisenchant(EntityPlayer entityPlayer)
	{
		if (LoadResearchData.isResearched(entityPlayer, ResearchTypes.VitaeDisenchanter))
		{
			if (entityPlayer.inventory.getCurrentItem().isItemEnchanted())
			{
				ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
				NBTTagList enchantments = itemStack.getEnchantmentTagList();
				List<ItemStack> validBooks = new ArrayList<ItemStack>();

				int numberOfBooksInInventory = 0;
				for (ItemStack books : entityPlayer.inventory.mainInventory)
				{
					if (books != null && books.getItem() instanceof ItemBook && !books.isItemEnchanted())
					{
						numberOfBooksInInventory = numberOfBooksInInventory + books.stackSize;
						validBooks.add(books);
					}
				}

				if (numberOfBooksInInventory >= enchantments.tagCount())
				{
					int vitaeCost = 0;

					List<NBTTagCompound> enchantmentList = new ArrayList<NBTTagCompound>();

					for (int i = 0; i < enchantments.tagCount(); i++)
					{
						if (enchantments.get(i) instanceof NBTTagCompound)
						{
							NBTTagCompound enchantment = (NBTTagCompound) enchantments.get(i);
							enchantmentList.add(enchantment);
							int enchantmentLevel = enchantment.getInteger("lvl");
							int id = enchantment.getInteger("id");

							vitaeCost = vitaeCost + enchantmentLevel;
						}
					}

					int vitaeMultiplier = 1;
					if (itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemSword)
					{
						ToolMaterial material = (itemStack.getItem() instanceof ItemTool) ? ((ItemTool) itemStack.getItem()).getToolMaterial() : ToolMaterial.valueOf(((ItemSword) itemStack.getItem()).getToolMaterialName());
						switch (material)
						{
							case EMERALD:
								vitaeMultiplier = 4;
								break;
							case GOLD:
								vitaeMultiplier = 5;
								break;
							case IRON:
								vitaeMultiplier = 3;
								break;
							case STONE:
								vitaeMultiplier = 1;
								break;
							case WOOD:
								vitaeMultiplier = 1;
								break;
							default:
								vitaeMultiplier = 1;
								break;
						}
					}

					vitaeCost = vitaeCost * vitaeMultiplier;

					if (Vitae.get(entityPlayer) >= vitaeCost)
					{
						Vitae.addVitae(entityPlayer, -vitaeCost, Side.SERVER);
						return true;
					}
					else
					{
						if (!entityPlayer.worldObj.isRemote)
						{
							if (vitaeCost > Constants.entityVitaeResistance.get(EntityPlayer.class))
							{
								entityPlayer.addChatMessage(new ChatComponentText("I'm not powerful enough to perform this action."));
							}
							else
							{
								entityPlayer.addChatMessage(new ChatComponentText("I don't have enough vitae to perform this action."));
							}
						}
					}
				}
				else
				{
					if (!entityPlayer.worldObj.isRemote)
					{
						entityPlayer.addChatMessage(new ChatComponentText("I don't have enough books to move the enchantments on to."));
					}
				}
			}
			else
			{
				if (!entityPlayer.worldObj.isRemote)
				{
					entityPlayer.addChatMessage(new ChatComponentText("This item is not enchanted."));
				}
			}
		}
		else
		{
			if (!entityPlayer.worldObj.isRemote)
			{
				entityPlayer.addChatMessage(new ChatComponentText("I don't understand how to use this."));
			}
		}

		return false;
	}
}
