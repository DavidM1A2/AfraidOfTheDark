package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemArmor

/**
 * Base class for AOTD armor
 *
 * @constructor sets up armor item properties
 * @param baseName        The name of the item to be used by the game registry
 * @param material      The material used for the armor
 * @param renderIndex   The type of model to use when rendering the armor, 0 is cloth, 1 is chain, 2 is iron, 3 is diamond and 4 is gold.
 * @param equipmentSlot The slot that this armor pieces goes on, can be one of 4 options
 * @param displayInCreative True if this item should be displayed in creative mode, false otherwise
 * @property percentOfDamageBlocked The percent of damage blocked by the armor
 * @property maxDamageBlocked The maximum damage blocked by the armor
 */
abstract class AOTDItemArmor(
    baseName: String,
    material: ArmorMaterial,
    renderIndex: Int,
    equipmentSlot: EntityEquipmentSlot,
    displayInCreative: Boolean = true
) :
    ItemArmor(material, renderIndex, equipmentSlot) {
    protected var percentOfDamageBlocked = 0.0
    protected var maxDamageBlocked = 0

    init {
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")

        if (displayInCreative) {
            this.creativeTab = Constants.AOTD_CREATIVE_TAB
        }
    }

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