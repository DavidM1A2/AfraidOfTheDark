package com.DavidM1A2.afraidofthedark.common.potion.base;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

/**
 * Base class for all AOTD potions
 */
public class AOTDPotion extends Potion
{
    // Due to the crappy MC design all potion textures are on one PNG, so here we load that sheet of textures in
    private static final ResourceLocation AOTD_POTION_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/potion_effect/potion_effects.png");

    /**
     * Constructor sets item properties
     *
     * @param name         The name of the potion for the players to see
     * @param registryName The name of the potion in the game registry
     * @param indexX       The X index of the position on the texture sheet
     * @param indexY       The Y index of the position on the texture sheet
     * @param isBad        True if the potion is 'bad', false otherwise
     * @param color        The color of the potion
     */
    public AOTDPotion(String name, String registryName, int indexX, int indexY, boolean isBad, Color color)
    {
        super(isBad, color.hashCode());
        this.setRegistryName(Constants.MOD_ID + ":" + registryName);
        this.setPotionName(name);
        this.setIconIndex(indexX, indexY);
    }

    /**
     * Whenever MC wants to render a potion effect inject our custom textures and then render it
     *
     * @return The index of our custom potion texture sheet
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex()
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(AOTD_POTION_TEXTURES);
        return super.getStatusIconIndex();
    }

    /**
     * True if the duration is greater than 0
     *
     * @param duration  The duration left on the drink
     * @param amplifier The potion amplifier
     * @return True if the potion is ready, false otherwise
     */
    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return duration >= 1;
    }
}
