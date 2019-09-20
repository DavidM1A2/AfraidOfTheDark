package com.davidm1a2.afraidofthedark.client.gui.fontLibrary;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * TrueTyper: Open Source TTF implementation for Minecraft.
 * Copyright (C) 2013 - Mr_okushama
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Code was heavily modified to support MC 1.12+
 */
public class FontLoader
{
    /**
     * Creates a font to be used by MC given a resource location, size, and anti-alias flag
     *
     * @param resourceLocation The resource location of the .ttf file to read
     * @param size The size of the font
     * @param antiAlias Flag enabling or disabling anti-aliasing
     * @return A true-type font reference to be used in the MC game engine
     */
    public static TrueTypeFont createFont(ResourceLocation resourceLocation, float size, boolean antiAlias)
    {
        return createFont(resourceLocation, size, antiAlias, Font.TRUETYPE_FONT);
    }

    /**
     * Creates a font to be used by MC given a resource location, size, type, and anti-alias flag
     *
     * @param resourceLocation The resource location of the .ttf file to read
     * @param size The size of the font
     * @param antiAlias Flag enabling or disabling anti-aliasing
     * @param type The type of font to read, see Font public static fields for options
     * @return A true-type font reference to be used in the MC game engine
     */
    public static TrueTypeFont createFont(ResourceLocation resourceLocation, float size, boolean antiAlias, int type)
    {
        try
        {
            // Create a system font first given the type and input stream created by the MC game engine
            Font font = Font.createFont(type, Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream());
            // Set the font size and make it bold. Bold fonts tend to look better in this font rendering system
            font = font.deriveFont(size).deriveFont(Font.BOLD);
            // Return a new true type font without any additional characters
            return new TrueTypeFont(font, antiAlias, null);
        }
        // If an error occurs print it and return null
        catch (Exception e)
        {
            AfraidOfTheDark.INSTANCE.getLogger().error("Could not create the true type font.", e);
            return null;
        }
    }

}