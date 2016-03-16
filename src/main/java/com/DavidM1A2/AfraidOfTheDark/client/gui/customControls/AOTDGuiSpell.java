package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiLabel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDKeyListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellManager;

public class AOTDGuiSpell extends AOTDGuiPanel
{
	private Spell mySpell;
	private boolean waitingOnKeyInput = false;
	private static SpellManager spellManager = AOTDPlayerData.get(entityPlayer).getSpellManager();

	public AOTDGuiSpell(int x, int y, int width, int height, boolean scissorEnabled, final Spell source)
	{
		super(x, y, width, height, scissorEnabled);

		mySpell = source;

		AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spellCrafting/spellBackground.png");
		this.add(background);

		AOTDGuiPanel spellNameContainer = new AOTDGuiPanel(5, 2, width - 15, 15, true);
		this.add(spellNameContainer);

		AOTDGuiLabel spellName = new AOTDGuiLabel(0, 0, ClientData.getTargaMSHandFontSized(30f));
		spellName.setText(source.getName());
		spellName.setTextColor(new float[]
		{ 0.96f, 0.24f, 0.78f, 1.0f });
		spellNameContainer.setHoverText(source.getName());
		spellNameContainer.add(spellName);

		AOTDMouseListener highlightEffect = new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				entityPlayer.playSound("afraidofthedark:buttonHover", 0.7f, 1.9f);
			}

			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
			}
		};

		AOTDGuiButton edit = new AOTDGuiButton(11, 22, 22, 13, null, "afraidofthedark:textures/gui/spellCrafting/spellBackgroundEdit.png");
		edit.addMouseListener(highlightEffect);
		edit.setHoverText("Edit spell");
		this.add(edit);

		AOTDGuiButton delete = new AOTDGuiButton(38, 22, 22, 13, null, "afraidofthedark:textures/gui/spellCrafting/spellBackgroundDelete.png");
		delete.addMouseListener(highlightEffect);
		delete.setHoverText("Delete spell. This cannot be undone");
		this.add(delete);

		AOTDGuiButton keyBind = new AOTDGuiButton(66, 22, 22, 13, null, "afraidofthedark:textures/gui/spellCrafting/spellBackgroundKeyBind.png");
		keyBind.addMouseListener(highlightEffect);
		keyBind.setHoverText("Spell currently bound to: " + (spellManager.keyFromSpell(mySpell) == null ? "None" : spellManager.keyFromSpell(mySpell)));
		keyBind.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered())
					waitingOnKeyInput = true;
				else if (waitingOnKeyInput)
					waitingOnKeyInput = false;
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
		this.add(keyBind);

		this.addKeyListener(new AOTDKeyListener()
		{
			@Override
			public void keyTyped(AOTDKeyEvent event)
			{
				if (waitingOnKeyInput)
				{
					spellManager.setKeybindingToSpell(event.getKey(), mySpell);
					waitingOnKeyInput = false;
				}
			}

			@Override
			public void keyReleased(AOTDKeyEvent event)
			{
			}

			@Override
			public void keyPressed(AOTDKeyEvent event)
			{
			}
		});
	}
}