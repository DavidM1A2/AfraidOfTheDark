/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScreen;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiTextField;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.TextAlignment;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SpawnMeteor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class SextantGUI extends AOTDGuiScreen
{
	private AOTDGuiTextField angle;
	private AOTDGuiTextField latitude;
	private AOTDGuiTextField longitude;

	public SextantGUI()
	{
		AOTDGuiPanel background = new AOTDGuiPanel((640 - 256) / 2, (360 - 256) / 2, 256, 256, false);

		AOTDGuiImage backgroundImage = new AOTDGuiImage(0, 0, 256, 256, "textures/gui/sextant.png");
		background.add(backgroundImage);

		this.angle = new AOTDGuiTextField(15, 108, 120, 30, ClientData.getTargaMSHandFontSized(45f));
		this.latitude = new AOTDGuiTextField(15, 140, 120, 30, ClientData.getTargaMSHandFontSized(45f));
		this.longitude = new AOTDGuiTextField(15, 172, 120, 30, ClientData.getTargaMSHandFontSized(45f));
		angle.setTextColor(Color.WHITE);
		angle.setGhostText("Angle");
		background.add(angle);
		latitude.setTextColor(Color.WHITE);
		latitude.setGhostText("Latitude");
		background.add(latitude);
		longitude.setTextColor(Color.WHITE);
		longitude.setGhostText("Longitude");
		background.add(longitude);

		AOTDGuiButton confirm = new AOTDGuiButton(15, 204, 120, 20, ClientData.getTargaMSHandFontSized(40f), "afraidofthedark:textures/gui/signButton.png", "afraidofthedark:textures/gui/signButtonHovered.png");
		confirm.setText("Calculate");
		confirm.setTextAlignment(TextAlignment.ALIGN_CENTER);
		confirm.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == actionType.MousePressed)
					if (component.isHovered())
						try
						{
							if ((Integer.parseInt(SextantGUI.this.angle.getText()) == ClientData.selectedMeteor[0]) && (Integer.parseInt(SextantGUI.this.latitude.getText()) == ClientData.selectedMeteor[1]) && (Integer.parseInt(SextantGUI.this.longitude.getText()) == ClientData.selectedMeteor[2]))
							{
								SextantGUI.this.tellServerToCreateMeteor(entityPlayer);
								entityPlayer.closeScreen();
							}
							else if (SextantGUI.this.angle.getText().isEmpty() || SextantGUI.this.latitude.getText().isEmpty() || SextantGUI.this.longitude.getText().isEmpty())
								entityPlayer.addChatMessage(new ChatComponentText("I forgot to fill out one of the fields."));
							else
								entityPlayer.addChatMessage(new ChatComponentText("The calculation was not sucessful.\nMaybe I entered incorrect numbers or should find another meteor to track."));
						}
						catch (final Exception e)
						{
							entityPlayer.addChatMessage(new ChatComponentText("The calculation was not sucessful.\nMaybe I entered incorrect numbers or should find another meteor to track."));
						}
			}
		});
		background.add(confirm);

		this.getContentPane().add(background);
	}

	// If E is typed we close the GUI screen
	@Override
	protected void keyTyped(final char character, final int keyCode) throws IOException
	{
		super.keyTyped(character, keyCode);
		if (!this.angle.isFocused() && !this.latitude.isFocused() && !this.longitude.isFocused())
		{
			if (keyCode == INVENTORY_KEYCODE)
			{
				entityPlayer.closeScreen();
				GL11.glFlush();
			}
		}
	}

	private void tellServerToCreateMeteor(final EntityPlayer entityPlayer)
	{
		final Random random = entityPlayer.worldObj.rand;
		final int xLocOfDrop = (int) entityPlayer.posX + (((random.nextDouble() >= .5) ? -1 : 1) * (entityPlayer.worldObj.rand.nextInt(500) + 15));
		final int zLocOfDrop = (int) entityPlayer.posZ + (((random.nextDouble() >= .5) ? -1 : 1) * (entityPlayer.worldObj.rand.nextInt(500) + 15));

		final BlockPos location = new BlockPos(xLocOfDrop, 255, zLocOfDrop);
		AfraidOfTheDark.getPacketHandler().sendToServer(new SpawnMeteor(location, 3, 3, ClientData.watchedMeteorType.getIndex()));
		entityPlayer.addChatMessage(new ChatComponentText("Based off of this information the meteor fell at " + xLocOfDrop + ", " + zLocOfDrop));
		ClientData.selectedMeteor = new int[]
		{ -1, -1, -1 };
		ClientData.watchedMeteorType = null;
	}

	@Override
	public boolean inventoryToCloseGuiScreen()
	{
		return false;
	}

	@Override
	public boolean drawGradientBackground()
	{
		return true;
	}
}
