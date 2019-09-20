package com.davidm1a2.afraidofthedark.client.gui.specialControls;

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiButton;
import com.davidm1a2.afraidofthedark.common.registry.meteor.MeteorEntry;
import org.lwjgl.util.Color;

import java.util.Random;

/**
 * Special UI control that represents a meteor button which is a meteor in the sky
 */
public class AOTDGuiMeteorButton extends AOTDGuiButton
{
    // RNG used to pick opacity randomly
    private static final Random RANDOM = new Random();
    // The type that this meteor button represents
    private final MeteorEntry meteorType;

    /**
     * Constructor initializes fields and what meteor it represents
     *
     * @param x          The x coordinate of this button
     * @param y          The y coordinate of this button
     * @param width      The width of this button
     * @param height     The height of this button
     * @param meteorType The type of meteor to represent
     */
    public AOTDGuiMeteorButton(int x, int y, int width, int height, MeteorEntry meteorType)
    {
        super(x, y, width, height, null, meteorType.getIcon().toString());
        this.meteorType = meteorType;
        // Give the meteor a random opacity
        this.setColor(new Color(255, 255, 255, RANDOM.nextInt(256)));
    }

    /**
     * @return The type that this meteor button represents
     */
    public MeteorEntry getMeteorType()
    {
        return this.meteorType;
    }
}
