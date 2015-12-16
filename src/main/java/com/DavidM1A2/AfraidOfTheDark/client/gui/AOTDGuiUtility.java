/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class AOTDGuiUtility
{
	private static ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);

	public static void updateScaledResolution()
	{
		AOTDGuiUtility.scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	}

	public static ScaledResolution getScaledResolution()
	{
		return AOTDGuiUtility.scaledResolution;
	}

	public static int mcToRealCoord(int coord)
	{
		return coord * scaledResolution.getScaleFactor();
	}

	public static int realToGLScreenCoords(int coord)
	{
		return Minecraft.getMinecraft().displayHeight - coord;
	}
}
