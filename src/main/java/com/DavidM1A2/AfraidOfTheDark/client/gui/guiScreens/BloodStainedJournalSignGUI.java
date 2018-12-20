package com.DavidM1A2.afraidofthedark.client.gui.guiScreens;

import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiButton;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel;
import com.DavidM1A2.afraidofthedark.client.settings.ClientData;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import org.lwjgl.util.Color;

/**
 * Class used to create a blood stained journal signing UI
 */
public class BloodStainedJournalSignGUI extends AOTDGuiScreen
{
	/**
	 * Constructor adds any required components to the sign UI
	 */
	public BloodStainedJournalSignGUI()
	{
		super();

		final int GUI_SIZE = 256;
		// Setup the background panel that holds all of our controls
		AOTDGuiPanel backgroundPanel = new AOTDGuiPanel(
				(Constants.GUI_WIDTH - GUI_SIZE) / 2,
				(Constants.GUI_HEIGHT - GUI_SIZE) / 2,
				GUI_SIZE,
				GUI_SIZE,
				false);

		// Add a background image to the background panel
		final int BACKGROUND_IMAGE_SIZE = 220;
		AOTDGuiImage backgroundImage = new AOTDGuiImage(
				(GUI_SIZE - BACKGROUND_IMAGE_SIZE) / 2,
				0,
				BACKGROUND_IMAGE_SIZE,
				BACKGROUND_IMAGE_SIZE,
				"afraidofthedark:textures/gui/blood_stained_journal.png");
		backgroundPanel.add(backgroundImage);

		// Add the sign button
		final int SIGN_BUTTON_WIDTH = 100;
		final int SIGN_BUTTON_HEIGHT = 25;
		AOTDGuiButton signButton = new AOTDGuiButton(
				GUI_SIZE / 2 - SIGN_BUTTON_WIDTH / 2,
				GUI_SIZE - 30,
				SIGN_BUTTON_WIDTH,
				SIGN_BUTTON_HEIGHT,
				ClientData.getInstance().getTargaMSHandFontSized(55f),
				"afraidofthedark:textures/gui/sign_button.png",
				"afraidofthedark:textures/gui/sign_button_hovered.png");
		signButton.setText("Sign");
		signButton.setTextColor(new Color(255, 0, 0));
		signButton.setTextAlignment(TextAlignment.ALIGN_CENTER);
		backgroundPanel.add(signButton);

		// Add the background panel to the content pane
		this.getContentPane().add(backgroundPanel);
	}

	/**
	 * @return True because we want the background to be a gray
	 */
	@Override
	public boolean drawGradientBackground()
	{
		return true;
	}

	/**
	 * @return False since we can't use e to close the GUI screen
	 */
	@Override
	public boolean inventoryToCloseGuiScreen()
	{
		return false;
	}
}
