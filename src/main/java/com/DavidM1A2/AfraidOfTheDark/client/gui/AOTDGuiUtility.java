/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class AOTDGuiUtility
{
	private static ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
	private static boolean scissorWasEnabled = false;
	private static int numberOfActiveScissors = 0;

	public static void updateScaledResolution()
	{
		AOTDGuiUtility.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
	}

	public static ScaledResolution getScaledResolution()
	{
		return AOTDGuiUtility.scaledResolution;
	}

	public static int mcToRealCoord(int coord)
	{
		return coord * scaledResolution.getScaleFactor();
	}

	public static int realToMcCoord(int coord)
	{
		return coord / scaledResolution.getScaleFactor();
	}

	public static int realToGLScreenCoords(int coord)
	{
		return Minecraft.getMinecraft().displayHeight - coord;
	}

	public static int getMouseX()
	{
		return Mouse.getX() * AOTDGuiUtility.getScaledResolution().getScaledWidth() / Minecraft.getMinecraft().displayWidth;
	}

	public static int getMouseY()
	{
		return AOTDGuiUtility.getScaledResolution().getScaledHeight() - Mouse.getY() * AOTDGuiUtility.getScaledResolution().getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
	}

	/**
	 * Returns true if either windows ctrl key is down or if either mac meta key
	 * is down
	 */
	public static boolean isCtrlKeyDown()
	{
		return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
	}

	/**
	 * Stores the given string in the system clipboard
	 */
	public static void setClipboardString(String copyText)
	{
		if (!StringUtils.isEmpty(copyText))
		{
			try
			{
				StringSelection stringselection = new StringSelection(copyText);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, (ClipboardOwner) null);
			}
			catch (Exception exception)
			{
			}
		}
	}

	/**
	 * Returns a string stored in the system clipboard.
	 */
	public static String getClipboardString()
	{
		try
		{
			Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object) null);

			if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
			{
				return (String) transferable.getTransferData(DataFlavor.stringFlavor);
			}
		}
		catch (Exception exception)
		{
		}

		return "";
	}

	public static void beginGLScissor(int x, int y, int width, int height)
	{
		if (numberOfActiveScissors == 0)
			scissorWasEnabled = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
		GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(x, y, width, height);
		numberOfActiveScissors = numberOfActiveScissors + 1;
	}

	public static void endGLScissor()
	{
		if (numberOfActiveScissors == 0)
		{
			LogHelper.info("Tried to end a scissor that was not even started...");
			return;
		}
		if (numberOfActiveScissors == 0 && !scissorWasEnabled)
		{
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
			GL11.glPopAttrib();
		}
		else
		{
			GL11.glPopAttrib();
		}
	}
}
