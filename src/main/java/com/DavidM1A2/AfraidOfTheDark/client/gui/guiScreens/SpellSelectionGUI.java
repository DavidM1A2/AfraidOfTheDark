package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollBar;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpell;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellStage;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.client.Minecraft;

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

		for (Spell spell : this.entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getSpellManager().getSpellList())
		{
			this.addSpellContainer(spell);
		}

		AOTDGuiImage newSpell = new AOTDGuiImage(62, 218, 25, 25, "afraidofthedark:textures/gui/spellCrafting/createSpell.png");
		newSpell.setHoverText("Create a new spell");
		newSpell.addMouseListener(new AOTDMouseListener()
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
					Spell newSpell = new Spell(entityPlayer, "", null, new SpellStage[]
					{ new SpellStage(null, new ArrayList<IEffect>()) }, UUID.randomUUID());
					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getSpellManager().addSpell(newSpell);
					SpellSelectionGUI.this.addSpellContainer(newSpell);
				}
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}

			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
			}
		});
		background.add(newSpell);

		this.getContentPane().add(background);
	}

	private void addSpellContainer(Spell spell)
	{
		final AOTDGuiSpell guiSpell = new AOTDGuiSpell(0, this.spells.size() * distanceBetweenEntries, 100, 40, false, spell, this);
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
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getSpellManager().removeSpell(spell.getSpell());
			for (int i = index; i < spells.size(); i++)
			{
				AOTDGuiSpell current = spells.get(i);
				current.setY(current.getY() - distanceBetweenEntries);
			}
			if (spells.size() > 4)
				scrollPanel.setMaximumOffset((this.spells.size() - 4) * distanceBetweenEntries);
		}
	}

	public void update(AOTDGuiSpell changed)
	{
		for (AOTDGuiSpell spell : this.spells)
		{
			if (spell != changed)
			{
				if (spell.getKeyBindButton().getHoverText().equals(changed.getKeyBindButton().getHoverText()))
				{
					spell.getKeyBindButton().setHoverText("Spell currently bound to: None");
				}
			}
		}
	}

	@Override
	public void onGuiClosed()
	{
		entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).syncSpellManager();
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
