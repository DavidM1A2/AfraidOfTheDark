package com.DavidM1A2.afraidofthedark.client.gui.guiScreens;

import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiLabel;
import com.DavidM1A2.afraidofthedark.client.settings.ClientData;

public class BloodStainedJournalSignGUI extends AOTDGuiScreen
{
	public BloodStainedJournalSignGUI()
	{
		super();

		AOTDGuiLabel aotdGuiLabel = new AOTDGuiLabel(50, 50, ClientData.getInstance().getTargaMSHandFontSized(35f));
		aotdGuiLabel.setText("Test label abcdefgh dd");
		AOTDGuiLabel sec = new AOTDGuiLabel(0, 0, ClientData.getInstance().getTargaMSHandFontSized(55f));
		sec.setText("This is a test string that is used to show labels");
		AOTDGuiLabel thr = new AOTDGuiLabel(100, 100, ClientData.getInstance().getTargaMSHandFontSized(31f));
		thr.setText("Third test label abc defg lol test 134 abc");
		this.getContentPane().add(aotdGuiLabel);
		this.getContentPane().add(sec);
		this.getContentPane().add(thr);
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
