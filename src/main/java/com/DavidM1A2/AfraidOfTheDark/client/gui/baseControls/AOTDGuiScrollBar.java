/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;

import net.minecraft.util.MathHelper;

public class AOTDGuiScrollBar extends AOTDGuiContainer
{
	private AOTDGuiImage scrollBarBackground;
	private AOTDGuiButton scrollBarHandle;
	private float handleLocation = 0.0f;
	private boolean mouseBeingHeld = false;
	private int originalMousePressLocation = 0;
	private float originalHandlePosition = 0;

	public AOTDGuiScrollBar(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		scrollBarBackground = new AOTDGuiImage(0, 0, width, height, "textures/gui/spellCrafting/scrollBar.png");
		this.add(scrollBarBackground);
		scrollBarHandle = new AOTDGuiButton(0, 0, width, height / 10, null, "afraidofthedark:textures/gui/spellCrafting/scrollBarHandle.png");
		this.add(scrollBarHandle);

		this.scrollBarHandle.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MousePressed)
				{
					if (component.isHovered())
					{
						mouseBeingHeld = true;
						originalMousePressLocation = AOTDGuiUtility.getMouseY();
						originalHandlePosition = handleLocation;
					}
				}
				else if (actionType == ActionType.MouseReleased)
				{
					mouseBeingHeld = false;
				}
				else if (actionType == ActionType.MouseMove)
				{
					if (mouseBeingHeld)
					{
						int newY = (AOTDGuiUtility.getMouseY() - originalMousePressLocation) + (int) (AOTDGuiScrollBar.this.getY() + originalHandlePosition * AOTDGuiScrollBar.this.getHeight());
						newY = MathHelper.clamp_int(newY, AOTDGuiScrollBar.this.getY(), AOTDGuiScrollBar.this.getY() + AOTDGuiScrollBar.this.getHeight() - scrollBarHandle.getHeight());
						scrollBarHandle.setY(newY);
						handleLocation = (scrollBarHandle.getY() - AOTDGuiScrollBar.this.getY()) / ((float) (AOTDGuiScrollBar.this.getHeight() - scrollBarHandle.getHeight()));
					}
				}
			}
		});
	}

	public float getSliderValue()
	{
		return -this.handleLocation;
	}
}
