package com.davidm1a2.afraidofthedark.common.item;

import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Class representing an igneous gem used to craft igneous armor and weapons
 */
public class ItemIgneousGem extends AOTDItem
{
    /**
     * Constructor initializes the item name
     */
    public ItemIgneousGem()
    {
        super("igneous_gem");
    }

    /**
     * Lights the entity on fire when held for 3 seconds
     *
     * @param stack The itemstack being held
     * @param worldIn The world the item is in
     * @param entityIn The entity to light on fire for 3 seconds
     * @param itemSlot The slot the item is in
     * @param isSelected True if the item is selected, false otherwise
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        entityIn.setFire(3);
    }
}
