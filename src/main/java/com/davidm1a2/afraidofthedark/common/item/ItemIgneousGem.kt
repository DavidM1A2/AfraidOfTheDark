package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

/**
 * Class representing an igneous gem used to craft igneous armor and weapons
 *
 * @constructor initializes the item name
 */
class ItemIgneousGem : AOTDItem("igneous_gem")
{
    /**
     * Lights the entity on fire when held for 3 seconds
     *
     * @param stack The itemstack being held
     * @param world The world the item is in
     * @param entity The entity to light on fire for 3 seconds
     * @param itemSlot The slot the item is in
     * @param isSelected True if the item is selected, false otherwise
     */
    override fun onUpdate(stack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean)
    {
        entity.setFire(3)
    }
}