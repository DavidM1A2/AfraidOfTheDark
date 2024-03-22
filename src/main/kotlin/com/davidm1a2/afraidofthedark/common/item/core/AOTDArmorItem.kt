package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ArmorItem
import net.minecraft.item.IArmorMaterial
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
abstract class AOTDArmorItem(
    baseName: String,
    material: IArmorMaterial,
    equipmentSlot: EquipmentSlotType,
    properties: Properties,
    displayInCreative: Boolean = true
) : ArmorItem(material, equipmentSlot, properties.apply {
    if (displayInCreative) {
        tab(Constants.AOTD_CREATIVE_TAB)
    }
}) {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }

    /**
     * Processes damage taken when wearing armor. Note that this applies AFTER standard armor protection
     *
     * @param entity The entity being hit
     * @param armorStack The armor itemstack
     * @param source The source of the damage
     * @param slot The slot the armor piece is in
     * @return The multiplier the damage should be multiplied against. Eg:
     *  -> Returning 0.5 will half the damage taken.
     *  -> Returning 1.0 will leave the damage unmodified
     *  -> Returning 1.5 will increase the damage by 50%
     */
    abstract fun getDamageMultiplier(entity: LivingEntity, armorStack: ItemStack, source: DamageSource, slot: EquipmentSlotType): Double

    /**
     * Tests if the player is wearing a full set of this armor type
     *
     * @param entityPlayer The player to test
     * @return True if the player is wearing all armor of this type, false otherwise
     */
    protected fun isWearingFullArmor(entityPlayer: Player): Boolean {
        val armorInventory = entityPlayer.inventory.armor
        val armorClass = this.javaClass
        return armorClass.isInstance(armorInventory[0].item) &&
                armorClass.isInstance(armorInventory[1].item) &&
                armorClass.isInstance(armorInventory[2].item) &&
                armorClass.isInstance(armorInventory[3].item)
    }

    companion object {
        // Damage sources that relate to unblockable damage
        @JvmStatic
        protected val TRUE_DAMAGE_SOURCES = setOf(
            DamageSource.DROWN,
            DamageSource.IN_WALL,
            DamageSource.OUT_OF_WORLD,
            DamageSource.STARVE
        )
    }
}