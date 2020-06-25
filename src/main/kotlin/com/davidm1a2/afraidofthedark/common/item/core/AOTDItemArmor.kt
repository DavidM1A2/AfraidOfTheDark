package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.IArmorMaterial
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource

/**
 * Base class for AOTD armor
 *
 * @constructor sets up armor item properties
 * @param baseName        The name of the item to be used by the game registry
 * @param material      The material used for the armor
 * @param equipmentSlot The slot that this armor pieces goes on, can be one of 4 options
 * @param displayInCreative True if this item should be displayed in creative mode, false otherwise
 */
abstract class AOTDItemArmor(
    baseName: String,
    material: IArmorMaterial,
    equipmentSlot: EntityEquipmentSlot,
    properties: Properties,
    displayInCreative: Boolean = true
) : ItemArmor(material, equipmentSlot, properties.apply {
    if (displayInCreative) {
        group(Constants.AOTD_CREATIVE_TAB)
    }
}) {
    init {
        this.setRegistryName("${Constants.MOD_ID}:$baseName")
    }

    /**
     * Processes damage taken when wearing armor
     *
     * @param entity The entity being hit
     * @param armorStack The armor itemstack
     * @param source The source of the damage
     * @param amount The amount of damage
     * @param slot The slot the armor piece is in
     * @return The percent of damage this specific armor piece blocks, should be between 0-10
     */
    abstract fun processDamage(entity: EntityLivingBase, armorStack: ItemStack, source: DamageSource, amount: Float, slot: EntityEquipmentSlot): Double

    /**
     * Tests if the player is wearing a full set of this armor type
     *
     * @param entityPlayer The player to test
     * @return True if the player is wearing all armor of this type, false otherwise
     */
    protected fun isWearingFullArmor(entityPlayer: EntityPlayer): Boolean {
        val armorInventory = entityPlayer.inventory.armorInventory
        val armorClass = this.javaClass
        return armorClass.isInstance(armorInventory[0].item) &&
                armorClass.isInstance(armorInventory[1].item) &&
                armorClass.isInstance(armorInventory[2].item) &&
                armorClass.isInstance(armorInventory[3].item)
    }
}