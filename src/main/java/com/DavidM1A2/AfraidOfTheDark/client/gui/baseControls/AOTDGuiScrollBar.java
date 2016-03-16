/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseMoveListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;

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
		scrollBarBackground = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spellCrafting/scrollBar.png");
		this.add(scrollBarBackground);
		scrollBarHandle = new AOTDGuiButton(0, 0, width, height / 10, null, "afraidofthedark:textures/gui/spellCrafting/scrollBarHandle.png");
		this.add(scrollBarHandle);

		this.scrollBarHandle.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered())
				{
					mouseBeingHeld = true;
					originalMousePressLocation = event.getMouseY();
					originalHandlePosition = handleLocation;
				}
			}
			@Override
			public void mousePressed(AOTDMouseEvent event) {}
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
				mouseBeingHeld = false;				
			}
			@Override
			public void mouseEntered(AOTDMouseEvent event) {}
			@Override
			public void mouseExited(AOTDMouseEvent event) {}
		});
		this.scrollBarHandle.addMouseMoveListener(new AOTDMouseMoveListener()
		{			
			@Override
			public void mouseMoved(AOTDMouseEvent event)
			{
				if (mouseBeingHeld)
				{
					int newY = (event.getMouseY() - originalMousePressLocation) + (int) (AOTDGuiScrollBar.this.getY() + originalHandlePosition * AOTDGuiScrollBar.this.getHeight());
					newY = MathHelper.clamp_int(newY, AOTDGuiScrollBar.this.getY(), AOTDGuiScrollBar.this.getY() + AOTDGuiScrollBar.this.getHeight() - scrollBarHandle.getHeight());
					scrollBarHandle.setY(newY);
					handleLocation = (scrollBarHandle.getY() - AOTDGuiScrollBar.this.getY()) / ((float) (AOTDGuiScrollBar.this.getHeight() - scrollBarHandle.getHeight()));
				}			
			}			
			@Override
			public void mouseDragged(AOTDMouseEvent event) {}
		});
	}

	public float getSliderValue()
	{
		return -this.handleLocation;
	}
}
