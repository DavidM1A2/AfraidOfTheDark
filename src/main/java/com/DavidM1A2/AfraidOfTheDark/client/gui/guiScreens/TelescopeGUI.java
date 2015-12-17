/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiClickAndDragable;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiMeteorButton;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.MeteorTypes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class TelescopeGUI extends AOTDGuiClickAndDragable
{
	private final AOTDGuiPanel telescopeMeteors;
	private final AOTDGuiImage telescopeImage;

	public TelescopeGUI()
	{
		int FRAME_HEIGHT = 256;
		int FRAME_WIDTH = 256;

		// GUI height and width
		int baseWidth = 512;
		int baseHeight = 512;
		// Current GUI x/y Scrool positions, background positions, and research
		// positions
		int xPosTelescope;
		int yPosTelescope;
		// Calculate the various positions of GUI elements on the screen
		baseHeight = (360 - 256) / 2;
		baseWidth = (640 - 256) / 2;
		xPosTelescope = baseWidth;
		yPosTelescope = baseHeight;

		AOTDGuiPanel telescope = new AOTDGuiPanel(xPosTelescope, yPosTelescope, FRAME_WIDTH, FRAME_HEIGHT, true);
		AOTDGuiImage telescopeFrame = new AOTDGuiImage(0, 0, FRAME_WIDTH, FRAME_HEIGHT, "textures/gui/telescopeGUI.png");
		telescopeMeteors = new AOTDGuiPanel(0, 0, FRAME_WIDTH, FRAME_WIDTH, false);
		telescopeImage = new AOTDGuiImage(0, 0, FRAME_WIDTH, FRAME_HEIGHT, 2160, 3840, "textures/gui/telescopeBackground.png");
		telescopeImage.setU(this.guiOffsetX + (3840 / 2));
		telescopeImage.setV(this.guiOffsetY + (2160 / 2));

		int numberOfMeteors = 50 + Minecraft.getMinecraft().thePlayer.getRNG().nextInt(50);
		for (int i = 0; i < numberOfMeteors; i++)
		{
			AOTDGuiMeteorButton nextToAdd = null;
			if (AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).isResearched(ResearchTypes.AstronomyII))
				nextToAdd = new AOTDGuiMeteorButton(Minecraft.getMinecraft().theWorld.rand.nextInt(3840 * 2) - 3840, Minecraft.getMinecraft().theWorld.rand.nextInt(2160 * 2) - 2160, 64, 64, MeteorTypes.values()[Minecraft.getMinecraft().theWorld.rand.nextInt(MeteorTypes.values().length)]);
			else
				nextToAdd = new AOTDGuiMeteorButton(Minecraft.getMinecraft().theWorld.rand.nextInt(3840 * 2) - 3840, Minecraft.getMinecraft().theWorld.rand.nextInt(2160 * 2) - 2160, 64, 64, MeteorTypes.silver);

			nextToAdd.addActionListener(new AOTDActionListener()
			{
				@Override
				public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
				{
					if (actionType == ActionType.MousePressed)
					{
						Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(TelescopeGUI.this.createMeteorMessage(((AOTDGuiMeteorButton) component).getMyType())));
						Minecraft.getMinecraft().thePlayer.closeScreen();
					}
				}
			});
			telescopeMeteors.add(nextToAdd);
		}

		telescope.add(telescopeImage);
		telescope.add(telescopeMeteors);
		telescope.add(telescopeFrame);
		this.getContentPane().add(telescope);
	}

	// When the mouse is dragged, update the GUI accordingly
	@Override
	protected void mouseClickMove(final int mouseX, final int mouseY, final int lastButtonClicked, final long timeBetweenClicks)
	{
		super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks);

		this.telescopeMeteors.setX(-this.guiOffsetX + telescopeMeteors.getParent().getX());
		this.telescopeMeteors.setY(-this.guiOffsetY + telescopeMeteors.getParent().getY());

		telescopeImage.setU(this.guiOffsetX + (3840 / 2));
		telescopeImage.setV(this.guiOffsetY + (2160 / 2));

	}

	@Override
	protected void checkOutOfBounds()
	{
		if (this.guiOffsetX > (3840 / 2))
		{
			this.guiOffsetX = 3840 / 2;
		}
		if (this.guiOffsetX < (-3840 / 2))
		{
			this.guiOffsetX = -3840 / 2;
		}
		if (this.guiOffsetY > (2160 / 2))
		{
			this.guiOffsetY = 2160 / 2;
		}
		if (this.guiOffsetY < (-2160 / 2))
		{
			this.guiOffsetY = -2160 / 2;
		}
	}

	private String createMeteorMessage(final MeteorTypes type)
	{
		String toReturn = "";
		final Random random = Minecraft.getMinecraft().theWorld.rand;

		ClientData.selectedMeteor[0] = random.nextInt(45) + 5;
		ClientData.selectedMeteor[1] = random.nextInt(50) + 5;
		ClientData.selectedMeteor[2] = random.nextInt(50) + 5;
		ClientData.watchedMeteorType = type;

		toReturn = toReturn + "It appears that this " + type.formattedString() + "meteor is falling to earth. I have collected some information on it:\n";
		toReturn = toReturn + "Drop Angle: " + ClientData.selectedMeteor[0] + " degrees  Latitude: " + ClientData.selectedMeteor[1] + " degrees Longitude: " + ClientData.selectedMeteor[2] + " degrees ";
		toReturn = toReturn + "\nI should probably write this down and later run calculations in my sextant.";

		return toReturn;
	}
}
