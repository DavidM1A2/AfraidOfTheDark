/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class AOTDGuiUtility {
	private static ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

	public static void updateScaledResolution() {
		AOTDGuiUtility.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
	}

	public static ScaledResolution getScaledResolution() {
		return AOTDGuiUtility.scaledResolution;
	}

	public static float[] convert255To01Color(Color color) {
		return new float[] { color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
				color.getAlpha() / 255.0f };
	}

	public static int mcToRealCoord(int coord) {
		return coord * scaledResolution.getScaleFactor();
	}

	public static int realToMcCoord(int coord) {
		return coord / scaledResolution.getScaleFactor();
	}

	public static int realToGLScreenCoords(int coord) {
		return Minecraft.getMinecraft().displayHeight - coord;
	}

	public static int getMouseX() {
		return Mouse.getX() * AOTDGuiUtility.getScaledResolution().getScaledWidth()
				/ Minecraft.getMinecraft().displayWidth;
	}

	public static int getMouseY() {
		return AOTDGuiUtility.getScaledResolution().getScaledHeight() - Mouse.getY()
				* AOTDGuiUtility.getScaledResolution().getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
	}

	/**
	 * Returns true if either windows ctrl key is down or if either mac meta key
	 * is down
	 */
	public static boolean isCtrlKeyDown() {
		return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220)
				: Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
	}

	/**
	 * Stores the given string in the system clipboard
	 */
	public static void setClipboardString(String copyText) {
		if (!StringUtils.isEmpty(copyText)) {
			try {
				StringSelection stringselection = new StringSelection(copyText);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, (ClipboardOwner) null);
			} catch (Exception exception) {
			}
		}
	}

	/**
	 * Returns a string stored in the system clipboard.
	 */
	public static String getClipboardString() {
		try {
			Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object) null);

			if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return (String) transferable.getTransferData(DataFlavor.stringFlavor);
			}
		} catch (Exception exception) {
		}

		return "";
	}
}
