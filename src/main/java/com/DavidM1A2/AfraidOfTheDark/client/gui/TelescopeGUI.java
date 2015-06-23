/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.GuiClickAndDragable;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.MeteorButton;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.MeteorTypes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class TelescopeGUI extends GuiClickAndDragable
{
	private static final int FRAME_HEIGHT = 256;
	private static final int FRAME_WIDTH = 256;

	private static final int BUTTON_BASE_ID = 0;

	private static final int NUMBER_OF_METEORS = 25;

	// GUI height and width
	private static int baseWidth = 512;
	private static int baseHeight = 512;

	private static final ResourceLocation nebula = new ResourceLocation("afraidofthedark:textures/gui/TelescopeBackground.png");

	// Current GUI x/y Scrool positions, background positions, and research
	// positions
	private static int xPosTelescope;
	private static int yPosTelescope;

	/*
	 * GUI for the blood stained journal on the initial signing
	 */
	@Override
	public void initGui()
	{
		// Calculate the various positions of GUI elements on the screen
		TelescopeGUI.baseHeight = (this.height - 256) / 2;
		TelescopeGUI.baseWidth = (this.width - 256) / 2;
		TelescopeGUI.xPosTelescope = TelescopeGUI.baseWidth;
		TelescopeGUI.yPosTelescope = TelescopeGUI.baseHeight;
		this.buttonList.clear();
		for (int i = 0; i < TelescopeGUI.NUMBER_OF_METEORS; i++)
		{
			if (LoadResearchData.isResearched(Minecraft.getMinecraft().thePlayer, ResearchTypes.AstronomyII))
			{
				this.buttonList.add(new MeteorButton(TelescopeGUI.BUTTON_BASE_ID + i, Minecraft.getMinecraft().theWorld.rand.nextInt(3840 * 2) - 3840, Minecraft.getMinecraft().theWorld.rand.nextInt(2160 * 2) - 2160, 64, 64, MeteorTypes.values()[Minecraft.getMinecraft().theWorld.rand
						.nextInt(MeteorTypes.values().length)]));
			}
			else
			{
				this.buttonList.add(new MeteorButton(TelescopeGUI.BUTTON_BASE_ID + i, Minecraft.getMinecraft().theWorld.rand.nextInt(3840 * 2) - 3840, Minecraft.getMinecraft().theWorld.rand.nextInt(2160 * 2) - 2160, 64, 64, MeteorTypes.silver));
			}
		}
	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(final int i, final int j, final float f)
	{
		this.drawDefaultBackground();

		this.mc.renderEngine.bindTexture(TelescopeGUI.nebula);
		Gui.drawScaledCustomSizeModalRect(TelescopeGUI.xPosTelescope, TelescopeGUI.yPosTelescope, this.guiOffsetX + (3840 / 2), this.guiOffsetY + (2160 / 2), TelescopeGUI.FRAME_WIDTH, TelescopeGUI.FRAME_HEIGHT, TelescopeGUI.FRAME_WIDTH, TelescopeGUI.FRAME_HEIGHT, 3840, 2160);

		final int disWidth = Minecraft.getMinecraft().displayWidth;
		final int disHeight = Minecraft.getMinecraft().displayHeight;
		final int widthScale = Math.round(disWidth / (float) this.width);
		final int heightScale = Math.round(disHeight / (float) this.height);

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(disWidth - ((TelescopeGUI.xPosTelescope + TelescopeGUI.FRAME_WIDTH) * widthScale), disHeight - ((TelescopeGUI.yPosTelescope + TelescopeGUI.FRAME_HEIGHT) * heightScale), TelescopeGUI.FRAME_WIDTH * widthScale, TelescopeGUI.FRAME_HEIGHT * heightScale);
		super.drawScreen(i, j, f);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		this.mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/TelescopeGUI.png"));
		this.drawTexturedModalRect(TelescopeGUI.xPosTelescope, TelescopeGUI.yPosTelescope, 0, 0, TelescopeGUI.FRAME_WIDTH, TelescopeGUI.FRAME_HEIGHT);
	}

	// If you press the sign button one of two things happens
	@Override
	public void actionPerformed(final GuiButton button)
	{
		final EntityPlayer playerWhoPressed = Minecraft.getMinecraft().thePlayer;
		for (final Object o : this.buttonList)
		{
			final MeteorButton theButton = (MeteorButton) o;
			if (theButton.id == button.id)
			{
				playerWhoPressed.addChatMessage(new ChatComponentText(this.createMeteorMessage(theButton.getMyType())));

				playerWhoPressed.closeScreen();
				GL11.glFlush();
			}
		}
	}

	// When the mouse is dragged, update the GUI accordingly
	@Override
	protected void mouseClickMove(final int mouseX, final int mouseY, final int lastButtonClicked, final long timeBetweenClicks)
	{
		super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks);
		for (final Object o : this.buttonList)
		{
			((MeteorButton) o).setPosition(this.guiOffsetX, this.guiOffsetY);
		}
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

		toReturn = toReturn + "§oIt §oappears §othat §othis " + type.formattedString() + "§ometeor §ois §ofalling §oto §oearth. §oI §ohave §ocollected §osome §oinformation §oon §oit:\n";
		toReturn = toReturn + "§oDrop §oAngle: " + ClientData.selectedMeteor[0] + "§o°  §oLatitude: " + ClientData.selectedMeteor[1] + "§o°  §oLongitude: " + ClientData.selectedMeteor[2] + "§o° ";
		toReturn = toReturn + "\n§oI §oshould §oprobably §owrite §othis §odown §oand §olater §orun §ocalculations §oin §omy §osextant.";

		return toReturn;
	}
}
