package com.davidm1a2.afraidofthedark.common.config

import com.davidm1a2.afraidofthedark.common.constants.ModClientConfiguration
import net.minecraftforge.common.ForgeConfigSpec

/**
 * AfraidOfTheDark client config
 *
 * @param builder The builder to append each config option into
 */
class ClientConfig(builder: ForgeConfigSpec.Builder) {
    // True if calibri should be used instead of a custom font
    private val useCalibri: ForgeConfigSpec.BooleanValue

    init {
        builder.push("general")
        useCalibri = builder
            .comment("True will force Afraid of the Dark to use the 'Calibri' font. Calibri supports a lot more languages at the cost of longer load times and more memory usage. If you are seeing a lot of question marks in text, turning this on may help.")
            .translation("config.afraidofthedark:use_calibri")
            .define("useCalibri", false)
        builder.pop()
    }

    fun reload() {
        ModClientConfiguration.useCalibri = useCalibri.get()
    }
}