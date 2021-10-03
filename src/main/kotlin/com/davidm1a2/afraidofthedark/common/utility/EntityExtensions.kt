package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.entity.Entity
import net.minecraft.util.Util
import net.minecraft.util.text.ITextComponent

fun Entity.sendMessage(textComponent: ITextComponent) {
    sendMessage(textComponent, Util.NIL_UUID)
}