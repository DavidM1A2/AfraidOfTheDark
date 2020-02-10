package com.davidm1a2.afraidofthedark.client.gui.fontLibrary

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import java.awt.Font

/**
 * TrueTyper: Open Source TTF implementation for Minecraft.
 * Copyright (C) 2013 - Mr_okushama
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 *
 * Code was heavily modified to support MC 1.12+
 */
object FontLoader
{
    // If a font can't render all characters required use this instead
    private val DEFAULT_FONT_LOCATION = ResourceLocation("afraidofthedark:fonts/calibri.ttf")

    /**
     * Creates a font to be used by MC given a resource location, size, type, and anti-alias flag
     *
     * @param resourceLocation The resource location of the .ttf file to read
     * @param size The size of the font
     * @param antiAlias Flag enabling or disabling anti-aliasing
     * @param type The type of font to read, see Font public static fields for options
     * @return A true-type font reference to be used in the MC game engine
     */
    fun createFont(resourceLocation: ResourceLocation, size: Float, antiAlias: Boolean = true, type: Int = Font.TRUETYPE_FONT): TrueTypeFont
    {
        // Grab the list of characters our font must support
        val characters = AfraidOfTheDark.INSTANCE.configurationHandler.supportedCharacters
        // Create a system font first given the type and input stream created by the MC game engine
        var font = Font.createFont(type, Minecraft.getMinecraft().resourceManager.getResource(resourceLocation).inputStream)
        // If the font has non-ascii characters use Calibri
        if (characters.any { !font.canDisplay(it) })
        {
            font = Font.createFont(type, Minecraft.getMinecraft().resourceManager.getResource(DEFAULT_FONT_LOCATION).inputStream)
            // If Calibri can't render the characters, give up
            characters.map { !font.canDisplay(it) }.forEach { AfraidOfTheDark.INSTANCE.logger.error("Calibri doesnt support character '$it', ignoring...") }
        }

        // Set the font size and make it bold. Bold fonts tend to look better in this font rendering system
        font = font.deriveFont(size).deriveFont(Font.BOLD)

        // Return a new true type font without any additional characters
        return TrueTypeFont(font, antiAlias, characters)
    }
}