/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseMoveListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;

import net.minecraft.client.Minecraft;

public class AOTDGuiSpellStage extends AOTDGuiPanel
{
	private AOTDGuiButton addNewRow;
	private AOTDGuiButton removeRow;

	public AOTDGuiSpellStage(int x, int y, int width, int height, boolean scissorEnabled)
	{
		super(x, y, width, height, scissorEnabled);

		AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height - 14, "afraidofthedark:textures/gui/spellCrafting/tabletSpellModule2.png");
		this.add(background);

		AOTDGuiImage deliveryMethod = new AOTDGuiImage(5, 5, height - 25, height - 25, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		deliveryMethod.setColor(new float[]
		{ 0.8f, 0.8f, 1.0f, 1.0f });
		deliveryMethod.setHoverText("Delivery Method Slot");
		deliveryMethod.addMouseListener(new AOTDMouseListener()
		{			
			@Override
			public void mouseReleased(AOTDMouseEvent event) {}			
			@Override
			public void mousePressed(AOTDMouseEvent event) {}
			
			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}
			
			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
			}
			
			@Override
			public void mouseClicked(AOTDMouseEvent event) {}
		});
		this.add(deliveryMethod);

		AOTDMouseListener effectHover = new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event) {}
			@Override
			public void mousePressed(AOTDMouseEvent event) {}
			@Override
			public void mouseReleased(AOTDMouseEvent event) {}

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
		};

		AOTDGuiImage effect1 = new AOTDGuiImage(25, 5, height - 25, height - 25, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		effect1.addMouseListener(effectHover);
		effect1.setHoverText("Effect Slot");
		AOTDGuiImage effect2 = new AOTDGuiImage(45, 5, height - 25, height - 25, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		effect2.addMouseListener(effectHover);
		effect2.setHoverText("Effect Slot");
		AOTDGuiImage effect3 = new AOTDGuiImage(65, 5, height - 25, height - 25, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		effect3.addMouseListener(effectHover);
		effect3.setHoverText("Effect Slot");
		AOTDGuiImage effect4 = new AOTDGuiImage(85, 5, height - 25, height - 25, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		effect4.addMouseListener(effectHover);
		effect4.setHoverText("Effect Slot");
		this.add(effect1);
		this.add(effect2);
		this.add(effect3);
		this.add(effect4);

		this.addNewRow = new AOTDGuiButton(0, height - 15, 15, 15, null, "afraidofthedark:textures/gui/spellCrafting/add.png");
		this.addNewRow.setHoverText("Add new spell stage");
		this.addNewRow.addMouseListener(effectHover);
		this.add(this.addNewRow);
		this.removeRow = new AOTDGuiButton(15, height - 15, 15, 15, null, "afraidofthedark:textures/gui/spellCrafting/delete.png");
		this.removeRow.setHoverText("Remove spell stage");
		this.removeRow.addMouseListener(effectHover);
		this.add(this.removeRow);
	}

	public void showPlus()
	{
		this.addNewRow.setVisible(true);
	}

	public void showMinus()
	{
		this.removeRow.setVisible(true);
	}

	public void hidePlus()
	{
		this.addNewRow.setVisible(false);
	}

	public void hideMinus()
	{
		this.removeRow.setVisible(false);
	}

	public void addMouseListenerToNewRow(AOTDMouseListener mouseListener)
	{
		this.addNewRow.addMouseListener(mouseListener);
	}

	public void addMouseListenerToRemoveRow(AOTDMouseListener mouseListener)
	{
		this.removeRow.addMouseListener(mouseListener);
	}
}
