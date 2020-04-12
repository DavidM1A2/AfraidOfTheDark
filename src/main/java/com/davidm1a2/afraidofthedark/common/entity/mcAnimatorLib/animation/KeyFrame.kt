package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f

/**
 * Class was provided by the MC animator library
 *
 * @property modelRenderersRotations A map of part name to quaternion rotation
 * @property modelRenderersTranslations A map of part name to position
 */
class KeyFrame {
    internal var modelRenderersRotations: MutableMap<String, Quaternion> = mutableMapOf()
    internal var modelRenderersTranslations: MutableMap<String, Vector3f> = mutableMapOf()

    /**
     * Tests if this key frame has data for rotation for a given box
     *
     * @param boxName The box to test
     */
    fun useBoxInRotation(boxName: String): Boolean {
        return modelRenderersRotations[boxName] != null
    }

    /**
     * Tests if this key frame has data for translation for a given box
     *
     * @param boxName The box to test
     */
    fun useBoxInTranslation(boxName: String): Boolean {
        return modelRenderersTranslations[boxName] != null
    }
}