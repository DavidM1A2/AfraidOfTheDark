package com.davidm1a2.afraidofthedark.client.gui.guiScreens;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiScreen;
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent;
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*;
import com.davidm1a2.afraidofthedark.client.settings.ClientData;
import com.davidm1a2.afraidofthedark.common.constants.Constants;
import com.davidm1a2.afraidofthedark.common.constants.ModSounds;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Journal page UI which is shown when a player opens a page
 */
public class BloodStainedJournalPageGUI extends AOTDGuiScreen
{
    // The complete text that is to be shown on the GUI
    private final String completeText;

    // A partitioned list the "complete text" list to be written on each page
    private final List<String> textOnEachPage = new LinkedList<>();
    // A list of recipes for this research
    private final List<IRecipe> researchRecipes = new ArrayList<>();
    // The text box of the left page
    private AOTDGuiTextBox leftPage;
    private AOTDGuiTextBox rightPage;
    // The button to go forward and backwards
    private AOTDGuiButton forwardButton;
    private AOTDGuiButton backwardButton;
    // The left page number and right page number
    private AOTDGuiLabel leftPageNumber;
    private AOTDGuiLabel rightPageNumber;
    // The 4 possible recipe positions
    private AOTDGuiRecipe topLeftRecipe;
    private AOTDGuiRecipe bottomLeftRecipe;
    private AOTDGuiRecipe topRightRecipe;
    private AOTDGuiRecipe bottomRightRecipe;
    // The current page we're on
    private int pageNumber = 0;

    /**
     * Initializes the entire UI
     *
     * @param text               The text to place on the page
     * @param titleText          The title of the research
     * @param relatedItemRecipes The related items to get recipes for
     */
    public BloodStainedJournalPageGUI(String text, String titleText, List<Item> relatedItemRecipes)
    {
        super();

        // Iterate over all items that we want to show recipes for
        for (Item item : relatedItemRecipes)
            // Iterate over all recipes known
            for (IRecipe recipe : CraftingManager.REGISTRY)
                // If the recipe's output is our item store that recipe
                if (recipe.getRecipeOutput().getItem() == item)
                {
                    researchRecipes.add(recipe);
                }

        // Store the raw text of the research
        this.completeText = text;

        // Set the width and height of the journal
        int journalWidth = 256;
        int journalHeight = 256;
        // Calculate the x and y corner positions of the page
        int xCornerOfPage = (Constants.GUI_WIDTH - journalWidth) / 2;
        int yCornerOfPage = (Constants.GUI_HEIGHT - journalHeight) / 2;

        // Create a panel to contain everyting
        // The journal background
        AOTDGuiPanel journalBackground = new AOTDGuiPanel(xCornerOfPage, yCornerOfPage, journalWidth, journalHeight, false);

        // Create a page image to be used as the background
        AOTDGuiImage pageBackgroundImage = new AOTDGuiImage(0, 0, journalWidth, journalHeight, "afraidofthedark:textures/gui/journal_page/background.png");
        journalBackground.add(pageBackgroundImage);

        // Create red colors for text
        final Color TEXT_RED = new Color(170, 3, 25);
        final Color TITLE = new Color(200, 0, 0);

        // Create a title label to contain the research name
        AOTDGuiLabel titleLabel = new AOTDGuiLabel(xCornerOfPage, yCornerOfPage - 25, journalWidth, 25, ClientData.getInstance().getTargaMSHandFontSized(50f));
        titleLabel.setText(titleText);
        titleLabel.setTextColor(TITLE);
        titleLabel.setTextAlignment(TextAlignment.ALIGN_CENTER);
        this.getContentPane().add(titleLabel);

        // Create two page numbers, one for the left page and one for the right page
        this.leftPageNumber = new AOTDGuiLabel(15, 12, 15, 15, ClientData.getInstance().getTargaMSHandFontSized(32f));
        this.rightPageNumber = new AOTDGuiLabel(journalWidth - 27, 12, 15, 15, ClientData.getInstance().getTargaMSHandFontSized(32f));
        // Align the right page number right so that it fits into the corner
        this.rightPageNumber.setTextAlignment(TextAlignment.ALIGN_RIGHT);
        // Start the page numbers at 1 and 2
        this.leftPageNumber.setText("1");
        this.rightPageNumber.setText("2");
        // Both page numbers are red
        this.leftPageNumber.setTextColor(TEXT_RED);
        this.rightPageNumber.setTextColor(TEXT_RED);
        // Add both page numbers
        journalBackground.add(this.leftPageNumber);
        journalBackground.add(this.rightPageNumber);

        // Create two pages, one for the left page text and one for the right page text
        this.leftPage = new AOTDGuiTextBox(15, 30, journalWidth / 2 - 20, journalHeight - 70, ClientData.getInstance().getTargaMSHandFontSized(32f));
        this.rightPage = new AOTDGuiTextBox(135, 30, journalWidth / 2 - 20, journalHeight - 70, ClientData.getInstance().getTargaMSHandFontSized(32f));
        // Set the text on both pages to red
        this.leftPage.setTextColor(TEXT_RED);
        this.rightPage.setTextColor(TEXT_RED);
        // Add the pages to the journal
        journalBackground.add(this.leftPage);
        journalBackground.add(this.rightPage);

        // The bookmark button returns the user to the research screen
        // The bookmark button to go back
        AOTDGuiButton bookmarkButton = new AOTDGuiButton(journalWidth / 2 - 16, journalHeight - 28, 15, 30, null, "afraidofthedark:textures/gui/journal_page/slot_highlight.png");
        // Hide the button to start
        bookmarkButton.setVisible(false);
        // Set the color to a see-through white
        bookmarkButton.setColor(new Color(255, 255, 255, 50));
        // When we click the bookmark return to the journal research ui
        bookmarkButton.addMouseListener(event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Click)
            {
                if (event.getSource().isHovered() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    entityPlayer.openGui(AfraidOfTheDark.INSTANCE, AOTDGuiHandler.BLOOD_STAINED_JOURNAL_ID, entityPlayer.world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
                }
            }
        });

        // When we hover the bookmark button show/hide it
        bookmarkButton.addMouseMoveListener(event ->
        {
            if (event.getEventType() == AOTDMouseMoveEvent.EventType.Enter)
            {
                event.getSource().setVisible(true);
            }
            else if (event.getEventType() == AOTDMouseMoveEvent.EventType.Exit)
            {
                event.getSource().setVisible(false);
            }
        });
        journalBackground.add(bookmarkButton);

        // Initialize 4 recipes, two for the left page and two for the right page
        this.topLeftRecipe = new AOTDGuiRecipe(10, 38, 110, 90, null);
        journalBackground.add(this.topLeftRecipe);
        this.bottomLeftRecipe = new AOTDGuiRecipe(10, 130, 110, 90, null);
        journalBackground.add(this.bottomLeftRecipe);
        this.topRightRecipe = new AOTDGuiRecipe(130, 38, 110, 90, null);
        journalBackground.add(this.topRightRecipe);
        this.bottomRightRecipe = new AOTDGuiRecipe(130, 130, 110, 90, null);
        journalBackground.add(this.bottomRightRecipe);

        // Create the forward and backward button to advance and rewind pages
        this.forwardButton = new AOTDGuiButton(journalWidth - 23, journalHeight - 40, 16, 16, null, "afraidofthedark:textures/gui/journal_page/forward_button.png", "afraidofthedark:textures/gui/journal_page/forward_button_hovered.png");
        this.backwardButton = new AOTDGuiButton(10, journalHeight - 40, 16, 16, null, "afraidofthedark:textures/gui/journal_page/backward_button.png", "afraidofthedark:textures/gui/journal_page/backward_button_hovered.png");
        // Upon clicking forward then advance the page, if we hover the button darken the color, if we don't hover the button brighten the color
        this.forwardButton.addMouseListener(event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Click)
            {
                if (event.getSource().isHovered() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    BloodStainedJournalPageGUI.this.advancePage();
                }
            }
        });
        // Upon clicking backward then rewind the page, if we hover the button darken the color, if we don't hover the button brighten the color
        this.backwardButton.addMouseListener(event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Click)
            {
                if (event.getSource().isHovered() && event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    BloodStainedJournalPageGUI.this.rewindPage();
                }
            }
        });
        // Add the buttons to the pane
        journalBackground.add(this.forwardButton);
        journalBackground.add(this.backwardButton);

        // Add the journal to the content pane
        this.getContentPane().add(journalBackground);


        // Update the text on each page
        this.updateText();
        // Update the actual page content
        this.refreshPagesForNumber();

        // Hide the forward button if we're not on the last page
        this.forwardButton.setVisible(this.hasPageForward());
        // Hide the backward button if we're not on the first page
        this.backwardButton.setVisible(this.hasPageBackward());

        // Play a page turn sound to the player
        entityPlayer.playSound(ModSounds.PAGE_TURN, 1.0f, 1.0f);
    }

    /**
     * Advances the page to the next page
     */
    private void advancePage()
    {
        // Ensure we can advance the page
        if (this.hasPageForward())
        {
            // Advance the page number
            pageNumber = pageNumber + 2;
            // Play the turn sound
            entityPlayer.playSound(ModSounds.PAGE_TURN, 1.0f, 1.0f);
            // Update the page content
            this.refreshPagesForNumber();
        }
    }

    /**
     * Rewinds the page to the previous page
     */
    private void rewindPage()
    {
        // Ensure we can rewind the page
        if (this.hasPageBackward())
        {
            // Rewind the page number
            pageNumber = pageNumber - 2;
            // Play the turn sound
            entityPlayer.playSound(ModSounds.PAGE_TURN, 1.0F, 1.0F);
            // Update the page content
            this.refreshPagesForNumber();
        }
    }

    /**
     * @return True if we can rewind the page, or false otherwise
     */
    private boolean hasPageBackward()
    {
        return pageNumber != 0;
    }

    /**
     * @return True if we can advance the page, or false otherwise
     */
    private boolean hasPageForward()
    {
        boolean hasMoreText = this.hasIndex(this.textOnEachPage, pageNumber + 2);
        boolean hasMoreRecipes = this.hasIndex(this.researchRecipes, (pageNumber + 2 - this.textOnEachPage.size()) * 2);
        return hasMoreText || hasMoreRecipes;
    }

    /**
     * Updates the text or recipes on each page
     */
    private void refreshPagesForNumber()
    {
        // Update the page numbers
        this.leftPageNumber.setText(Integer.toString(this.pageNumber + 1));
        this.rightPageNumber.setText(Integer.toString(this.pageNumber + 2));
        // Hide the forward button if we're not on the last page
        this.forwardButton.setVisible(this.hasPageForward());
        // Hide the backward button if we're not on the first page
        this.backwardButton.setVisible(this.hasPageBackward());

        // The adjusted index for an index into the recipe list
        int adjustedIndexForRecipe = (pageNumber - textOnEachPage.size()) * 2;
        // If we have another page of text then load that page of text and clear out the recipes
        if (this.hasIndex(this.textOnEachPage, pageNumber))
        {
            this.leftPage.setText(this.textOnEachPage.get(pageNumber));
            this.topLeftRecipe.setRecipe(null);
            this.bottomLeftRecipe.setRecipe(null);
            adjustedIndexForRecipe = adjustedIndexForRecipe + 2;
        }
        // If we don't have any more text then clear the left page of text and show any recipes if we have them
        else
        {
            this.leftPage.setText("");
            // If we have another recipe load it into the top left box, otherwise clear it
            this.topLeftRecipe.setRecipe(this.hasIndex(this.researchRecipes, adjustedIndexForRecipe) ? this.researchRecipes.get(adjustedIndexForRecipe++) : null);
            // If we have another recipe load it into the bottom left box, otherwise clear it
            this.bottomLeftRecipe.setRecipe(this.hasIndex(this.researchRecipes, adjustedIndexForRecipe) ? this.researchRecipes.get(adjustedIndexForRecipe++) : null);
        }

        // If we have another page of text then load that page of text and clear out the recipes
        if (this.hasIndex(this.textOnEachPage, pageNumber + 1))
        {
            this.rightPage.setText(this.textOnEachPage.get(pageNumber + 1));
            this.topRightRecipe.setRecipe(null);
            this.bottomRightRecipe.setRecipe(null);
        }
        else
        {
            this.rightPage.setText("");
            // If we have another recipe load it into the top left box, otherwise clear it
            this.topRightRecipe.setRecipe(this.hasIndex(this.researchRecipes, adjustedIndexForRecipe) ? this.researchRecipes.get(adjustedIndexForRecipe++) : null);
            // If we have another recipe load it into the bottom left box, otherwise clear it
            this.bottomRightRecipe.setRecipe(this.hasIndex(this.researchRecipes, adjustedIndexForRecipe) ? this.researchRecipes.get(adjustedIndexForRecipe++) : null);
        }
    }

    /**
     * Updates the text based on the size of text boxes
     */
    private void updateText()
    {
        // Clear the text distribution to start out with
        this.textOnEachPage.clear();
        // Create a variable that will be the text to distribute
        String textToDistribute = this.completeText;
        // An alternator variable to switch between adding text to the left and right box
        boolean alternator = true;

        // Loop while we have text to distribute
        while (StringUtils.isNotEmpty(textToDistribute))
        {
            // Left over text
            String leftOver;
            if (alternator)
            {
                // Set the text of the left page, then retrieve the text that doesnt fit into the box
                leftPage.setText(textToDistribute);
                leftOver = leftPage.getOverflowText();
            }
            else
            {
                // Set the text of the right page, then retrieve the text that doesnt fit into the box
                rightPage.setText(textToDistribute);
                leftOver = rightPage.getOverflowText();
            }
            // Flip our alternator
            alternator = !alternator;

            // Grab the text that will go on this page alone
            String pageText = textToDistribute.substring(0, textToDistribute.length() - leftOver.length());
            // Update the remaining text that needs distributing
            textToDistribute = textToDistribute.substring(textToDistribute.length() - leftOver.length());

            // Add the page of text
            this.textOnEachPage.add(pageText);
        }
    }

    /**
     * Called whenever a key is typed, we ask our key handler to handle the event
     *
     * @param character The character typed
     * @param keyCode   The code of the character typed
     * @throws IOException forwarded from the super method
     */
    @Override
    protected void keyTyped(char character, int keyCode) throws IOException
    {
        // If we press our inventory button close the UI and go to the journal UI
        if (keyCode == INVENTORY_KEYCODE)
        {
            entityPlayer.openGui(AfraidOfTheDark.INSTANCE, AOTDGuiHandler.BLOOD_STAINED_JOURNAL_ID, entityPlayer.world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
        }
        // If we press 'a', 'A', or left arrow rewind
        else if (character == 'a' || character == 'A' || keyCode == Keyboard.KEY_LEFT)
        {
            this.rewindPage();
        }
        // If we press 'd', 'D', or right arrow advamce
        else if (character == 'd' || character == 'D' || keyCode == Keyboard.KEY_RIGHT)
        {
            this.advancePage();
        }
        super.keyTyped(character, keyCode);
    }

    /**
     * Returns true if the list has the given index or false otherwise
     *
     * @param list  The list to test
     * @param index The index to check
     * @return True if the list has the index, false otherwise
     */
    private boolean hasIndex(List<?> list, int index)
    {
        return index >= 0 && index < list.size();
    }

    /**
     * @return False since the inventory key does not close the screen
     */
    @Override
    public boolean inventoryToCloseGuiScreen()
    {
        return false;
    }

    /**
     * @return True since the screen should have a gradient background
     */
    @Override
    public boolean drawGradientBackground()
    {
        return true;
    }
}
