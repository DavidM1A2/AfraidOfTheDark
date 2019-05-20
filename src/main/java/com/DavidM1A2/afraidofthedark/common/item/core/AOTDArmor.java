package com.DavidM1A2.afraidofthedark.common.item.core;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Base class for AOTD armor
 */
public abstract class AOTDArmor extends ItemArmor
{
    // The percent of damage blocked by the armor
    protected double percentOfDamageBlocked;
    // The maximum damage blocked by the armor
    protected int maxDamageBlocked;

    /**
     * Constructor sets up armor item properties
     *
     * @param baseName        The name of the item to be used by the game registry
     * @param materialIn      The material used for the armor
     * @param renderIndexIn   The type of model to use when rendering the armor, 0 is cloth, 1 is chain, 2 is
     *                        iron, 3 is diamond and 4 is gold.
     * @param equipmentSlotIn The slot that this armor pieces goes on, can be one of 4 options
     */
    public AOTDArmor(String baseName, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
    {
        super(materialIn, renderIndexIn, equipmentSlotIn);
        this.setUnlocalizedName(Constants.MOD_ID + ":" + baseName);
        this.setRegistryName(Constants.MOD_ID + ":" + baseName);
        if (this.displayInCreative())
        {
            this.setCreativeTab(Constants.AOTD_CREATIVE_TAB);
        }
    }

    /**
     * @return True if this item should be displayed in creative mode, false otherwise
     */
    protected boolean displayInCreative()
    {
        return true;
    }


    /**
     * Tests if the player is wearing a full set of this armor type
     *
     * @param entityPlayer The player to test
     * @return True if the player is wearing all armor of this type, false otherwise
     */
    protected boolean isWearingFullArmor(final EntityPlayer entityPlayer)
    {
        NonNullList<ItemStack> armorInventory = entityPlayer.inventory.armorInventory;
        Class<? extends AOTDArmor> armorClass = this.getClass();
        return armorClass.isInstance(armorInventory.get(0).getItem()) &&
                armorClass.isInstance(armorInventory.get(1).getItem()) &&
                armorClass.isInstance(armorInventory.get(2).getItem()) &&
                armorClass.isInstance(armorInventory.get(3).getItem());
    }
}
