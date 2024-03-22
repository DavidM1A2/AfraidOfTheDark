package com.davidm1a2.afraidofthedark.common.item.eldritchmetal

import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.world.item.ItemStack

interface IEldritchItem {
    fun setLastKillTime(itemStack: ItemStack, lastKillTime: Long) {
        NBTHelper.setLong(itemStack, LAST_KILL_TIME_NBT, lastKillTime)
    }

    fun getLastKillTime(itemStack: ItemStack): Long {
        if (!NBTHelper.hasTag(itemStack, LAST_KILL_TIME_NBT)) {
            setLastKillTime(itemStack, System.currentTimeMillis())
        }
        return NBTHelper.getLong(itemStack, LAST_KILL_TIME_NBT)!!
    }

    companion object {
        private const val LAST_KILL_TIME_NBT = "last_kill_time"
    }
}