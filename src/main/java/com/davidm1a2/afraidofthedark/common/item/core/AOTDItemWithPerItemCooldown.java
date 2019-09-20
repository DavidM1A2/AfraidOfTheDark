package com.davidm1a2.afraidofthedark.common.item.core;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.SyncItemWithCooldown;
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

/**
 * Class representing an item that has a cooldown
 */
public abstract class AOTDItemWithPerItemCooldown extends AOTDItem
{
    public static final String NBT_LAST_USE_TIME = "last_use_time";

    // The time different between server and client clocks which is used to syncronize the cooldown bar. This can be the same for all items with cooldowns
    // Since the server-client time difference is not item dependent
    private Long serverClientTimeDifference = 0L;

    /**
     * Constructor sets up item properties
     *
     * @param baseName The name of the item to be used by the game registry
     */
    public AOTDItemWithPerItemCooldown(String baseName)
    {
        super(baseName);
        // Cooldown items can't stack!
        this.setMaxStackSize(1);
    }

    /**
     * Updates the amount of milliseconds difference between the server and client clocks. This is useful to make the progress bar of the item
     * look correct and not be off if the server has a different time
     *
     * @param difference The new time difference
     */
    public void updateServerClientDifference(long difference)
    {
        this.serverClientTimeDifference = difference;
    }

    /**
     * Sets the item on cooldown
     *
     * @param itemStack    The item to set on cooldown
     * @param entityPlayer The player that is holding the item
     */
    public void setOnCooldown(ItemStack itemStack, EntityPlayer entityPlayer)
    {
        NBTHelper.setLong(itemStack, NBT_LAST_USE_TIME, System.currentTimeMillis());
        // We need to update the client of the new cooldown, so send a packet
        if (!entityPlayer.world.isRemote)
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendTo(new SyncItemWithCooldown(System.currentTimeMillis(), this), (EntityPlayerMP) entityPlayer);
        }
    }

    /**
     * Tests if an item is on cooldown
     *
     * @param itemStack The itemstack to test the cooldown of
     * @return True if the item is on cooldown, false otherwise
     */
    public boolean isOnCooldown(ItemStack itemStack)
    {
        long millisecondsElapsed = System.currentTimeMillis() - this.getLastUseTime(itemStack);
        return millisecondsElapsed < this.getItemCooldownInMilliseconds(itemStack);
    }

    /**
     * Returns the cooldown remaining in seconds
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The remaining cooldown in seconds
     */
    public int cooldownRemainingInSeconds(ItemStack itemStack)
    {
        long millisecondsElapsed = System.currentTimeMillis() - this.getLastUseTime(itemStack);
        return MathHelper.ceil((this.getItemCooldownInMilliseconds(itemStack) - millisecondsElapsed) / 1000.0);
    }

    /**
     * The durability to show for this item is based on how much cooldown is left
     *
     * @param stack The itemstack to show durability for
     * @return Value between 0 to 1 of what percent of the durability bar to show
     */
    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        long millisecondsElapsed = System.currentTimeMillis() - this.serverClientTimeDifference - this.getLastUseTime(stack);
        return Math.max(0, 1 - (double) millisecondsElapsed / (double) this.getItemCooldownInMilliseconds(stack));
    }

    /**
     * Returns the last time in milliseconds that the item was used
     *
     * @param itemStack The item to test
     * @return The last time in milliseconds the cooldown was activated
     */
    private long getLastUseTime(ItemStack itemStack)
    {
        if (!NBTHelper.hasTag(itemStack, NBT_LAST_USE_TIME))
        {
            NBTHelper.setLong(itemStack, NBT_LAST_USE_TIME, 0L);
        }
        return NBTHelper.getLong(itemStack, NBT_LAST_USE_TIME);
    }

    /**
     * The durability bar represents the CD remaining
     *
     * @param itemStack The stack to show current CD for
     * @return True to show the CD bar
     */
    @Override
    public boolean showDurabilityBar(ItemStack itemStack)
    {
        return true;
    }

    /**
     * Returns the number of milliseconds required for this item to get off cooldown
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The number of milliseconds required to finish the cooldown
     */
    public abstract int getItemCooldownInMilliseconds(ItemStack itemStack);
}
