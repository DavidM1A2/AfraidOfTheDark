package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.refrence.MeteorTypes;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchTypes;

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
		baseHeight = (this.height - 256) / 2;
		baseWidth = (this.width - 256) / 2;
		xPosTelescope = baseWidth;
		yPosTelescope = baseHeight;
		buttonList.clear();
		for (int i = 0; i < NUMBER_OF_METEORS; i++)
		{
			if (LoadResearchData.isResearched(Minecraft.getMinecraft().thePlayer, ResearchTypes.Astronomy2))
			{
				buttonList.add(new MeteorButton(BUTTON_BASE_ID + i, Minecraft.getMinecraft().theWorld.rand.nextInt(3840 * 2) - 3840, Minecraft.getMinecraft().theWorld.rand.nextInt(2160 * 2) - 2160, 64, 64,
						MeteorTypes.values()[Minecraft.getMinecraft().theWorld.rand.nextInt(MeteorTypes.values().length)]));
			}
			else
			{
				buttonList.add(new MeteorButton(BUTTON_BASE_ID + i, Minecraft.getMinecraft().theWorld.rand.nextInt(3840 * 2) - 3840, Minecraft.getMinecraft().theWorld.rand.nextInt(2160 * 2) - 2160, 64, 64, MeteorTypes.silver));
			}
		}
	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(int i, int j, float f)
	{
		this.drawDefaultBackground();

		mc.renderEngine.bindTexture(nebula);
		this.drawScaledCustomSizeModalRect(xPosTelescope, yPosTelescope, guiOffsetX + 3840 / 2, guiOffsetY + 2160 / 2, FRAME_WIDTH, FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT, 3840, 2160);

		int disWidth = Minecraft.getMinecraft().displayWidth;
		int disHeight = Minecraft.getMinecraft().displayHeight;
		int widthScale = Math.round(disWidth / (float) this.width);
		int heightScale = Math.round(disHeight / (float) this.height);

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(disWidth - (xPosTelescope + FRAME_WIDTH) * widthScale, disHeight - (yPosTelescope + FRAME_HEIGHT) * heightScale, FRAME_WIDTH * 3, FRAME_HEIGHT * 3);
		super.drawScreen(i, j, f);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/TelescopeGUI.png"));
		this.drawTexturedModalRect(xPosTelescope, yPosTelescope, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
	}

	// If you press the sign button one of two things happens
	@Override
	public void actionPerformed(GuiButton button)
	{
		EntityPlayer playerWhoPressed = mc.getMinecraft().thePlayer;
		for (Object o : this.buttonList)
		{
			MeteorButton theButton = (MeteorButton) o;
			if (theButton.id == button.id)
			{
				playerWhoPressed.addChatMessage(new ChatComponentText(createMeteorMessage(theButton.getMyType())));

				playerWhoPressed.closeScreen();
				GL11.glFlush();
			}
		}
	}

	// When the mouse is dragged, update the GUI accordingly
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeBetweenClicks)
	{
		super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks);
		for (Object o : this.buttonList)
		{
			((MeteorButton) o).setPosition(guiOffsetX, guiOffsetY);
		}
	}

	@Override
	protected void checkOutOfBounds()
	{
		if (guiOffsetX > 3840 / 2)
		{
			guiOffsetX = 3840 / 2;
		}
		if (guiOffsetX < -3840 / 2)
		{
			guiOffsetX = -3840 / 2;
		}
		if (guiOffsetY > 2160 / 2)
		{
			guiOffsetY = 2160 / 2;
		}
		if (guiOffsetY < -2160 / 2)
		{
			guiOffsetY = -2160 / 2;
		}
	}

	private String createMeteorMessage(MeteorTypes type)
	{
		String toReturn = "";
		Random random = Minecraft.getMinecraft().theWorld.rand;

		Refrence.selectedMeteor[0] = random.nextInt(45);
		Refrence.selectedMeteor[1] = random.nextInt(random.nextInt(50));
		Refrence.selectedMeteor[2] = random.nextInt(random.nextInt(50));

		toReturn = toReturn + "§oIt §oappears §othat §othis " + type.formattedString() + "§ometeor §ois §ofalling §oto §oearth. §oI §ohave §ocollected §osome §oinformation §oon §oit:\n";
		toReturn = toReturn + "§oDrop §oAngle: " + Refrence.selectedMeteor[0] + "§o°  §oLatitude: " + Refrence.selectedMeteor[1] + "§o°  §oLongitude: " + Refrence.selectedMeteor[2] + "§o° ";
		toReturn = toReturn + "\n§oI §oshould §oprobably §owrite §othis §odown §oand §olater §orun §ocalculations §oin §omy §osextant.";

		return toReturn;
	}
}
