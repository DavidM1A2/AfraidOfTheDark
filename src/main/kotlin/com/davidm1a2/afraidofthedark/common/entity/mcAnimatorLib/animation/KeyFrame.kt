package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation

import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.Vector3f

/**
 * Class was provided by the MC animator library
 *
 * @property modelRotations A map of part name to Quat4f rotation
 * @property modelTranslations A map of part name to position
 */
class KeyFrame {
    internal var modelRotations: MutableMap<String, Quaternion> = mutableMapOf()
    internal var modelTranslations: MutableMap<String, Vector3f> = mutableMapOf()

    /**
     * Tests if this key frame has data for rotation for a given box
     *
     * @param boxName The box to test
     */
    fun useBoxInRotation(boxName: String): Boolean {
        return modelRotations[boxName] != null
    }

    /**
     * Tests if this key frame has data for translation for a given box
     *
     * @param boxName The box to test
     */
    fun useBoxInTranslation(boxName: String): Boolean {
        return modelTranslations[boxName] != null
    }
}