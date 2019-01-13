package com.DavidM1A2.afraidofthedark.client.gui.base;

import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Base class for all GuiScreens used by the mod, allows support for things such as action listeners and proper UI scaling
 */
public abstract class AOTDGuiScreen extends GuiScreen
{
	private final AOTDGuiPanel contentPane;
	protected final EntityPlayerSP entityPlayer = Minecraft.getMinecraft().player;
	private List<SpriteSheetController> spriteSheetControllers = new LinkedList<>();
	protected final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
	private boolean leftMouseButtonDown = false;

	/**
	 * Constructor initializes the gui panel background to be 640x360 without scissors
	 */
	public AOTDGuiScreen()
	{
		super();
		contentPane = new AOTDGuiPanel(0, 0, Constants.GUI_WIDTH, Constants.GUI_HEIGHT, false);
	}

	/**
	 * Called to initialize the GUI screen
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		// Force our GUI utility to update its scaled resolution
		AOTDGuiUtility.getInstance().refreshScaledResolution();
		// Clear all buttons on the screen
		this.buttonList.clear();

		// Compute the correct X and Y gui scale that we should use
		double guiScaleX = this.width / (double) Constants.GUI_WIDTH;
		double guiScaleY = this.height / (double) Constants.GUI_HEIGHT;
		// Set the gui screen's gui scale
		double guiScale = Math.min(guiScaleX, guiScaleY);
		// Set the content pane's gui scale
		this.contentPane.setScaleXAndY(guiScale);

		// If our X scale is less than our Y scale we pin the X coordinate to the left side of the screen and set the Y to center the GUI panel
		if (guiScaleX < guiScaleY)
		{
			this.contentPane.setX(0);
			// We must multiply by 1 / guiScale so that our Y position is centered and not scaled since 1 / guiScale * guiScale = 1
			this.contentPane.setY(Math.toIntExact(Math.round((this.height - this.contentPane.getHeightScaled()) / 2f * (1 / guiScale))));
		}
		// If our Y scale is less than our X scale we pin the Y coordinate to the top of the screen and set the X to center the GUI panel
		else
		{
			// We must multiply by 1 / guiScale so that our X position is centered and not scaled since 1 / guiScale * guiScale = 1
			this.contentPane.setX(Math.toIntExact(Math.round((this.width - this.contentPane.getWidthScaled()) / 2f * (1 / guiScale))));
			this.contentPane.setY(0);
		}
	}

	/**
	 * Most important method that draws the GUI screen, here we ask our content pane to draw itself
	 *
	 * @param mouseX The current mouse's X position
	 * @param mouseY The current mouse's Y position
	 * @param partialTicks How much time has happened since the last tick, ignored
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		// First update all of our sprite sheet controllers
		this.spriteSheetControllers.forEach(SpriteSheetController::performUpdate);
		// Enable blend so we can draw opacity
		GlStateManager.enableBlend();
		// If we want a gradient background draw that background
		if (this.drawGradientBackground())
			this.drawDefaultBackground();
		// Draw the content pane
		this.contentPane.draw();
		// Draw the overlay on top of the content pane
		this.contentPane.drawOverlay();
		// Call the super method
		super.drawScreen(mouseX, mouseY, partialTicks);
		// Disable blend now that we drew the UI
		GlStateManager.disableBlend();
	}

	/**
	 * @return True if the screen should have a gradient background, false otherwise
	 */
	public abstract boolean drawGradientBackground();

	/**
	 * @return The content pane containing all the controls to draw
	 */
	public AOTDGuiPanel getContentPane()
	{
		return this.contentPane;
	}

	/**
	 * @return True if this gui screen pauses the game, false otherwise
	 */
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	/**
	 * Called whenever a key is typed, we ask our key handler to handle the event
	 *
	 * @param character The character typed
	 * @param keyCode The code of the character typed
	 * @throws IOException forwarded from the super method
	 */
	@Override
	protected void keyTyped(char character, int keyCode) throws IOException
	{
		// Fire the process key event on our content pane
		this.contentPane.processKeyInput(new AOTDKeyEvent(this.getContentPane(), character, keyCode, AOTDKeyEvent.KeyEventType.Type));
		// If our inventory key closes the screen, test if that key was pressed
		if (this.inventoryToCloseGuiScreen())
		{
			// if the keycode is the inventory key bind close the GUI screen
			if (keyCode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode())
			{
				// Close the screen
				entityPlayer.closeScreen();
				GL11.glFlush();
			}
		}
		// Call super afterwards to finish any default MC processing
		super.keyTyped(character, keyCode);
	}

	/**
	 * @return True if the inventory key closes the screen, false otherwise
	 */
	public abstract boolean inventoryToCloseGuiScreen();

	/**
	 * Adds a sprite sheet controller to the list that will be updated every tick
	 *
	 * @param sheetController The controller to update every tick
	 */
	public void addSpriteSheetController(SpriteSheetController sheetController)
	{
		this.spriteSheetControllers.add(sheetController);
	}

	/**
	 * Called whenever mouse input should be handled
	 */
	@Override
	public void handleMouseInput() throws IOException
	{
		// Ensure to call super so default MC functions are called
		super.handleMouseInput();
		// Figure out what mouse button was pressed
		switch (Mouse.getEventButton())
		{
			// Left mouse was clicked
			case 0:
				this.processMouseClick(AOTDMouseEvent.MouseButtonClicked.Left);
				break;
			// Right mouse was clicked
			case 1:
				this.processMouseClick(AOTDMouseEvent.MouseButtonClicked.Right);
				break;
			// A drag event happened
			case -1:
				// Grab the X and Y coordinates of the mouse
				Integer mouseX = AOTDGuiUtility.getInstance().getMouseXInMCCoord();
				Integer mouseY = AOTDGuiUtility.getInstance().getMouseYInMCCoord();
				// Fire the content pane's move listener
				contentPane.processMouseInput(new AOTDMouseEvent(contentPane,mouseX, mouseY, AOTDMouseEvent.MouseButtonClicked.Other, AOTDMouseEvent.MouseEventType.Move));
				// If the left mouse button is down fire the content pane's drag listener
				if (leftMouseButtonDown)
					contentPane.processMouseInput(new AOTDMouseEvent(contentPane, mouseX, mouseY, AOTDMouseEvent.MouseButtonClicked.Other, AOTDMouseEvent.MouseEventType.Drag));
				break;
		}
	}

	/**
	 * Called to process the mouse click
	 *
	 * @param clickedButton The button that was clicked
	 */
	private void processMouseClick(AOTDMouseEvent.MouseButtonClicked clickedButton)
	{
		// The X position of the mouse
		Integer mouseX = AOTDGuiUtility.getInstance().getMouseXInMCCoord();
		// The Y position of the mouse
		Integer mouseY = AOTDGuiUtility.getInstance().getMouseYInMCCoord();

		// If the mouse button is pressed set the flag
		if (Mouse.getEventButtonState())
		{
			leftMouseButtonDown = true;
			// Fire the mouse clicked event
			contentPane.processMouseInput(new AOTDMouseEvent(contentPane, mouseX, mouseY, clickedButton, AOTDMouseEvent.MouseEventType.Click));
		}
		// If it's not pressed fire the mouse released event and press event
		else
		{
			// Fire the release event for sure
			contentPane.processMouseInput(new AOTDMouseEvent(contentPane, mouseX, mouseY, clickedButton, AOTDMouseEvent.MouseEventType.Release));
			// If the left mouse button was down fire the press event
			if (leftMouseButtonDown)
			{
				leftMouseButtonDown = false;
				contentPane.processMouseInput(new AOTDMouseEvent(contentPane, mouseX, mouseY, clickedButton, AOTDMouseEvent.MouseEventType.Press));
			}
		}
	}
}
