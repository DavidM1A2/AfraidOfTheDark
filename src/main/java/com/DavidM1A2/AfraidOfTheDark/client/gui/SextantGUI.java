package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.TellServerToCreateMeteor;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public class SextantGUI extends GuiScreen
{
	private GuiTextField angle;
	private GuiTextField latitude;
	private GuiTextField longitude;
	private GuiLabel angleLabel;
	private GuiLabel latitudeLabel;
	private GuiLabel longitudeLabel;
	private GuiButton runCalculations;
	private final static int RUN_CALCULATIONS_ID = 0;
	private final static int TEXTFIELD_BASE_ID = 1;
	private static ResourceLocation background = new ResourceLocation("afraidofthedark:textures/gui/sextant.png");
	private int xPosBackground;
	private int yPosBackground;

	/*
	 * GUI for the blood stained journal on the initial signing
	 */
	@Override
	public void initGui()
	{
		this.buttonList.clear();
		this.xPosBackground = (this.width - 256) / 2;
		this.yPosBackground = (this.height - 256) / 2;
		this.angle = new GuiTextField(TEXTFIELD_BASE_ID, mc.fontRendererObj, 15 + xPosBackground, yPosBackground + 128, 100, 20);
		this.latitude = new GuiTextField(TEXTFIELD_BASE_ID + 2, mc.fontRendererObj, 15 + xPosBackground, yPosBackground + 150, 100, 20);
		this.longitude = new GuiTextField(TEXTFIELD_BASE_ID + 4, mc.fontRendererObj, 15 + xPosBackground, yPosBackground + 172, 100, 20);
		this.runCalculations = new GuiButton(RUN_CALCULATIONS_ID, 15 + xPosBackground, yPosBackground + 194, 100, 20, "Run Calculation");
		this.angleLabel = new GuiLabel(mc.fontRendererObj, TEXTFIELD_BASE_ID + 1, 120 + xPosBackground, yPosBackground + 128, 100, 20, -1);
		this.latitudeLabel = new GuiLabel(mc.fontRendererObj, TEXTFIELD_BASE_ID + 3, 120 + xPosBackground, yPosBackground + 150, 100, 20, -1);
		this.longitudeLabel = new GuiLabel(mc.fontRendererObj, TEXTFIELD_BASE_ID + 5, 120 + xPosBackground, yPosBackground + 172, 100, 20, -1);
		this.buttonList.add(runCalculations);
		angleLabel.func_175202_a("Angle");
		latitudeLabel.func_175202_a("Latitude");
		longitudeLabel.func_175202_a("Longitude");
		this.labelList.add(angleLabel);
		this.labelList.add(latitudeLabel);
		this.labelList.add(longitudeLabel);
	}

	// Opening a research book DOES NOT pause the game (unlike escape)
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	// If E is typed we close the GUI screen
	@Override
	protected void keyTyped(char character, int keyCode) throws IOException
	{
		super.keyTyped(character, keyCode);

		if (!this.angle.isFocused() && !this.latitude.isFocused() && !this.longitude.isFocused())
		{
			if (character == 'e' || character == 'E')
			{
				Minecraft.getMinecraft().thePlayer.closeScreen();
				GL11.glFlush();
			}
		}

		if (this.angle.isFocused())
		{
			this.angle.textboxKeyTyped(character, keyCode);
		}
		if (this.latitude.isFocused())
		{
			this.latitude.textboxKeyTyped(character, keyCode);
		}
		if (this.longitude.isFocused())
		{
			this.longitude.textboxKeyTyped(character, keyCode);
		}
	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(int i, int j, float f)
	{
		this.drawDefaultBackground();

		mc.renderEngine.bindTexture(background);
		this.drawScaledCustomSizeModalRect(xPosBackground, yPosBackground, 0, 0, 512, 512, 256, 256, 512, 512);

		angle.drawTextBox();
		latitude.drawTextBox();
		longitude.drawTextBox();

		super.drawScreen(i, j, f);
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseX >= this.angle.xPosition && mouseY >= this.angle.yPosition && mouseX < this.angle.xPosition + this.angle.width && mouseY < this.angle.yPosition + this.angle.height)
		{
			this.angle.setFocused(true);
			this.latitude.setFocused(false);
			this.longitude.setFocused(false);
		}
		else if (mouseX >= this.latitude.xPosition && mouseY >= this.latitude.yPosition && mouseX < this.latitude.xPosition + this.latitude.width && mouseY < this.latitude.yPosition + this.latitude.height)
		{
			this.latitude.setFocused(true);
			this.angle.setFocused(false);
			this.longitude.setFocused(false);
		}
		else if (mouseX >= this.longitude.xPosition && mouseY >= this.longitude.yPosition && mouseX < this.longitude.xPosition + this.longitude.width && mouseY < this.longitude.yPosition + this.longitude.height)
		{
			this.longitude.setFocused(true);
			this.latitude.setFocused(false);
			this.angle.setFocused(false);
		}
	}

	// If you press the sign button one of two things happens
	@Override
	public void actionPerformed(GuiButton button)
	{
		if (button.id == RUN_CALCULATIONS_ID)
		{
			EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
			try
			{
				if (Integer.parseInt(this.angle.getText()) == Refrence.selectedMeteor[0] && Integer.parseInt(this.latitude.getText()) == Refrence.selectedMeteor[1] && Integer.parseInt(this.longitude.getText()) == Refrence.selectedMeteor[2])
				{
					tellServerToCreateMeteor(entityPlayer);
					entityPlayer.closeScreen();
				}
				else if (this.angle.getText().isEmpty() || this.latitude.getText().isEmpty() || this.longitude.getText().isEmpty())
				{
					entityPlayer.addChatMessage(new ChatComponentText("§oI §oforgot §oto §ofill §oout §oone §oof §othe §ofields."));
				}
				else
				{
					entityPlayer.addChatMessage(new ChatComponentText("§oThe §ocalculation §owas §onot §osucessful.\n§oMaybe §oI §oentered §oincorrect §onumbers §oor §oshould §ofind §oanother §ometeor §oto §otrack."));
				}
			}
			catch (Exception e)
			{
				entityPlayer.addChatMessage(new ChatComponentText("§oThe §ocalculation §owas §onot §osucessful.\n§oMaybe §oI §oentered §oincorrect §onumbers §oor §oshould §ofind §oanother §ometeor §oto §otrack."));
			}
		}
	}

	private void tellServerToCreateMeteor(EntityPlayer entityPlayer)
	{
		Random random = entityPlayer.worldObj.rand;
		int xLocOfDrop = (int) entityPlayer.posX + 15 + entityPlayer.worldObj.rand.nextInt(100);
		int zLocOfDrop = (int) entityPlayer.posZ + 15 + entityPlayer.worldObj.rand.nextInt(100);
		xLocOfDrop = xLocOfDrop * ((random.nextDouble() >= .5) ? -1 : 1);
		zLocOfDrop = zLocOfDrop * ((random.nextDouble() >= .5) ? -1 : 1);
		BlockPos location = new BlockPos(xLocOfDrop, 255, zLocOfDrop);
		AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new TellServerToCreateMeteor(location, 3, 3));
		entityPlayer.addChatMessage(new ChatComponentText("§oBased §ooff §oof §othis §oinformation §othe §ometeor §ofell §oat §o" + xLocOfDrop + "§o, §o" + zLocOfDrop));
		Refrence.selectedMeteor = new int[]
		{ -1, -1, -1 };
	}
}
