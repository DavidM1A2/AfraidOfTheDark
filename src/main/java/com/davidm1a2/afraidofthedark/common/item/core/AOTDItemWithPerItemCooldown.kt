package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.SyncItemWithCooldown
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import kotlin.math.ceil
import kotlin.math.max

/**
 * Class representing an item that has a cooldown
 *
 * @constructor sets up item properties
 * @param baseName The name of the item to be used by the game registry
 * @property serverClientTimeDifference The time different between server and client clocks which is used to synchronize the cooldown bar. This can be the same for all items with cooldowns.
 *                                      Since the server-client time difference is not item dependent
 */
abstract class AOTDItemWithPerItemCooldown(baseName: String, displayInCreative: Boolean = true) :
    AOTDItem(baseName, displayInCreative) {
    private var serverClientTimeDifference = 0L

    init {
        // Cooldown items can't stack!
        setMaxStackSize(1)
    }

    /**
     * Updates the amount of milliseconds difference between the server and client clocks. This is useful to make the progress bar of the item
     * look correct and not be off if the server has a different time
     *
     * @param difference The new time difference
     */
    fun updateServerClientDifference(difference: Long) {
        serverClientTimeDifference = difference
    }

    /**
     * Sets the item on cooldown
     *
     * @param itemStack    The item to set on cooldown
     * @param entityPlayer The player that is holding the item
     */
    open fun setOnCooldown(itemStack: ItemStack, entityPlayer: EntityPlayer) {
        NBTHelper.setLong(itemStack, NBT_LAST_USE_TIME, System.currentTimeMillis())

        // We need to update the client of the new cooldown, so send a packet
        if (!entityPlayer.world.isRemote) {
            AfraidOfTheDark.INSTANCE.packetHandler.sendTo(
                SyncItemWithCooldown(System.currentTimeMillis(), this),
                entityPlayer as EntityPlayerMP
            )
        }
    }

    /**
     * Tests if an item is on cooldown
     *
     * @param itemStack The itemstack to test the cooldown of
     * @return True if the item is on cooldown, false otherwise
     */
    fun isOnCooldown(itemStack: ItemStack): Boolean {
        val millisecondsElapsed = System.currentTimeMillis() - getLastUseTime(itemStack)
        return millisecondsElapsed < getCooldownInMilliseconds(itemStack)
    }

    /**
     * Returns the cooldown remaining in seconds
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The remaining cooldown in seconds
     */
    fun cooldownRemainingInSeconds(itemStack: ItemStack): Int {
        val millisecondsElapsed = System.currentTimeMillis() - getLastUseTime(itemStack)
        return ceil((getCooldownInMilliseconds(itemStack) - millisecondsElapsed) / 1000.0).toInt()
    }

    /**
     * The durability to show for this item is based on how much cooldown is left
     *
     * @param stack The itemstack to show durability for
     * @return Value between 0 to 1 of what percent of the durability bar to show
     */
    override fun getDurabilityForDisplay(stack: ItemStack): Double {
        val millisecondsElapsed = System.currentTimeMillis() - serverClientTimeDifference - getLastUseTime(stack)
        return max(0.0, 1 - millisecondsElapsed.toDouble() / getCooldownInMilliseconds(stack).toDouble())
    }

    /**
     * Returns the last time in milliseconds that the item was used
     *
     * @param itemStack The item to test
     * @return The last time in milliseconds the cooldown was activated
     */
    private fun getLastUseTime(itemStack: ItemStack): Long {
        if (!NBTHelper.hasTag(itemStack, NBT_LAST_USE_TIME)) {
            NBTHelper.setLong(itemStack, NBT_LAST_USE_TIME, 0L)
        }
        return NBTHelper.getLong(itemStack, NBT_LAST_USE_TIME)!!
    }

    /**
     * The durability bar represents the CD remaining
     *
     * @param itemStack The stack to show current CD for
     * @return True to show the CD bar
     */
    override fun showDurabilityBar(itemStack: ItemStack): Boolean {
        return true
    }

    /**
     * Returns the number of milliseconds required for this item to get off cooldown
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The number of milliseconds required to finish the cooldown
     */
    abstract fun getCooldownInMilliseconds(itemStack: ItemStack): Int

    companion object {
        private const val NBT_LAST_USE_TIME = "last_use_time"
    }
}