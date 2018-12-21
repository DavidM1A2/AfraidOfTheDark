package com.DavidM1A2.afraidofthedark.client.gui.guiScreens;

import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiButton;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiTextField;
import com.DavidM1A2.afraidofthedark.client.settings.ClientData;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.init.SoundEvents;
import org.lwjgl.util.Color;

/**
 * Class used to create a blood stained journal signing UI
 */
public class BloodStainedJournalSignGUI extends AOTDGuiScreen
{
	// The text field that you sign your name in
	private final AOTDGuiTextField nameSignField;

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

		this.nameSignField = new AOTDGuiTextField(
				45,
				90,
				160,
				30,
				ClientData.getInstance().getTargaMSHandFontSized(45f));
		this.nameSignField.setTextColor(new Color(255, 0, 0));
		this.nameSignField.setGhostText("-");
		backgroundPanel.add(this.nameSignField);

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
		signButton.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Left)
				{
					entityPlayer.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
				}
				event.consume();
			}
		});
		signButton.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				//entityPlayer.playSound(ModSounds.buttonHover, 0.1f, 0.8f);
			}
		});
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
		return !this.nameSignField.isFocused();
	}
}
