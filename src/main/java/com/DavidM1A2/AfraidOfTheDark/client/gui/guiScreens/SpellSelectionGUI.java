package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollBar;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpell;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

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
		final AOTDGuiSpell guiSpell = new AOTDGuiSpell(0, this.spells.size() * distanceBetweenEntries, 100, 40, false, spell);
		guiSpell.getDeleteButton().addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered())
				{
					SpellSelectionGUI.this.removeSpellContainer(guiSpell);
				}
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
			}
		});
		this.spells.add(guiSpell);
		if (this.spells.size() > 4)
			this.scrollPanel.setMaximumOffset((this.spells.size() - 4) * distanceBetweenEntries);
		this.scrollPanel.add(guiSpell);
	}

	public void removeSpellContainer(AOTDGuiSpell spell)
	{
		int index = spells.indexOf(spell);
		if (index == -1)
			LogHelper.info("Attempted to delete spell at index -1, wtf?");
		else
		{
			spells.remove(index);
			scrollPanel.remove(spell);
			AOTDPlayerData.get(entityPlayer).getSpellManager().removeSpell(spell.getSpell());
			for (int i = index; i < spells.size(); i++)
			{
				AOTDGuiSpell current = spells.get(i);
				current.setY(current.getY() - distanceBetweenEntries);
			}
			if (spells.size() > 4)
				scrollPanel.setMaximumOffset((this.spells.size() - 4) * distanceBetweenEntries);
		}
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
