package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollBar;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpell;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

public class SpellSelectionGUI extends AOTDGuiScreen
{
	private AOTDGuiScrollBar scrollBar;
	private AOTDGuiScrollPanel scrollPanel;
	private List<AOTDGuiSpell> spells = new ArrayList<AOTDGuiSpell>();
	private static final int distanceBetweenEntries = 45;

	public SpellSelectionGUI()
	{
		AOTDGuiPanel background = new AOTDGuiPanel((640 - 180) / 2, (360 - 256) / 2, 180, 256, false);

		AOTDGuiImage backgroundImage = new AOTDGuiImage(0, 0, 149, 256, "afraidofthedark:textures/gui/spellCrafting/magicMirror.png");
		background.add(backgroundImage);

		scrollBar = new AOTDGuiScrollBar(160, 30, 15, 200);

		scrollPanel = new AOTDGuiScrollPanel(25, 35, 100, 187, true, scrollBar);
		this.scrollPanel.setMaximumOffset(0);
		background.add(scrollPanel);
		background.add(scrollBar);

		for (Spell spell : AOTDPlayerData.get(this.entityPlayer).getSpellManager().getSpellList())
		{
			this.addSpellContainer(spell);
		}

		this.getContentPane().add(background);
	}

	private void addSpellContainer(Spell spell)
	{
		AOTDGuiSpell guiSpell = new AOTDGuiSpell(0, this.spells.size() * distanceBetweenEntries, 100, 40, false, spell);
		this.spells.add(guiSpell);
		if (this.spells.size() > 4)
			this.scrollPanel.setMaximumOffset((this.spells.size() - 4) * distanceBetweenEntries);
		this.scrollPanel.add(guiSpell);
	}

	@Override
	public void onGuiClosed()
	{
		AOTDPlayerData.get(entityPlayer).syncSpellManager();
	}

	@Override
	public boolean inventoryToCloseGuiScreen()
	{
		return true;
	}

	@Override
	public boolean drawGradientBackground()
	{
		return true;
	}
}
