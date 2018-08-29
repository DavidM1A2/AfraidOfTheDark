package com.DavidM1A2.afraidofthedark.client.gui.guiScreens;

import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiButton;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiLabel;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel;
import com.DavidM1A2.afraidofthedark.client.settings.ClientData;
import org.lwjgl.util.Color;

public class BloodStainedJournalSignGUI extends AOTDGuiScreen
{
	public BloodStainedJournalSignGUI()
	{
		super();

		// Setup the background panel that holds all of our controls
		AOTDGuiPanel backgroundPanel = new AOTDGuiPanel((640 - 256) / 2, (360 - 256) / 2, 256, 256, false);

		// Add a background image to the background panel
		AOTDGuiImage backgroundImage = new AOTDGuiImage(28, 0, 220, 220, "afraidofthedark:textures/gui/blood_stained_journal.png");
		backgroundPanel.add(backgroundImage);

		// Add the sign button
		AOTDGuiButton signButton = new AOTDGuiButton(256 / 2 - 100 / 2, 256 - 30, 100, 25, ClientData.getInstance().getTargaMSHandFontSized(55f), "afraidofthedark:textures/gui/sign_button.png", "afraidofthedark:textures/gui/sign_button_hovered.png");
		signButton.setText("Sign");
		signButton.setTextColor(new Color(255, 0, 0));
		signButton.setTextAlignment(TextAlignment.ALIGN_CENTER);
		backgroundPanel.add(signButton);

		// Add the background panel to the content pane
		this.getContentPane().add(backgroundPanel);
	}

	@Override
	public boolean drawGradientBackground()
	{
		return true;
	}

	@Override
	public boolean inventoryToCloseGuiScreen()
	{
		return false;
	}
}
