package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDScreen
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import net.minecraft.client.Minecraft
import net.minecraft.item.Item
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.text.TranslationTextComponent
import org.lwjgl.glfw.GLFW
import java.awt.Color

/**
 * Journal page UI which is shown when a player opens a page
 *
 * @constructor Initializes the entire UI
 * @param text The text on the pages to render
 * @param titleText The text of the title
 * @param relatedItemRecipes The items that we should show recipes for
 * @property completeText The complete text that is to be shown on the GUI
 * @property textOnEachPage A partitioned list the "complete text" list to be written on each page
 * @property researchRecipes A list of recipes for this research
 * @property leftPage The text box of the left page
 * @property rightPage The text box of the right page
 * @property forwardButton The button to go forward
 * @property backwardButton The button to go backward
 * @property leftPageNumber The left page number
 * @property rightPageNumber The right page number
 * @property topLeftRecipe The top left recipe image box
 * @property bottomLeftRecipe The bottom left recipe image box
 * @property topRightRecipe The top right recipe image box
 * @property bottomRightRecipe The bottom right recipe image box
 * @property pageNumber The current page we're on
 */
class BloodStainedJournalPageScreen(text: String, titleText: String, relatedItemRecipes: List<Item>) :
    AOTDScreen(TranslationTextComponent("screen.afraidofthedark.blood_stained_journal_page")) {
    private val completeText: String
    private val textOnEachPage: MutableList<String> = mutableListOf()
    private val researchRecipes: List<IRecipe<*>>
    private val leftPage: AOTDGuiTextBox
    private val rightPage: AOTDGuiTextBox
    private val forwardButton: AOTDGuiButton
    private val backwardButton: AOTDGuiButton
    private val leftPageNumber: AOTDGuiLabel
    private val rightPageNumber: AOTDGuiLabel
    private val topLeftRecipe: AOTDGuiRecipe
    private val bottomLeftRecipe: AOTDGuiRecipe
    private val topRightRecipe: AOTDGuiRecipe
    private val bottomRightRecipe: AOTDGuiRecipe
    private var pageNumber = 0

    init {
        // Get a list of recipes for each item
        researchRecipes = entityPlayer.world.recipeManager.recipes.filter { relatedItemRecipes.contains(it.recipeOutput.item) }

        // Store the raw text of the research
        completeText = text

        // Set the width and height of the journal
        val journalWidth = 256
        val journalHeight = 256

        // Calculate the x and y corner positions of the page
        val xCornerOfPage = (Constants.GUI_WIDTH - journalWidth) / 2
        val yCornerOfPage = (Constants.GUI_HEIGHT - journalHeight) / 2

        // Create a panel to contain everything
        val journalBackground = AOTDGuiPanel(xCornerOfPage, yCornerOfPage, journalWidth, journalHeight, false)

        // Create a page image to be used as the background
        val pageBackgroundImage =
            AOTDGuiImage(0, 0, journalWidth, journalHeight, "afraidofthedark:textures/gui/journal_page/background.png")
        journalBackground.add(pageBackgroundImage)

        // Create red colors for text
        val textColor = Color(170, 3, 25)
        val titleColor = Color(200, 0, 0)

        // Create a title label to contain the research name
        val titleLabel = AOTDGuiLabel(
            xCornerOfPage,
            yCornerOfPage - 25,
            journalWidth, 25,
            ClientData.getOrCreate(50f)
        )
        titleLabel.text = titleText
        titleLabel.textColor = titleColor
        titleLabel.textAlignment = TextAlignment.ALIGN_CENTER
        contentPane.add(titleLabel)

        // Create two page numbers, one for the left page and one for the right page
        leftPageNumber = AOTDGuiLabel(15, 12, 15, 15, ClientData.getOrCreate(32f))
        rightPageNumber = AOTDGuiLabel(journalWidth - 27, 12, 15, 15, ClientData.getOrCreate(32f))
        // Align the right page number right so that it fits into the corner
        rightPageNumber.textAlignment = TextAlignment.ALIGN_RIGHT
        // Start the page numbers at 1 and 2
        leftPageNumber.text = "1"
        rightPageNumber.text = "2"
        // Both page numbers are red
        leftPageNumber.textColor = textColor
        rightPageNumber.textColor = textColor
        // Add both page numbers
        journalBackground.add(leftPageNumber)
        journalBackground.add(rightPageNumber)

        // Create two pages, one for the left page text and one for the right page text
        leftPage = AOTDGuiTextBox(15, 30, journalWidth / 2 - 20, journalHeight - 70, ClientData.getOrCreate(32f))
        rightPage = AOTDGuiTextBox(135, 30, journalWidth / 2 - 20, journalHeight - 70, ClientData.getOrCreate(32f))
        // Set the text on both pages to red
        leftPage.textColor = textColor
        rightPage.textColor = textColor
        // Add the pages to the journal
        journalBackground.add(leftPage)
        journalBackground.add(rightPage)

        // The bookmark button returns the user to the research screen
        // The bookmark button to go back
        val bookmarkButton = AOTDGuiButton(
            journalWidth / 2 - 16,
            journalHeight - 28,
            15,
            30,
            "afraidofthedark:textures/gui/journal_page/slot_highlight.png"
        )
        // Hide the button to start
        bookmarkButton.isVisible = false
        // Set the color to a see-through white
        bookmarkButton.color = Color(255, 255, 255, 50)
        // When we click the bookmark return to the journal research ui
        bookmarkButton.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    Minecraft.getInstance().displayGuiScreen(BloodStainedJournalResearchScreen(false))
                }
            }
        }
        // When we hover the bookmark button show/hide it
        bookmarkButton.addMouseMoveListener {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                it.source.isVisible = true
            } else if (it.eventType == AOTDMouseMoveEvent.EventType.Exit) {
                it.source.isVisible = false
            }
        }
        journalBackground.add(bookmarkButton)

        // Initialize 4 recipes, two for the left page and two for the right page
        topLeftRecipe = AOTDGuiRecipe(10, 38, 110, 90)
        journalBackground.add(topLeftRecipe)
        bottomLeftRecipe = AOTDGuiRecipe(10, 130, 110, 90)
        journalBackground.add(bottomLeftRecipe)
        topRightRecipe = AOTDGuiRecipe(130, 38, 110, 90)
        journalBackground.add(topRightRecipe)
        bottomRightRecipe = AOTDGuiRecipe(130, 130, 110, 90)
        journalBackground.add(bottomRightRecipe)

        // Create the forward and backward button to advance and rewind pages
        forwardButton = AOTDGuiButton(
            journalWidth - 23,
            journalHeight - 40,
            16,
            16,
            "afraidofthedark:textures/gui/journal_page/forward_button.png",
            "afraidofthedark:textures/gui/journal_page/forward_button_hovered.png"
        )
        backwardButton = AOTDGuiButton(
            10,
            journalHeight - 40,
            16,
            16,
            "afraidofthedark:textures/gui/journal_page/backward_button.png",
            "afraidofthedark:textures/gui/journal_page/backward_button_hovered.png"
        )
        // Upon clicking forward then advance the page, if we hover the button darken the color, if we don't hover the button brighten the color
        forwardButton.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    advancePage()
                }
            }
        }
        // Upon clicking backward then rewind the page, if we hover the button darken the color, if we don't hover the button brighten the color
        backwardButton.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    rewindPage()
                }
            }
        }
        // Add the buttons to the pane
        journalBackground.add(forwardButton)
        journalBackground.add(backwardButton)

        // Add the journal to the content pane
        contentPane.add(journalBackground)

        // Update the text on each page
        this.updateText()
        // Update the actual page content
        this.refreshPagesForNumber()

        // Hide the forward button if we're not on the last page
        forwardButton.isVisible = this.hasPageForward()
        // Hide the backward button if we're not on the first page
        backwardButton.isVisible = this.hasPageBackward()

        contentPane.addKeyListener {
            if (it.eventType == AOTDKeyEvent.KeyEventType.Press) {
                if (isInventoryKeybind(it.key, it.scanCode)) {
                    Minecraft.getInstance().displayGuiScreen(BloodStainedJournalResearchScreen(false))
                } else if (it.key == GLFW.GLFW_KEY_A || it.key == GLFW.GLFW_KEY_LEFT) {
                    rewindPage()
                } else if (it.key == GLFW.GLFW_KEY_D || it.key == GLFW.GLFW_KEY_RIGHT) {
                    advancePage()
                }
            }
        }

        // Play a page turn sound to the player
        entityPlayer.playSound(ModSounds.PAGE_TURN, 1.0f, 1.0f)
    }

    /**
     * Advances the page to the next page
     */
    private fun advancePage() {
        // Ensure we can advance the page
        if (this.hasPageForward()) {
            // Advance the page number
            pageNumber = pageNumber + 2
            // Play the turn sound
            entityPlayer.playSound(ModSounds.PAGE_TURN, 1.0f, 1.0f)
            // Update the page content
            this.refreshPagesForNumber()
        }
    }

    /**
     * Rewinds the page to the previous page
     */
    private fun rewindPage() {
        // Ensure we can rewind the page
        if (this.hasPageBackward()) {
            // Rewind the page number
            pageNumber = pageNumber - 2
            // Play the turn sound
            entityPlayer.playSound(ModSounds.PAGE_TURN, 1.0f, 1.0f)
            // Update the page content
            this.refreshPagesForNumber()
        }
    }

    /**
     * @return True if we can rewind the page, or false otherwise
     */
    private fun hasPageBackward(): Boolean {
        return pageNumber != 0
    }

    /**
     * @return True if we can advance the page, or false otherwise
     */
    private fun hasPageForward(): Boolean {
        val hasMoreText = textOnEachPage.hasIndex(pageNumber + 2)
        val hasMoreRecipes = researchRecipes.hasIndex((pageNumber + 2 - textOnEachPage.size) * 2)
        return hasMoreText || hasMoreRecipes
    }

    /**
     * Updates the text or recipes on each page
     */
    private fun refreshPagesForNumber() {
        // Update the page numbers
        leftPageNumber.text = (pageNumber + 1).toString()
        rightPageNumber.text = (pageNumber + 2).toString()

        // Hide the forward button if we're not on the last page
        forwardButton.isVisible = hasPageForward()
        // Hide the backward button if we're not on the first page
        backwardButton.isVisible = hasPageBackward()

        // The adjusted index for an index into the recipe list
        var adjustedIndexForRecipe = (pageNumber - textOnEachPage.size) * 2

        // If we have another page of text then load that page of text and clear out the recipes
        if (textOnEachPage.hasIndex(pageNumber)) {
            leftPage.setText(textOnEachPage[pageNumber])
            topLeftRecipe.setRecipe(null)
            bottomLeftRecipe.setRecipe(null)
            adjustedIndexForRecipe = adjustedIndexForRecipe + 2
        } else {
            leftPage.setText("")
            // If we have another recipe load it into the top left box, otherwise clear it
            topLeftRecipe.setRecipe(if (researchRecipes.hasIndex(adjustedIndexForRecipe)) researchRecipes[adjustedIndexForRecipe++] else null)
            // If we have another recipe load it into the bottom left box, otherwise clear it
            bottomLeftRecipe.setRecipe(if (researchRecipes.hasIndex(adjustedIndexForRecipe)) researchRecipes[adjustedIndexForRecipe++] else null)
        }

        // If we have another page of text then load that page of text and clear out the recipes
        if (textOnEachPage.hasIndex(pageNumber + 1)) {
            rightPage.setText(textOnEachPage[pageNumber + 1])
            topRightRecipe.setRecipe(null)
            bottomRightRecipe.setRecipe(null)
        } else {
            rightPage.setText("")
            // If we have another recipe load it into the top left box, otherwise clear it
            topRightRecipe.setRecipe(if (researchRecipes.hasIndex(adjustedIndexForRecipe)) researchRecipes[adjustedIndexForRecipe++] else null)
            // If we have another recipe load it into the bottom left box, otherwise clear it
            bottomRightRecipe.setRecipe(if (researchRecipes.hasIndex(adjustedIndexForRecipe)) researchRecipes[adjustedIndexForRecipe] else null)
        }
    }

    /**
     * Updates the text based on the size of text boxes
     */
    private fun updateText() {
        // Clear the text distribution to start out with
        textOnEachPage.clear()
        // Create a variable that will be the text to distribute
        var textToDistribute = completeText
        // An alternator variable to switch between adding text to the left and right box
        var alternator = true
        // Loop while we have text to distribute
        while (textToDistribute.isNotEmpty()) {
            // Left over text
            val leftOver = if (alternator) {
                // Set the text of the left page, then retrieve the text that doesnt fit into the box
                leftPage.setText(textToDistribute)
                leftPage.overflowText
            } else {
                // Set the text of the right page, then retrieve the text that doesnt fit into the box
                rightPage.setText(textToDistribute)
                rightPage.overflowText
            }

            // Flip our alternator
            alternator = !alternator

            // Grab the text that will go on this page alone
            val pageText = textToDistribute.substring(0, textToDistribute.length - leftOver.length)
            // Update the remaining text that needs distributing
            textToDistribute = textToDistribute.substring(textToDistribute.length - leftOver.length)

            // Add the page of text
            textOnEachPage.add(pageText)
        }
    }

    /**
     * Returns true if the list has the given index or false otherwise
     *
     * @param index The index to check
     * @return True if the list has the index, false otherwise
     */
    private fun <T> List<T>.hasIndex(index: Int): Boolean {
        return index >= 0 && index < this.size
    }

    /**
     * @return False since the inventory key does not close the screen
     */
    override fun inventoryToCloseGuiScreen(): Boolean {
        return false
    }

    /**
     * @return True since the screen should have a gradient background
     */
    override fun drawGradientBackground(): Boolean {
        return true
    }
}