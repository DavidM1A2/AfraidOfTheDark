/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;

public abstract class AOTDGuiSpellComponent<T extends ISpellComponentEnum> extends AOTDGuiPanel
{
	private static ISpellComponentEnum selectedComponent = null;

	protected AOTDGuiImage background;
	protected AOTDGuiImage icon;
	private AOTDGuiImage highlight;
	protected T type;
	private boolean showHighlightOnSelected;

	public AOTDGuiSpellComponent(int x, int y, int width, int height, T type)
	{
		this(x, y, width, height, type, false);
	}

	public AOTDGuiSpellComponent(int x, int y, int width, int height, T type, boolean showHighlightOnSelected)
	{
		super(x, y, width, height, false);

		this.showHighlightOnSelected = showHighlightOnSelected;

		this.background = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		this.add(background);

		this.icon = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spellCrafting/blank.png");
		this.setType(type);

		this.highlight = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spellCrafting/highlight.png");
		this.highlight.setVisible(this.showHighlightOnSelected);
		this.add(this.highlight);

		this.add(this.icon);
	}

	@Override
	public void draw()
	{
		if (this.showHighlightOnSelected)
		{
			this.highlight.setVisible(AOTDGuiSpellComponent.selectedComponent == this.type);
		}
		super.draw();
	}

	public void setType(T source)
	{
		this.type = source;
		if (source == null)
			this.icon.setVisible(false);
		else
		{
			this.icon.setImageTexture(source.getIcon());
			this.icon.setVisible(true);
		}
	}

	public T getType()
	{
		return this.type;
	}

	@Override
	public void brightenColor(float amount)
	{
		super.brightenColor(amount);
		this.background.brightenColor(amount);
		this.icon.brightenColor(amount);
	}

	@Override
	public void darkenColor(float amount)
	{
		super.darkenColor(amount);
		this.background.darkenColor(amount);
		this.icon.darkenColor(amount);
	}

	public String getTypeNameFormatted()
	{
		return this.type != null ? this.type.getNameFormatted() : "None";
	}

	public abstract void updateHoverText();

	public static void setSelectedComponent(ISpellComponentEnum component)
	{
		AOTDGuiSpellComponent.selectedComponent = component;
	}

	public static ISpellComponentEnum getSelectedComponent()
	{
		return AOTDGuiSpellComponent.selectedComponent;
	}
}
