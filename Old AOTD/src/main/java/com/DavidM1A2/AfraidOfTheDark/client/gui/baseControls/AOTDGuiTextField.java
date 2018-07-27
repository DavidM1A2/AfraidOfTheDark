/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDKeyListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent.MouseButtonClicked;
import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.TrueTypeFont;

import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;

public class AOTDGuiTextField extends AOTDGuiTextComponent
{
	private final AOTDGuiImage background;
	private final AOTDGuiPanel textContainer;
	private final AOTDGuiLabel textField;
	private boolean isFocused = false;
	private String ghostText = "";
	private float[] originalTextColor = new float[]
	{ 1.0f, 1.0f, 1.0f, 1.0f };

	public AOTDGuiTextField(int x, int y, int width, int height, TrueTypeFont font)
	{
		super(x, y, width, height, font);
		Keyboard.enableRepeatEvents(true);
		background = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/text_field_background.png");
		textContainer = new AOTDGuiPanel(5, 5, width - 10, height - 10, true);
		textField = new AOTDGuiLabel(0, 0, font);
		textContainer.add(textField);
		this.add(background);
		this.add(textContainer);
		this.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
				if (event.getClickedButton() == MouseButtonClicked.Left)
				{
					AOTDGuiTextField current = (AOTDGuiTextField) event.getSource();
					if (current.isHovered())
						current.setFocused(true);
					else
						current.setFocused(false);
				}
			}
		});
		this.addKeyListener(new AOTDKeyListener()
		{
			@Override
			public void keyTyped(AOTDKeyEvent event)
			{
				AOTDGuiTextField current = (AOTDGuiTextField) event.getSource();
				current.keyTyped(event.getKey(), event.getKeyCode());
			}
		});
	}

	public void keyTyped(char character, int keyCode)
	{
		if (this.isFocused())
		{
			// Ctrl+A
			if (AOTDGuiUtility.isCtrlKeyDown() && keyCode == Keyboard.KEY_A)
			{
				// Select all text
			}
			// Ctrl+C
			else if (AOTDGuiUtility.isCtrlKeyDown() && keyCode == Keyboard.KEY_C)
			{
				AOTDGuiUtility.setClipboardString(this.getText());
			}
			// Ctrl+V
			else if (AOTDGuiUtility.isCtrlKeyDown() && keyCode == Keyboard.KEY_V)
			{
				this.setText("");
				this.addText(ChatAllowedCharacters.filterAllowedCharacters(AOTDGuiUtility.getClipboardString()));
			}
			// Ctrl+X
			else if (AOTDGuiUtility.isCtrlKeyDown() && keyCode == Keyboard.KEY_X)
			{
				AOTDGuiUtility.setClipboardString(this.getText());
				this.setText("");
			}
			else
			{
				switch (keyCode)
				{
					// Backspace
					case Keyboard.KEY_BACK:
						removeChars(1);
						break;
					// Left arrow
					case Keyboard.KEY_LEFT:
						//this.textField.setX(this.textField.getX() - 5);
						break;
					// Right arrow
					case Keyboard.KEY_RIGHT:
						//this.textField.setX(this.textField.getX() + 5);
						break;
					default:
						// Add the character
						if (ChatAllowedCharacters.isAllowedCharacter(character))
							this.addText(Character.toString(character));
						break;
				}
			}
		}
	}

	private void removeChars(int number)
	{
		number = MathHelper.clamp(number, 0, this.getText().length());
		this.setText(this.getText().substring(0, this.getText().length() - number).concat("_"));
	}

	private void addText(String text)
	{
		text = ChatAllowedCharacters.filterAllowedCharacters(text);
		this.setText(this.getText().concat(text + "_"));
	}

	private void updateAmountOfScroll()
	{
		float textWidth = this.getFont().getWidth(this.getRawText()) * this.textScaleConstant * .966f;
		if (textWidth > this.textContainer.getWidth() - this.textContainer.getXWithoutParentTransform())
			this.textField.setX((int) (this.textField.getParent().getX() + this.textField.getParent().getWidth() - textWidth - this.textContainer.getXWithoutParentTransform()) - 5);
		else
			this.textField.setX(this.textField.getParent().getX() - 5);
	}

	@Override
	public void draw()
	{
		if (this.isVisible())
		{
			super.draw();
		}
	}

	@Override
	public void setText(String text)
	{
		super.setText(text);
		this.textField.setText(text);
		this.updateAmountOfScroll();
	}

	@Override
	public String getText()
	{
		String text = this.getRawText();
		return text.length() == 0 ? "" : text.charAt(text.length() - 1) == '_' ? text.substring(0, text.length() - 1) : text;
	}

	public String getRawText()
	{
		return super.getText();
	}

	public void setFocused(boolean isFocused)
	{
		boolean wasFocused = this.isFocused;
		this.isFocused = isFocused;
		if (!wasFocused && isFocused)
		{
			this.background.setColor(new Color(230, 230, 230));
			String text = this.getRawText();
			if (text == this.getGhostText())
			{
				this.setText("_");
				this.textField.setTextColor(this.originalTextColor);
			}
			else
				this.setText(text.concat("_"));
		}
		else if (wasFocused && !isFocused)
		{
			this.background.setColor(Color.WHITE);
			String text = this.getRawText();
			if (this.getText().isEmpty())
				this.loadGhostText();
			else
				this.setText(text.charAt(text.length() - 1) == '_' ? text.substring(0, text.length() - 1) : text);
		}
	}

	public void setTextColor(Color color)
	{
		this.textField.setTextColor(color);
	}

	public boolean isFocused()
	{
		return this.isFocused;
	}

	public void setGhostText(String ghostText)
	{
		this.ghostText = ghostText;
		if (this.getText().isEmpty())
			this.loadGhostText();
	}

	private void loadGhostText()
	{
		this.setText(ghostText);
		this.originalTextColor = this.textField.getTextColor();
		this.setTextColor(Color.gray);
	}

	public String getGhostText()
	{
		return this.ghostText;
	}
}
