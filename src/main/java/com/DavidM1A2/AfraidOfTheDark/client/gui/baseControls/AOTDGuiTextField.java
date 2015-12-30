/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.TrueTypeFont;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

public class AOTDGuiTextField extends AOTDGuiTextComponent
{
	private final AOTDGuiImage background;
	private final AOTDGuiPanel textContainer;
	private final AOTDGuiLabel textField;
	private boolean isFocused = false;

	public AOTDGuiTextField(int x, int y, int width, int height, TrueTypeFont font)
	{
		super(x, y, width, height, font);
		Keyboard.enableRepeatEvents(true);
		background = new AOTDGuiImage(0, 0, width, height, "textures/gui/textFieldBackground.png");
		textContainer = new AOTDGuiPanel(5, 5, width - 10, height - 10, true);
		textField = new AOTDGuiLabel(0, 0, font);
		textContainer.add(textField);
		this.add(background);
		this.add(textContainer);
		this.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (component == AOTDGuiTextField.this)
				{
					AOTDGuiTextField current = AOTDGuiTextField.this;
					if (actionType == ActionType.MousePressed)
					{
						if (current.isHovered())
							current.setFocused(true);
						else
							current.setFocused(false);
					}
					else if (actionType == ActionType.KeyTyped)
					{
						current.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
					}
				}
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
						this.textField.setX(this.textField.getX() - 5);
						break;
					// Right arrow
					case Keyboard.KEY_RIGHT:
						this.textField.setX(this.textField.getX() + 5);
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
		number = MathHelper.clamp_int(number, 0, this.getText().length());
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
		{
			this.textField.setX((int) (this.textField.getParent().getX() + this.textField.getParent().getWidth() - textWidth - this.textContainer.getXWithoutParentTransform()) - 5);
		}
		else
		{
			this.textField.setX(this.textField.getParent().getX() - 5);
		}
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
		textField.setText(text);
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
			this.setText(text.concat("_"));
		}
		else if (wasFocused && !isFocused)
		{
			this.background.setColor(Color.WHITE);
			String text = this.getRawText();
			this.setText(StringUtils.isEmpty(text) ? "" : text.charAt(text.length() - 1) == '_' ? text.substring(0, text.length() - 1) : text);
		}
	}

	public void setTextColor(Color color)
	{
		this.textField.setColor(color);
	}

	public boolean isFocused()
	{
		return this.isFocused;
	}
}
