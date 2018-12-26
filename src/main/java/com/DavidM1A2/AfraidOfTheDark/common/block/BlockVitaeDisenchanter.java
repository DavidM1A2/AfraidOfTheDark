/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDBlock;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockVitaeDisenchanter extends AOTDBlock
{
	private static final PropertyBool VARIANT = PropertyBool.create("variant");

	public BlockVitaeDisenchanter()
	{
		super(Material.ROCK);
		this.setUnlocalizedName("vitae_disenchanter");
		this.setRegistryName("vitae_disenchanter");
		this.setHardness(10.0F);
		this.setResistance(50.0F);
		this.setHarvestLevel("pickaxe", 2);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(final int meta)
	{
		IBlockState defaultState = this.getDefaultState();
		return defaultState.withProperty(VARIANT, meta == 0 ? true : false);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(final IBlockState state)
	{
		return state.getValue(VARIANT).compareTo(true) == 0 ? 0 : 1;
	}

	// Default block states
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]
		{ BlockVitaeDisenchanter.VARIANT });
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.VitaeDisenchanter))
			{
				if (entityPlayer.inventory.getCurrentItem() == null)
				{
					world.setBlockState(blockPos, iBlockState.withProperty(VARIANT, this.getMetaFromState(iBlockState) == 0 ? false : true));
				}
				else
				{
					if (this.getMetaFromState(iBlockState) == 0)
					{
						if (readyToDisenchant(entityPlayer))
						{
							disenchantItem(entityPlayer);
						}
					}
					else
					{
						if (readyToConvertBook(entityPlayer))
						{
							convertBook(entityPlayer);
						}
					}
				}
			}
			else
			{
				entityPlayer.addChatMessage(new TextComponentString("I can't understand this block"));
			}
		}

		return true;
	}

	private void convertBook(EntityPlayer entityPlayer)
	{
		ItemStack itemstack = entityPlayer.getHeldItemMainhand();
		NBTTagList enchantments = ((ItemEnchantedBook) itemstack.getItem()).getEnchantments(itemstack);
		int numberOfXPBottlesToAdd = 0;

		for (int i = 0; i < enchantments.tagCount(); i++)
		{
			if (enchantments.get(i) instanceof NBTTagCompound)
			{
				NBTTagCompound enchantment = (NBTTagCompound) enchantments.get(i);
				numberOfXPBottlesToAdd = numberOfXPBottlesToAdd + enchantment.getInteger("lvl");
			}
		}

		numberOfXPBottlesToAdd = numberOfXPBottlesToAdd * 3;

		int bottlesToSpawn = numberOfXPBottlesToAdd / 64;
		int extraBottlesToSpawn = numberOfXPBottlesToAdd % 64;

		for (int i = 0; i < bottlesToSpawn; i++)
		{
			if (entityPlayer.inventory.getFirstEmptyStack() < 0)
			{
				EntityItem entity = new EntityItem(entityPlayer.worldObj, entityPlayer.posX, entityPlayer.posY + 1, entityPlayer.posZ, new ItemStack(Items.EXPERIENCE_BOTTLE, 64));
				entityPlayer.worldObj.spawnEntityInWorld(entity);
			}
			else
			{
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.EXPERIENCE_BOTTLE, 64));
			}
		}
		if (extraBottlesToSpawn > 0)
		{
			if (entityPlayer.inventory.getFirstEmptyStack() < 0)
			{
				EntityItem entity = new EntityItem(entityPlayer.worldObj, entityPlayer.posX, entityPlayer.posY + 1, entityPlayer.posZ, new ItemStack(Items.EXPERIENCE_BOTTLE, extraBottlesToSpawn));
				entityPlayer.worldObj.spawnEntityInWorld(entity);
			}
			else
			{
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.EXPERIENCE_BOTTLE, extraBottlesToSpawn));
			}
		}

		entityPlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOOK, 1));
		entityPlayer.inventoryContainer.detectAndSendChanges();
	}

	private boolean readyToConvertBook(EntityPlayer entityPlayer)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.VitaeDisenchanter))
		{
			ItemStack itemStack = entityPlayer.getHeldItemMainhand();
			if (itemStack.getItem() instanceof ItemEnchantedBook)
			{
				NBTTagList enchantments = ((ItemEnchantedBook) itemStack.getItem()).getEnchantments(itemStack);
				int vitaeCost = 0;

				for (int i = 0; i < enchantments.tagCount(); i++)
				{
					if (enchantments.get(i) instanceof NBTTagCompound)
					{
						NBTTagCompound enchantment = (NBTTagCompound) enchantments.get(i);
						vitaeCost = vitaeCost + enchantment.getInteger("lvl");
					}
				}

				vitaeCost = vitaeCost * 3;

				if (VitaeUtils.consumeVitaeFromLanterns(entityPlayer, vitaeCost))
				{
					return true;
				}
				else
				{
					if (!entityPlayer.worldObj.isRemote)
					{
						entityPlayer.addChatMessage(new TextComponentString("I don't have enough vitae in my lanterns to perform this action."));
					}
				}
			}
			else
			{
				if (!entityPlayer.worldObj.isRemote)
				{
					entityPlayer.addChatMessage(new TextComponentString("I'll need to right click this with an enchanted book."));
				}
			}
		}
		else

		{
			if (!entityPlayer.worldObj.isRemote)
			{
				entityPlayer.addChatMessage(new TextComponentString("I don't understand how to use this."));
			}
		}
		return false;
	}

	private void disenchantItem(EntityPlayer entityPlayer)
	{
		ItemStack itemstack = entityPlayer.getHeldItemMainhand();
		NBTTagList enchantments = itemstack.getEnchantmentTagList();
		for (int i = 0; i < enchantments.tagCount(); i++)
		{
			if (enchantments.get(i) instanceof NBTTagCompound)
			{
				NBTTagCompound enchantment = (NBTTagCompound) enchantments.get(i);

				for (int j = 0; j < entityPlayer.inventory.mainInventory.size(); j++)
				{
					ItemStack book = entityPlayer.inventory.mainInventory.get(j);
					if (book != null && book.getItem() instanceof ItemBook)
					{
						if (book.func_190916_E() == 1)
						{
							entityPlayer.inventory.setInventorySlotContents(j, null);
						}
						else
						{
							book.func_190920_e(book.func_190916_E() - 1);
						}
						break;
					}
				}

				ItemStack newBook = Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByID(enchantment.getInteger("id")), enchantment.getInteger("lvl")));

				if (entityPlayer.inventory.getFirstEmptyStack() < 0)
				{
					EntityItem entity = new EntityItem(entityPlayer.worldObj, entityPlayer.posX, entityPlayer.posY + 1, entityPlayer.posZ, newBook);
					entityPlayer.worldObj.spawnEntityInWorld(entity);
				}
				else
				{
					entityPlayer.inventory.addItemStackToInventory(newBook);
				}
			}
		}

		EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>(), entityPlayer.getHeldItemMainhand());

		entityPlayer.inventoryContainer.detectAndSendChanges();
	}

	private boolean readyToDisenchant(EntityPlayer entityPlayer)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.VitaeDisenchanter))
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
						numberOfBooksInInventory = numberOfBooksInInventory + books.func_190916_E();
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
					if (itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemArmor)
					{
						String material = (itemStack.getItem() instanceof ItemTool) ? ((ItemTool) itemStack.getItem()).getToolMaterial().toString()
								: (itemStack.getItem() instanceof ItemSword) ? ((ItemSword) itemStack.getItem()).getToolMaterialName() : ((ItemArmor) itemStack.getItem()).getArmorMaterial().toString();
						if (Constants.toolMaterialRepairCosts.containsKey(material))
						{
							vitaeMultiplier = Constants.toolMaterialRepairCosts.get(material);
						}
					}

					vitaeCost = vitaeCost * vitaeMultiplier * 2;

					if (VitaeUtils.consumeVitaeFromLanterns(entityPlayer, vitaeCost))
					{
						return true;
					}
					else
					{
						if (!entityPlayer.worldObj.isRemote)
						{
							entityPlayer.addChatMessage(new TextComponentString("I don't have enough vitae in my lanterns to perform this action."));
						}
					}
				}
				else
				{
					if (!entityPlayer.worldObj.isRemote)
					{
						entityPlayer.addChatMessage(new TextComponentString("I don't have enough books to move the enchantments on to."));
					}
				}
			}
			else
			{
				if (!entityPlayer.worldObj.isRemote)
				{
					entityPlayer.addChatMessage(new TextComponentString("This item is not enchanted."));
				}
			}
		}
		else
		{
			if (!entityPlayer.worldObj.isRemote)
			{
				entityPlayer.addChatMessage(new TextComponentString("I don't understand how to use this."));
			}
		}

		return false;
	}
}
