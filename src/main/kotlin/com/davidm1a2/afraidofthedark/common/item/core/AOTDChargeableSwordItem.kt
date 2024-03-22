package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level
import kotlin.math.roundToInt

/**
 * Base class for swords that don't have durability but charge instead
 *
 * @constructor takes a tool material and name of the item in the constructor
 * @param baseName     The name of the sword
 * @param toolMaterial The tool material to be used for the sword
 * @param displayInCreative True if this item should be displayed in creative mode, false otherwise
 * @property percentChargePerAttack The percent the sword will charge per entity hit, defaults to 5%
 */
abstract class AOTDChargeableSwordItem(
    baseName: String,
    toolMaterial: Tier,
    damageAmplifier: Int,
    attackSpeed: Float,
    properties: Properties,
    displayInCreative: Boolean = true
) : AOTDSwordItem(baseName, toolMaterial, damageAmplifier, attackSpeed, properties, displayInCreative), IHasModelProperties {
    protected var percentChargePerAttack = 5.0

    override fun getProperties(): List<Pair<ResourceLocation, ClampedItemPropertyFunction>> {
        return listOf(
            // Emit a charged = 1 property when charged, 0 otherwise
            ResourceLocation(Constants.MOD_ID, "charged") to ClampedItemPropertyFunction { stack, _, _, _ ->
                if (isFullyCharged(stack)) 1f else 0f
            }
        )
    }

    override fun onLeftClickEntity(stack: ItemStack, player: Player, target: Entity): Boolean {
        // Only charge on hitting entity living entities not armor stands
        if (target is Player || target is Mob) {
            addCharge(stack, percentChargePerAttack)
        }
        return super.onLeftClickEntity(stack, player, target)
    }

    /**
     * Gets the durability to display for the itemstack which will be the inverse of charge (since charge goes from 0 -> 100
     * and durability goes from 1 -> 0
     *
     * @param stack The itemstack to get durability for
     * @return 0 for 100% charged or 1 for 0% charged
     */
    override fun getBarWidth(stack: ItemStack): Int {
        return (13*(1.0 - getCharge(stack) / 100.0)).roundToInt()
    }

    /**
     * Chargable swords always show the charge bar
     *
     * @param stack The itemstack to show charge bar for
     * @return True to show 'durability'
     */
    override fun isBarVisible(stack: ItemStack): Boolean {
        return true
    }

    /**
     * Right click fires the charge attack if it is fully charged
     *
     * @param worldIn  The world the item was right clicked in
     * @param playerIn The player that right clicked with the sword
     * @param handIn   The hand the right click was triggered from
     * @return Success if the sword fired its ability, pass otherwise
     */
    override fun use(worldIn: Level, playerIn: Player, handIn: InteractionHand): InteractionResultHolder<ItemStack> {
        val swordStack = playerIn.getItemInHand(handIn)
        // Server side processing only
        if (!worldIn.isClientSide) {
            // Test if the sword is charged
            if (isFullyCharged(swordStack)) {
                // Perform the attack, if it succeeds clear the charge
                if (performChargeAttack(swordStack, worldIn, playerIn)) {
                    // Reset the sword charge
                    addCharge(swordStack, -100.0)
                }
            } else {
                playerIn.sendMessage(TranslatableComponent("message.afraidofthedark.chargable_sword.not_enough_energy"))
            }
        }

        // Fail to avoid the move animation on item use
        return InteractionResultHolder.fail(swordStack)
    }

    /**
     * Performs the attack once the sword is charged
     *
     * @param itemStack    The itemstack that was charged
     * @param world        The world the charge attack happened in
     * @param entityPlayer The player who used the charge attack
     * @return True if the charge attack went off, false otherwise
     */
    abstract fun performChargeAttack(itemStack: ItemStack, world: Level, entityPlayer: Player): Boolean

    /**
     * Returns the amount of charge a given sword has
     *
     * @param itemStack The itemstack to test
     * @return The charge from 0% to 100%
     */
    fun getCharge(itemStack: ItemStack): Double {
        ensureChargeInit(itemStack)
        return NBTHelper.getDouble(itemStack, NBT_CHARGE)!!
    }

    /**
     * Adds charge to the sword
     *
     * @param itemStack The itemstack to add charge to
     * @param charge    The charge to add (or subtract) from 0.0 to 100.0
     */
    fun addCharge(itemStack: ItemStack, charge: Double) {
        ensureChargeInit(itemStack)
        val newCharge = (NBTHelper.getDouble(itemStack, NBT_CHARGE)!! + charge).coerceIn(0.0, 100.0)
        NBTHelper.setDouble(itemStack, NBT_CHARGE, newCharge)
    }

    /**
     * True if the sword was fully charged, false otherwise
     *
     * @param itemStack The itemstack to test charge of
     * @return True if the sword is fully charged, false if not
     */
    fun isFullyCharged(itemStack: ItemStack): Boolean {
        ensureChargeInit(itemStack)
        return NBTHelper.getDouble(itemStack, NBT_CHARGE) == 100.0
    }

    /**
     * Ensures an itemstack has the charge tag at default value of 0
     *
     * @param itemStack The itemstack to test
     */
    private fun ensureChargeInit(itemStack: ItemStack) {
        if (!NBTHelper.hasTag(itemStack, NBT_CHARGE)) {
            NBTHelper.setDouble(itemStack, NBT_CHARGE, 0.0)
        }
    }

    override fun canBeDepleted(): Boolean {
        return false
    }

    override fun isEnchantable(itemStack: ItemStack): Boolean {
        return true
    }

    companion object {
        // Constant for storing charge on the item NBT
        private const val NBT_CHARGE = "charge"
    }
}