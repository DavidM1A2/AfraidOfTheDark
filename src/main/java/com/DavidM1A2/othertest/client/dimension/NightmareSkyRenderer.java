package com.DavidM1A2.afraidofthedark.client.dimension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class that renders the nightmare 'sky' texture
 */
public class NightmareSkyRenderer extends IRenderHandler
{
    /**
     * Called to render the sky
     *
     * @param partialTicks The number of partial ticks since the last tick
     * @param world        The world to render in
     * @param mc           The minecraft instance
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void render(float partialTicks, WorldClient world, Minecraft mc)
    {
        // Empty for now, might have a skybox later
    }
}