package com.DavidM1A2.afraidofthedark.client.gui.guiScreens;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiScreen;
import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiButton;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiImage;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiTextField;
import com.DavidM1A2.afraidofthedark.client.settings.ClientData;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.packets.otherPackets.ProcessSextantInput;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.util.Color;

/**
 * Gui screen that represents the sextant GUI
 */
public class SextantGUI extends AOTDGuiScreen
{
    // The text field containing the meteor's drop angle
    private final AOTDGuiTextField angle;
    // The text field containing the meteor's latitude
    private final AOTDGuiTextField latitude;
    // The text field containing the meteor's longitude
    private final AOTDGuiTextField longitude;

    /**
     * The constructor initializes the GUI
     */
    public SextantGUI()
    {
        // The gui will be 256x256
        final int GUI_SIZE = 256;

        // Background panel holds all the gui items
        AOTDGuiPanel background = new AOTDGuiPanel((Constants.GUI_WIDTH - GUI_SIZE) / 2, (Constants.GUI_HEIGHT - GUI_SIZE) / 2, 256, 256, false);

        // Add an image to the background of the sextant texture
        AOTDGuiImage backgroundImage = new AOTDGuiImage(0, 0, GUI_SIZE, GUI_SIZE, "afraidofthedark:textures/gui/telescope/sextant.png");
        background.add(backgroundImage);

        // Grab the font for the text fields
        TrueTypeFont textFieldFont = ClientData.getInstance().getTargaMSHandFontSized(45f);
        // Initialize fields
        this.angle = new AOTDGuiTextField(15, 108, 120, 30, textFieldFont);
        this.latitude = new AOTDGuiTextField(15, 140, 120, 30, textFieldFont);
        this.longitude = new AOTDGuiTextField(15, 172, 120, 30, textFieldFont);
        // All fields are white and contain ghost text based on what they represent
        angle.setTextColor(new Color(255, 255, 255));
        angle.setGhostText("Angle");
        background.add(angle);
        latitude.setTextColor(new Color(255, 255, 255));
        latitude.setGhostText("Latitude");
        background.add(latitude);
        longitude.setTextColor(new Color(255, 255, 255));
        longitude.setGhostText("Longitude");
        background.add(longitude);

        // Create a calculate button that performs the math and returns drop location coordinates
        AOTDGuiButton confirm = new AOTDGuiButton(15, 204, 120, 20, ClientData.getInstance().getTargaMSHandFontSized(40f), "afraidofthedark:textures/gui/journal_sign/sign_button.png", "afraidofthedark:textures/gui/journal_sign/sign_button_hovered.png");
        // Text just says calculate
        confirm.setText("Calculate");
        // Center the text
        confirm.setTextAlignment(TextAlignment.ALIGN_CENTER);
        // When clicked tell the server to validate the numbers and create a meteor if possible
        confirm.addMouseListener(new AOTDMouseListener()
        {
            /**
             * Called when we click the confirm button
             *
             * @param event The event containing information about the mouse click
             */
            @Override
            public void mouseClicked(AOTDMouseEvent event)
            {
                // Ensure this button was the one clicked
                if (event.getSource().isHovered() && event.getClickedButton() == AOTDMouseEvent.MouseButtonClicked.Left)
                {
                    // Grab the text fron the text fields
                    String dropAngleText = SextantGUI.this.angle.getText();
                    String latitudeText = SextantGUI.this.latitude.getText();
                    String longitudeText = SextantGUI.this.longitude.getText();
                    // If any of the fields are empty print a message
                    if (dropAngleText.isEmpty() || latitudeText.isEmpty() || longitudeText.isEmpty())
                    {
                        entityPlayer.sendMessage(new TextComponentTranslation("aotd.sextant.process.field_empty"));
                    }
                    // Convert the strings to integers and return min_value if it's invalid
                    int dropAngle = NumberUtils.toInt(dropAngleText, Integer.MIN_VALUE);
                    int latitude = NumberUtils.toInt(latitudeText, Integer.MIN_VALUE);
                    int longitude = NumberUtils.toInt(longitudeText, Integer.MIN_VALUE);

                    // If any field is invalid send the player an error, otherwrise send the info to the server
                    if (dropAngle != Integer.MIN_VALUE && latitude != Integer.MIN_VALUE && longitude != Integer.MIN_VALUE)
                    {
                        AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(new ProcessSextantInput(dropAngle, latitude, longitude));
                    }
                    else
                    {
                        entityPlayer.sendMessage(new TextComponentTranslation("aotd.sextant.process.invalid_vals"));
                    }
                    entityPlayer.closeScreen();
                }
            }
        });
        background.add(confirm);

        this.getContentPane().add(background);
    }

    /**
     * @return True if none of the 3 fields are focused, false otherwise
     */
    @Override
    public boolean inventoryToCloseGuiScreen()
    {
        return !this.angle.isFocused() && !this.latitude.isFocused() && !this.longitude.isFocused();
    }

    /**
     * @return True since this UI uses a gradient background
     */
    @Override
    public boolean drawGradientBackground()
    {
        return true;
    }
}
