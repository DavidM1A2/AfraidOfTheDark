package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDImageDispMode
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiButton
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage
import com.davidm1a2.afraidofthedark.common.registry.meteor.MeteorEntry
import net.minecraft.util.ResourceLocation
import java.awt.Color
import kotlin.random.Random

/**
 * Special UI control that represents a meteor button which is a meteor in the sky
 *
 * @constructor Initializes fields and what meteor it represents
 * @param x          The x coordinate of this button
 * @param y          The y coordinate of this button
 * @param width      The width of this button
 * @param height     The height of this button
 * @param meteorType The type of meteor to represent
 */
class AOTDGuiMeteorButton(width: Int, height: Int, val meteorType: MeteorEntry) :
    AOTDGuiButton(width, height, icon = AOTDGuiImage(meteorType.icon.toString())) {
    init {
        // Give the meteor a random opacity
        this.color = Color(255, 255, 255, Random.nextInt(256))
    }
}
